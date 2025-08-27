package com.untels.hito2labo;

public class Main {
    /**
     * Punto de entrada principal de la aplicación.
     * Inicializa la vista principal (VentanaPrincipal),
     * crea el controlador para manejar la lógica de negocio
     * y hace visible la ventana para el usuario.
     */
    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        Controlador controlador = new Controlador(ventana);
        ventana.setVisible(true);
    }
}