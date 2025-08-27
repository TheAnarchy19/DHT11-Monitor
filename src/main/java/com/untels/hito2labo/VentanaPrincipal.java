package com.untels.hito2labo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Clase principal de la ventana de la interfaz gráfica para el sistema de monitoreo.
 * Configura la disposición de los paneles y componentes visuales.
 */
public class VentanaPrincipal extends JFrame {
    private PanelTemperatura panelTemperatura;
    private PanelHumedad panelHumedad;
    private PanelControl panelControl;
    private PanelLog panelLog;
    private JLabel lblEstado;

    /**
     * Constructor de la VentanaPrincipal.
     * Inicializa y configura todos los componentes de la interfaz gráfica.
     */
    public VentanaPrincipal() {
        setTitle("Sistema de Monitoreo de Sensores - COM5");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        /*
         * Configuración del panel superior que muestra el estado de la conexión.
         */
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblEstado = new JLabel("Estado: DESCONECTADO");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstado.setForeground(Color.RED);
        panelSuperior.add(lblEstado);
        panelSuperior.add(Box.createHorizontalGlue());

        add(panelSuperior, BorderLayout.NORTH);

        /*
         * Configuración del panel central que contendrá los paneles de datos (Temperatura y Humedad).
         */
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /*
         * Panel para mostrar los datos de temperatura.
         */
        panelTemperatura = new PanelTemperatura();
        panelTemperatura.setBorder(createTitledBorder("TEMPERATURA"));
        panelCentral.add(panelTemperatura);
        panelCentral.add(Box.createRigidArea(new Dimension(20, 0)));

        /*
         * Panel para mostrar los datos de humedad.
         */
        panelHumedad = new PanelHumedad();
        panelHumedad.setBorder(createTitledBorder("HUMEDAD"));
        panelCentral.add(panelHumedad);

        add(panelCentral, BorderLayout.CENTER);

        /*
         * Panel para los controles de la aplicación.
         */
        panelControl = new PanelControl();
        add(panelControl, BorderLayout.SOUTH);

        /*
         * Panel para el registro de eventos (log).
         */
        panelLog = new PanelLog();
        panelLog.setBorder(createTitledBorder("REGISTRO DE EVENTOS"));
        add(panelLog, BorderLayout.EAST);
    }

    /*
     * Métodos auxiliares para la configuración de bordes y obtención de paneles.
     * Incluye la creación de bordes con título personalizado y métodos getter
     * para acceder a las instancias de los paneles de datos, control y log.
     */
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(new Font("Arial", Font.BOLD, 12));
        border.setTitleColor(Color.DARK_GRAY);
        return border;
    }

    public PanelTemperatura getPanelTemperatura() {
        return panelTemperatura;
    }

    public PanelHumedad getPanelHumedad() {
        return panelHumedad;
    }

    public PanelControl getPanelControl() {
        return panelControl;
    }

    public PanelLog getPanelLog() {
        return panelLog;
    }

    /**
     * Actualiza el estado de conexión mostrado en la interfaz gráfica.
     *
     * @param conectado True si está conectado, false si está desconectado.
     */
    public void setEstadoConexion(boolean conectado) {
        if (conectado) {
            lblEstado.setText("Estado: CONECTADO");
            lblEstado.setForeground(new Color(0, 150, 0));
        } else {
            lblEstado.setText("Estado: DESCONECTADO");
            lblEstado.setForeground(Color.RED);
        }
    }
}
