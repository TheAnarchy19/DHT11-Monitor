package com.untels.hito2labo;

import java.util.concurrent.atomic.AtomicBoolean;

import com.fazecast.jSerialComm.SerialPort;

public class SerialReader {
    private SerialPort serialPort;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread readThread;
    private Thread reconnectThread;
    private String portName;
    private int baudRate = 9600;
    private int reconnectInterval = 5000; // 5 segundos

    public interface DataListener {
        void onDataReceived(String data);
    }

    public interface ConnectionListener {
        void onDisconnected();
        void onReconnected();
        void onConnectionFailed(String error);
    }

    private DataListener dataListener;
    private ConnectionListener connectionListener;

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    public void setConnectionListener(ConnectionListener listener) {
        this.connectionListener = listener;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public void setReconnectInterval(int milliseconds) {
        this.reconnectInterval = milliseconds;
    }

    public void connect(String portName) throws Exception {
        this.portName = portName;
        internalConnect();
    }

    private void internalConnect() throws Exception {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setComPortTimeouts(
            SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 
            0, 
            0
        );

        if (!serialPort.openPort()) {
            throw new Exception("No se pudo abrir el puerto " + portName);
        }

        running.set(true);
        readThread = new Thread(this::readSerialData);
        readThread.setDaemon(true);
        readThread.start();

        if (connectionListener != null) {
            connectionListener.onReconnected();
        }
    }

    private void readSerialData() {
        StringBuilder buffer = new StringBuilder();
        
        while (running.get()) {
            try {
                // Verificar si el puerto sigue conectado
                if (!serialPort.isOpen() || !serialPort.isOpen()) {
                    handleDisconnection();
                    return;
                }

                // Esperar datos disponibles
                while (serialPort.bytesAvailable() == 0) {
                    if (!running.get()) return;
                    Thread.sleep(20);
                    
                    // Verificar conexión periódicamente
                    if (!serialPort.isOpen()) {
                        handleDisconnection();
                        return;
                    }
                }

                // Leer datos disponibles
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                
                // Si hay error de lectura
                if (numRead == -1) {
                    handleDisconnection();
                    return;
                }

                String chunk = new String(readBuffer, 0, numRead);
                buffer.append(chunk);

                // Procesar mensajes completos (terminados en ;)
                int endIndex;
                while ((endIndex = buffer.indexOf(";")) >= 0) {
                    String completeMessage = buffer.substring(0, endIndex).trim();
                    buffer.delete(0, endIndex + 1);
                    
                    if (dataListener != null && !completeMessage.isEmpty()) {
                        dataListener.onDataReceived(completeMessage);
                    }
                }
            } catch (Exception e) {
                if (running.get()) {
                    handleDisconnection();
                }
            }
        }
    }

    private void handleDisconnection() {
        running.set(false);
        
        // Cerrar puerto si está abierto
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
        
        // Notificar desconexión
        if (connectionListener != null) {
            connectionListener.onDisconnected();
        }
        
        // Iniciar proceso de reconexión automática
        startReconnectionAttempt();
    }

    private void startReconnectionAttempt() {
        if (reconnectThread != null && reconnectThread.isAlive()) {
            return;
        }
        
        reconnectThread = new Thread(() -> {
            while (running.get()) {
                try {
                    Thread.sleep(reconnectInterval);
                    
                    try {
                        internalConnect();
                        return; // Salir si la conexión fue exitosa
                    } catch (Exception e) {
                        if (connectionListener != null) {
                            connectionListener.onConnectionFailed(e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        
        reconnectThread.setDaemon(true);
        reconnectThread.start();
    }

    public void disconnect() {
        running.set(false);
        
        // Detener hilo de lectura
        if (readThread != null) {
            try {
                readThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Detener hilo de reconexión
        if (reconnectThread != null) {
            reconnectThread.interrupt();
        }
        
        // Cerrar puerto
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }

    public boolean isConnected() {
        return serialPort != null && serialPort.isOpen() && running.get();
    }

    public static String[] getAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName() + " - " + ports[i].getDescriptivePortName();
        }
        return portNames;
    }
    public void sendCommand(String command) {
    if (serialPort != null && serialPort.isOpen()) {
        serialPort.writeBytes((command + "\n").getBytes(), command.length() + 1);
        }
    }
}