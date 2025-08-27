package com.untels.hito2labo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Panel que combina la visualización de datos de temperatura y humedad
 * con un área de registro de eventos (log).
 */
public class PanelDatos extends JPanel {
    private JLabel lblTemperatura;
    private JLabel lblHumedad;
    private JLabel lblEstado;
    public JTextPane areaLog;

    /**
     * Constructor de PanelDatos.
     * Configura el diseño del panel y sus subpaneles, inicializa etiquetas
     * y el área de log.
     */
    public PanelDatos() {
        /*
         * Configuración general del diseño y los componentes del panel.
         */
        setLayout(new GridLayout(1, 2));

        JPanel panelValores = new JPanel(new GridLayout(3, 1));

        Font fuenteGrande = new Font("Arial", Font.BOLD, 24);
        Font fuenteNormal = new Font("Arial", Font.PLAIN, 12);

        lblTemperatura = new JLabel("Temperatura: -- °C", JLabel.CENTER);
        lblTemperatura.setFont(fuenteGrande);

        lblHumedad = new JLabel("Humedad: -- %", JLabel.CENTER);
        lblHumedad.setFont(fuenteGrande);

        lblEstado = new JLabel("Desconectado", JLabel.CENTER);
        lblEstado.setFont(fuenteNormal);
        lblEstado.setForeground(Color.RED);

        panelValores.add(lblTemperatura);
        panelValores.add(lblHumedad);
        panelValores.add(lblEstado);

        /*
         * Configuración del área de log con scroll.
         */
        areaLog = new JTextPane();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaLog);

        /*
         * Añadir los subpaneles al panel principal.
         */
        add(panelValores);
        add(scroll);
    }

    /**
     * Se actualiza los datos de temperatura y humedad mostrados en las etiquetas
     * y añade una entrada al log con color según los valores.
     *
     * @param temperatura El valor de temperatura a mostrar.
     * @param humedad     El valor de humedad a mostrar.
     */
    public void actualizarDatos(float temperatura, float humedad) {
        /*
         * Actualizar las etiquetas y preparar la entrada del log.
         */
        String tempStr = String.format("%.1f", temperatura);
        String humStr = String.format("%.1f", humedad);

        lblTemperatura.setText("Temperatura: " + tempStr + " °C");
        lblHumedad.setText("Humedad: " + humStr + " %");

        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String logEntry = String.format("[%s] Temp: %s°C, Hum: %s%%\n",
                                        timestamp, tempStr, humStr);

        /*
         * Determinar el color del texto del log según los rangos de valores y añadirlo.
         */
        if (temperatura > 30 || humedad > 80) {
            appendColoredText(logEntry, Color.RED); // Valores altos (Alerta)
        } else if (temperatura < 10 || humedad < 20) {
            appendColoredText(logEntry, Color.ORANGE); // Valores bajos (Advertencia)
        } else {
            appendColoredText(logEntry, Color.BLUE); // Valores normales
        }
    }

    /**
     * Se añade un mensaje de error al área de log en color rojo.
     *
     * @param mensaje El mensaje de error a añadir.
     */
    public void appendError(String mensaje) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        appendColoredText(String.format("[%s] ERROR: %s\n", timestamp, mensaje), Color.RED);
    }

    /**
     * Actualiza el estado de conexión mostrado en la etiqueta de estado.
     * Cambia el texto y el color de la etiqueta.
     *
     * @param conectado True si está conectado, false si está desconectado.
     */
    public void setEstadoConexion(boolean conectado) {
        if (conectado) {
            lblEstado.setText("Conectado");
            lblEstado.setForeground(new Color(0, 128, 0)); // Verde oscuro
        } else {
            lblEstado.setText("Desconectado");
            lblEstado.setForeground(Color.RED);
        }
    }

    /**
     * Se añade texto al área de log con un color específico.
     *
     * @param text  El texto a añadir.
     * @param color El color del texto.
     */
    public void appendColoredText(String text, Color color) {
        try {
            StyledDocument doc = areaLog.getStyledDocument();
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                                             StyleConstants.Foreground, color);

            int len = doc.getLength();
            doc.insertString(len, text, aset);
            /*
             * Desplazar automáticamente al final del log.
             */
            areaLog.setCaretPosition(len + text.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
