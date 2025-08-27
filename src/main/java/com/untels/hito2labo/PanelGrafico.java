package com.untels.hito2labo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

/**
 * Panel para dibujar gráficos de temperatura y humedad.
 * Mantiene un historial limitado de datos para visualización.
 */
public class PanelGrafico extends JPanel {
    private ArrayList<Float> temperaturas;
    private ArrayList<Float> humedades;

    /**
     * Constructor de PanelGrafico.
     * Inicializa las listas para almacenar los datos y establece el color de fondo.
     */
    public PanelGrafico() {
        temperaturas = new ArrayList<>();
        humedades = new ArrayList<>();
        setBackground(Color.WHITE);
    }

    /**
     * Método principal para dibujar los componentes del panel.
     * Dibuja los ejes, las líneas de temperatura y humedad, y la leyenda.
     *
     * @param g El contexto gráfico para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * No dibujar si no hay datos disponibles.
         */
        if (temperaturas.isEmpty()) return;

        /*
         * Obtener dimensiones del panel y calcular áreas de dibujo.
         */
        int width = getWidth();
        int height = getHeight();
        int margin = 30;
        int chartWidth = width - 2 * margin;
        int chartHeight = height - 2 * margin;

        /*
         * Dibujar los ejes X e Y del gráfico.
         */
        g.setColor(Color.BLACK);
        g.drawLine(margin, margin, margin, margin + chartHeight); // Eje Y
        g.drawLine(margin, margin + chartHeight, margin + chartWidth, margin + chartHeight); // Eje X

        /*
         * Dibujar la línea de temperatura en color rojo.
         */
        dibujarLinea(g, temperaturas, margin, chartWidth, chartHeight, Color.RED);

        /*
         * Dibujar la línea de humedad en color azul.
         */
        dibujarLinea(g, humedades, margin, chartWidth, chartHeight, Color.BLUE);

        /*
         * Dibujar la leyenda para identificar las líneas.
         */
        g.setColor(Color.RED);
        g.drawString("Temperatura (°C)", width - 150, margin + 20);
        g.setColor(Color.BLUE);
        g.drawString("Humedad (%)", width - 150, margin + 40);
    }

    /**
     * Dibuja una línea en el gráfico a partir de una lista de datos.
     * Escala los datos para ajustarse al área del gráfico.
     *
     * @param g          El contexto gráfico.
     * @param datos      La lista de datos a dibujar.
     * @param margin     El margen del gráfico.
     * @param chartWidth El ancho del área de dibujo del gráfico.
     * @param chartHeight La altura del área de dibujo del gráfico.
     * @param color      El color de la línea.
     */
    private void dibujarLinea(Graphics g, ArrayList<Float> datos, int margin,
                                int chartWidth, int chartHeight, Color color) {
        /*
         * Calcular el rango de los datos para escalarlos.
         */
        float max = Collections.max(datos);
        float min = Collections.min(datos);
        float rango = max - min;
        if (rango == 0) rango = 1; // Procuramos evitar la división por cero

        /*
         * Se Dibuja la línea conectando los puntos de datos escalados.
         */
        g.setColor(color);
        for (int i = 0; i < datos.size() - 1; i++) {
            int x1 = margin + (i * chartWidth / (datos.size() - 1));
            int y1 = margin + (int)(chartHeight * (1 - (datos.get(i) - min) / rango));
            int x2 = margin + ((i + 1) * chartWidth / (datos.size() - 1));
            int y2 = margin + (int)(chartHeight * (1 - (datos.get(i + 1) - min) / rango));

            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Agrega nuevos puntos de datos de temperatura y humedad al historial.
     * Limita el tamaño del historial a 50 puntos y repinta el gráfico.
     *
     * @param temp El valor de temperatura a agregar.
     * @param hum  El valor de humedad a agregar.
     */
    public void agregarDatos(float temp, float hum) {
        temperaturas.add(temp);
        humedades.add(hum);
        /*
         * Se Elimina los datos más antiguos si el historial excede el límite.
         */
        if (temperaturas.size() > 50) {
            temperaturas.remove(0);
            humedades.remove(0);
        }
        /*
         * Solicitar redibujar el panel para mostrar los nuevos datos.
         */
        repaint();
    }
}
