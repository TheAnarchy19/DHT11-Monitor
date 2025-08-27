package com.untels.hito2labo;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel que contiene los botones de control para iniciar, detener y exportar datos.
 */
public class PanelControl extends JPanel {
    private JButton btnIniciar;
    private JButton btnDetener;
    private JButton btnExportar;
    private JButton btnLedControl;

    /**
     * Constructor de PanelControl.
     * Inicializa los botones y los añade al panel.
     */
    public PanelControl() {
        /*
         * Inicialización de los botones de control.
         */
        btnIniciar = new JButton("Iniciar");
        btnDetener = new JButton("Detener");
        btnExportar = new JButton("Exportar Datos");
        btnLedControl = new JButton("Encender LED");

        /*
         * Añadir los botones al panel.
         */
        add(btnIniciar);
        add(btnDetener);
        add(btnExportar);
        add(btnLedControl);
    }

    /**
     * Establece el ActionListener para los botones del panel.
     *
     * @param listener El ActionListener a asignar a los botones.
     */
    public void setActionListener(ActionListener listener) {
        /*
         * Asignar el mismo ActionListener a todos los botones.
         */
        btnIniciar.addActionListener(listener);
        btnDetener.addActionListener(listener);
        btnExportar.addActionListener(listener);
        btnLedControl.addActionListener(listener);
    }
}
