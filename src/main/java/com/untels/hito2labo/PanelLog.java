package com.untels.hito2labo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Panel para mostrar un registro de eventos (log) con mensajes de diferentes colores.
 */
public class PanelLog extends JPanel {
    private JTextPane areaLog;

    /**
     * Constructor de PanelLog.
     * Se configura el JTextPane y se añade a un JScrollPane.
     */
    public PanelLog() {
        setLayout(new BorderLayout());
        setPreferredSize(new java.awt.Dimension(300, 0));

        /*
         * Inicializa el JTextPane para el área de log.
         * Se configura como no editable, se establece la fuente y el color de fondo.
         */
        areaLog = new JTextPane();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setBackground(new Color(240, 240, 240));

        /*
         * Crea un JScrollPane para permitir el desplazamiento en el área de log
         * y lo añade al centro del panel.
         */
        JScrollPane scroll = new JScrollPane(areaLog);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Añade un mensaje al área de log con un color específico.
     * Incluye una marca de tiempo automática.
     *
     * @param mensaje El mensaje a añadir.
     * @param color   El color del texto del mensaje.
     */
    public void appendMensaje(String mensaje, Color color) {
        try {
            StyledDocument doc = areaLog.getStyledDocument();
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                                             StyleConstants.Foreground, color);
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            doc.insertString(doc.getLength(), "[" + timestamp + "] " + mensaje + "\n", aset);
            // Desplaza automáticamente al final del log
            areaLog.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Añade un mensaje de error al área de log en color rojo.
     *
     * @param mensaje El mensaje de error a añadir.
     */
    public void appendError(String mensaje) {
        appendMensaje("ERROR: " + mensaje, Color.RED);
    }
}
