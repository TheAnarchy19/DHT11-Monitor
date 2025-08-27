package com.untels.hito2labo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel para mostrar los datos de humedad.
 * Incluye etiquetas para el valor, la unidad y el estado de la humedad,
 * además de almacenar un historial de valores.
 */
public class PanelHumedad extends JPanel {
    private JLabel lblValor;
    private JLabel lblUnidad;
    private JLabel lblEstado;
    private List<Float> historial;
    private float ultimoValor;

    /**
     * Constructor de PanelHumedad.
     * Configura el diseño del panel, inicializa las etiquetas y la lista del historial.
     */
    public PanelHumedad() {
        /*
         * Configuración del diseño del panel a un GridLayout de 3 filas y 1 columna.
         * Se establece el color de fondo y se inicializa la lista del historial.
         */
        setLayout(new GridLayout(3, 1));
        setBackground(Color.WHITE);
        this.historial = new ArrayList<>();

        /*
         * Definición de las fuentes a utilizar para las etiquetas.
         */
        Font fuenteGrande = new Font("Arial", Font.BOLD, 48);
        Font fuenteNormal = new Font("Arial", Font.PLAIN, 14);

        /*
         * Inicialización y configuración de las etiquetas para el valor, unidad y estado.
         * Se establecen los textos iniciales, fuentes y alineación.
         */
        lblValor = new JLabel("--", JLabel.CENTER);
        lblValor.setFont(fuenteGrande);

        lblUnidad = new JLabel("%", JLabel.CENTER);
        lblUnidad.setFont(new Font("Arial", Font.PLAIN, 24));

        lblEstado = new JLabel("Sin datos", JLabel.CENTER);
        lblEstado.setFont(fuenteNormal);
        lblEstado.setForeground(Color.GRAY);

        /*
         * Añadir las etiquetas al panel.
         */
        add(lblValor);
        add(lblUnidad);
        add(lblEstado);
    }

    /**
     * Actualiza los datos de humedad mostrados en el panel.
     * Almacena el valor en el historial y cambia el color del valor
     * y el texto/color del estado según el rango de humedad.
     *
     * @param humedad El valor de humedad a mostrar y almacenar.
     */
    public void actualizarDatos(float humedad) {
        this.ultimoValor = humedad;
        this.historial.add(humedad);

        String humStr = String.format("%.1f", humedad);
        lblValor.setText(humStr);

        /*
         * Actualizar el color del valor y el texto/color del estado según el rango de humedad.
         */
        if (humedad > 80) {
            lblValor.setForeground(new Color(0, 0, 150)); // Azul oscuro
            lblEstado.setText("HUMEDAD ALTA");
            lblEstado.setForeground(new Color(0, 0, 150)); // Azul oscuro
        } else if (humedad < 20) {
            lblValor.setForeground(new Color(150, 100, 0)); // Naranja/Marrón
            lblEstado.setText("HUMEDAD BAJA");
            lblEstado.setForeground(new Color(150, 100, 0)); // Naranja/Marrón
        } else {
            lblValor.setForeground(Color.BLACK);
            lblEstado.setText("NORMAL");
            lblEstado.setForeground(Color.DARK_GRAY);
        }
    }

    /**
     * Obtiene el último valor de humedad registrado.
     *
     * @return El último valor de humedad.
     */
    public float getUltimoValor() {
        return ultimoValor;
    }

    /**
     * Obtiene una copia del historial de valores de humedad.
     *
     * @return Una nueva lista que contiene el historial de humedad.
     */
    public List<Float> getHistorial() {
        return new ArrayList<>(historial);
    }

    /**
     * Restablece las etiquetas a sus valores iniciales y limpia el historial.
     */
    public void reset() {
        lblValor.setText("--");
        lblValor.setForeground(Color.BLACK);
        lblEstado.setText("Sin datos");
        lblEstado.setForeground(Color.GRAY);
        this.historial.clear();
    }
}
