package com.untels.hito2labo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;


/**
 * Clase Controlador: Gestiona la lógica central de la aplicación.
 * Actúa como intermediario entre la interfaz de usuario (VentanaPrincipal)
 * y el módulo de comunicación serial (SerialReader).
 * Maneja las acciones del usuario, procesa datos seriales y controla el estado de la conexión.
 */
public class Controlador implements ActionListener {
    private VentanaPrincipal ventana;
    private SerialReader serialReader;
    private final String PUERTO_ARDUINO = "COM5";
    private boolean monitoreando = false;
    private boolean intentandoReconexion = false;
    private int intentosReconexion = 0;
    private final int MAX_INTENTOS_RECONEXION = 5;
    private boolean primeraConexion = true;
    private Thread reconexionThread;

    /**
     * Constructor de la clase Controlador.
     * Inicializa el SerialReader, configura los listeners y establece la conexión inicial.
     *
     * @param ventana La instancia de VentanaPrincipal asociada a este controlador.
     */
    public Controlador(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.serialReader = new SerialReader();
        /*
         * Configuración de los manejadores de eventos de la interfaz de usuario.
         */
        configurarControladores();
        /*
         * Configuración de los listeners para los eventos del SerialReader.
         */
        configurarListenersSerial();
        /*
         * Intento de conexión serial inicial al iniciar el controlador.
         */
        iniciarConexionSerial();
    }

    /*
     * Configura los ActionListeners para los componentes de la interfaz de usuario.
     */
    private void configurarControladores() {
        ventana.getPanelControl().setActionListener(this);
    }

    /*
     * Configura los listeners para los eventos generados por el SerialReader,
     * como la llegada de datos o cambios en el estado de la conexión.
     */
    private void configurarListenersSerial() {
        /*
         * Listener para la recepción de datos seriales.
         */
        serialReader.setDataListener(data -> {
            SwingUtilities.invokeLater(() -> {
                if (monitoreando) {
                    procesarDatosSerial(data);
                }
            });
        });

        /*
         * Listener para los cambios en el estado de la conexión serial.
         */
        serialReader.setConnectionListener(new SerialReader.ConnectionListener() {
            @Override
            public void onDisconnected() {
                /*
                 * Manejo de la desconexión del dispositivo.
                 */
                SwingUtilities.invokeLater(() -> {
                    ventana.setEstadoConexion(false);
                    ventana.getPanelTemperatura().reset();
                    ventana.getPanelHumedad().reset();

                    if (!intentandoReconexion) {
                        ventana.getPanelLog().appendError("¡Dispositivo desconectado!");
                        iniciarReconexionAutomatica();
                    }
                });
            }

            @Override
            public void onReconnected() {
                /*
                 * Manejo de la reconexión exitosa del dispositivo.
                 */
                SwingUtilities.invokeLater(() -> {
                    ventana.setEstadoConexion(true);
                    if (!primeraConexion) {
                        ventana.getPanelLog().appendMensaje("¡Dispositivo reconectado!", new Color(0, 150, 0));
                    }
                    primeraConexion = false;
                    detenerReconexionAutomatica();

                    if (monitoreando) {
                        ventana.getPanelLog().appendMensaje("Monitoreo reanudado", new Color(0, 100, 0));
                    }
                });
            }

            @Override
            public void onConnectionFailed(String error) {
                /*
                 * Manejo de fallos en los intentos de conexión.
                 */
                SwingUtilities.invokeLater(() -> {
                    intentosReconexion++;
                    if (intentosReconexion >= MAX_INTENTOS_RECONEXION) {
                        ventana.getPanelLog().appendError("Maximos intentos de reconexión alcanzados");
                        detenerReconexionAutomatica();
                    } else {
                        ventana.getPanelLog().appendMensaje(
                            "Intento " + intentosReconexion + "/" + MAX_INTENTOS_RECONEXION,
                            Color.ORANGE
                        );
                    }
                });
            }
        });
    }

    /*
     * Inicia el proceso de reconexión automática en un hilo separado.
     */
    private void iniciarReconexionAutomatica() {
        if (reconexionThread != null && reconexionThread.isAlive()) {
            return;
        }

        intentandoReconexion = true;
        intentosReconexion = 0;

        reconexionThread = new Thread(() -> {
            /*
             * Bucle principal del hilo de reconexión.
             */
            while (intentandoReconexion && intentosReconexion < MAX_INTENTOS_RECONEXION) {
                try {
                    Thread.sleep(3000); // Espera entre intentos

                    SwingUtilities.invokeLater(() -> {
                        ventana.getPanelLog().appendMensaje("Intentando reconectar...", Color.ORANGE);
                    });

                    try {
                        serialReader.connect(PUERTO_ARDUINO);
                        return; 
                    } catch (Exception e) {
                        
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return; 
                }
            }

            /*
             * Acciones a realizar al finalizar los intentos de reconexión.
             */
            SwingUtilities.invokeLater(() -> {
                if (intentosReconexion >= MAX_INTENTOS_RECONEXION) {
                    ventana.getPanelLog().appendError("No se pudo reconectar. Intente manualmente.");
                }
                intentandoReconexion = false;
            });
        });

        reconexionThread.setDaemon(true);
        reconexionThread.start();
    }

    /*
     * Detiene el proceso de reconexión automática.
     */
    private void detenerReconexionAutomatica() {
        intentandoReconexion = false;
        if (reconexionThread != null) {
            reconexionThread.interrupt();
        }
    }

    /*
     * Intenta establecer la conexión serial inicial.
     */
    private void iniciarConexionSerial() {
        try {
            serialReader.connect(PUERTO_ARDUINO);
            ventana.setEstadoConexion(true);
            ventana.getPanelLog().appendMensaje("Conectado a " + PUERTO_ARDUINO, Color.BLUE);

        } catch (Exception e) {
            /*
             * Manejo de errores en la conexión inicial e inicio de reconexión automática.
             */
            ventana.setEstadoConexion(false);
            ventana.getPanelLog().appendError("Error de conexión: " + e.getMessage());
            iniciarReconexionAutomatica();
        }
    }

    /*
     * Procesa los datos recibidos por serial, extrayendo temperatura y humedad
     * y actualizando la interfaz gráfica.
     */
    private void procesarDatosSerial(String data) {
        try {
            /*
             * Verificación de mensajes de error del sensor.
             */
            if (data.equals("ERROR")) {
                ventana.getPanelLog().appendError("Error en sensor DHT11");
                return;
            }

            /*
             * Parseo de los datos recibidos y actualización de la UI.
             */
            String[] partes = data.split(",");
            if (partes.length == 2) {
                float temp = Float.parseFloat(partes[0].split(":")[1]);
                float hum = Float.parseFloat(partes[1].split(":")[1]);

                ventana.getPanelTemperatura().actualizarDatos(temp);
                ventana.getPanelHumedad().actualizarDatos(hum);

                ventana.getPanelLog().appendMensaje(
                    String.format(": Temp %.1f°C, Hum %.1f%%", temp, hum),
                    Color.DARK_GRAY
                );
            }
        } catch (Exception e) {
            /*
             * Manejo de errores durante el procesamiento de datos.
             */
            ventana.getPanelLog().appendError("Datos inválidos: " + data);
        }
    }

    /**
     * Maneja los eventos de acción, como la pulsación de botones.
     *
     * @param e El ActionEvent que ocurrió.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * Obtener el comando de acción y realizar la acción correspondiente.
         */
        String comando = e.getActionCommand();

        switch (comando) {
            case "Iniciar":
                /* Lógica para iniciar el monitoreo */
                if (!monitoreando) {
                    if (serialReader.isConnected()) {
                        monitoreando = true;
                        ventana.getPanelLog().appendMensaje("Monitoreo iniciado", new Color(0, 100, 0));
                    } else {
                        ventana.getPanelLog().appendError("No se puede iniciar - Dispositivo desconectado");
                    }
                }
                break;
        
            case "Encender LED":
                if (serialReader.isConnected()) {
                    serialReader.sendCommand("ON");  // Método nuevo (ver abajo)
                    ((JButton)e.getSource()).setText("Apagar LED");
                }
                break;
            case "Apagar LED":
                if (serialReader.isConnected()) {
                    serialReader.sendCommand("OFF");
                    ((JButton)e.getSource()).setText("Encender LED");
                }
                break;

            case "Detener":
                /* Lógica para detener el monitoreo */
                if (monitoreando) {
                    monitoreando = false;
                    ventana.getPanelLog().appendMensaje("Monitoreo detenido", Color.ORANGE);
                }
                break;

            case "Exportar Datos":
                /* Lógica para exportar datos */
                if (serialReader.isConnected()) {
                    ventana.getPanelLog().appendMensaje("Exportando datos...", Color.BLUE);
                    exportarDatos();
                } else {
                    ventana.getPanelLog().appendError("No se puede exportar - Dispositivo desconectado");
                }
                break;

            case "Reconectar":
                /* Lógica para intentar reconexión manual */
                if (!serialReader.isConnected() && !intentandoReconexion) {
                    ventana.getPanelLog().appendMensaje("Reconexión manual iniciada...", Color.BLUE);
                    iniciarConexionSerial();
                }
                break;
        }
    }

    /**
     * Permite al usuario seleccionar una ubicación y nombre de archivo
     * y exporta los datos de temperatura y humedad a un archivo CSV.
     */
    private void exportarDatos() {
        /*
         * Verificar si hay datos suficientes en los historiales para exportar.
         */
        if (ventana.getPanelTemperatura().getHistorial().isEmpty() ||
            ventana.getPanelHumedad().getHistorial().isEmpty()) {
            ventana.getPanelLog().appendError("No hay datos suficientes para exportar");
            return;
        }

        /*
         * Configurar y mostrar el diálogo para guardar el archivo.
         */
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar datos de sensores");
        fileChooser.setSelectedFile(new File("datos_sensores_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));

        int userSelection = fileChooser.showSaveDialog(ventana);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            /*
             * Iniciar un nuevo hilo para realizar la escritura del archivo
             * y evitar bloquear la interfaz de usuario.
             */
            new Thread(() -> {
                try (PrintWriter writer = new PrintWriter(archivo)) {
                    /*
                     * Escribir la fila de encabezado del archivo CSV.
                     */
                    writer.println("Fecha,Hora,Temperatura (C),Humedad (%)");

                    /*
                     * Obtener los historiales de datos de temperatura y humedad.
                     */
                    List<Float> temps = ventana.getPanelTemperatura().getHistorial();
                    List<Float> hums = ventana.getPanelHumedad().getHistorial();

                    /*
                     * Determinar el número de filas a escribir basándose en el historial más corto.
                     */
                    int size = Math.min(temps.size(), hums.size());

                    /*
                     * Escribir cada fila de datos en el archivo CSV.
                     */
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                    for (int i = 0; i < size; i++) {
                        String fecha = dateFormat.format(new Date());
                        String hora = timeFormat.format(new Date()); 

                        writer.printf("%s,%s,%.1f,%.1f%n",
                            fecha, hora, temps.get(i), hums.get(i));
                    }

                    /*
                     * Mostrar un mensaje de éxito en el log de la interfaz de usuario.
                     */
                    SwingUtilities.invokeLater(() -> {
                        ventana.getPanelLog().appendMensaje(
                            "Datos exportados correctamente a: " + archivo.getAbsolutePath(),
                            new Color(0, 100, 0)
                        );
                    });
                } catch (IOException e) {
                    /*
                     * Manejar y reportar cualquier error de escritura del archivo.
                     */
                    SwingUtilities.invokeLater(() -> {
                        ventana.getPanelLog().appendError("Error al exportar: " + e.getMessage());
                    });
                }
            }).start(); // Iniciar el hilo de exportación
        } else {
            /*
             * Mostrar un mensaje en el log si la exportación fue cancelada.
             */
            ventana.getPanelLog().appendMensaje("Exportación cancelada", Color.ORANGE);
        }
    }

    /**
     * Método llamado al cerrar la aplicación para limpiar recursos.
     */
    public void cerrarAplicacion() {
        /*
         * Detener procesos y desconectar el dispositivo serial.
         */
        detenerReconexionAutomatica();
        monitoreando = false;
        if (serialReader != null) {
            serialReader.disconnect();
        }
    }
}
