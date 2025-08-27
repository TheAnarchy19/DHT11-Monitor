package com.untels.hito2labo;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel para mostrar los datos de temperatura.
 * Incluye etiquetas para el valor, la unidad y el estado de la temperatura,
 * además de almacenar un historial de valores.
 */
public class PanelTemperatura extends JPanel {
    private JLabel lblValor;
    private JLabel lblUnidad;
    private JLabel lblEstado;
    private List<Float> historial;
    private float ultimoValor;

    /**
     * Constructor de PanelTemperatura.
     * Configura el diseño del panel, inicializa las etiquetas y la lista del historial.
     */
    public PanelTemperatura() {
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

        lblUnidad = new JLabel("°C", JLabel.CENTER);
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
     * Actualiza los datos de temperatura mostrados en el panel.
     * Almacena el valor en el historial y cambia el color del valor
     * y el texto/color del estado según el rango de temperatura.
     *
     * @param temperatura El valor de temperatura a mostrar y almacenar.
     */
    public void actualizarDatos(float temperatura) {
        this.ultimoValor = temperatura;
        this.historial.add(temperatura);

        String tempStr = String.format("%.1f", temperatura);
        lblValor.setText(tempStr);

        /*
         * Actualizar el color del valor y el texto/color del estado según el rango de temperatura.
         */
        if (temperatura > 30) {
            lblValor.setForeground(Color.RED);
            lblEstado.setText("ALTA TEMPERATURA");
            lblEstado.setForeground(Color.RED);
        } else if (temperatura < 10) {
            lblValor.setForeground(Color.BLUE);
            lblEstado.setText("BAJA TEMPERATURA");
            lblEstado.setForeground(Color.BLUE);
        } else {
            lblValor.setForeground(Color.BLACK);
            lblEstado.setText("NORMAL");
            lblEstado.setForeground(Color.DARK_GRAY);
        }
    }

    /**
     * Obtiene el último valor de temperatura registrado.
     *
     * @return El último valor de temperatura.
     */
    public float getUltimoValor() {
        return ultimoValor;
    }

    /**
     * Obtiene una copia del historial de valores de temperatura.
     *
     * @return Una nueva lista que contiene el historial de temperatura.
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
