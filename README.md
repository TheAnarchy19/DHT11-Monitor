---

### 🌡️ DTH11-Monitor

Este proyecto es una solución completa para el monitoreo en tiempo real de temperatura y humedad ambiental. Utiliza un sensor DHT11 conectado a una placa Arduino para la recolección de datos, y una aplicación de escritorio desarrollada en Java para la visualización, registro y análisis de la información. La aplicación de escritorio, construida con la biblioteca Swing, ofrece una interfaz de usuario intuitiva para interactuar con el dispositivo.

---

### ✨ Características Principales

* [cite_start]**Monitoreo en Tiempo Real**: Muestra la temperatura y la humedad en tiempo real, recibiendo datos del sensor DHT11 a través de la comunicación serial[cite: 1, 10].
* [cite_start]**Visualización Gráfica**: La interfaz incluye un panel gráfico que traza los valores de temperatura (°C) y humedad (%) en tiempo real, con líneas de colores para una fácil diferenciación[cite: 7].
* [cite_start]**Control del Dispositivo**: Permite al usuario controlar un LED conectado al pin 13 del Arduino desde la aplicación de escritorio[cite: 4, 10].
* [cite_start]**Registro de Eventos (Log)**: Un panel de registro captura y muestra el estado de la conexión, los datos recibidos y los mensajes de error del sensor en diferentes colores para una mejor lectura[cite: 1, 9].
* [cite_start]**Manejo de Conexiones**: La aplicación cuenta con lógica para la reconexión automática en caso de que se pierda la comunicación con la placa Arduino[cite: 1].
* [cite_start]**Exportación de Datos**: Permite exportar el historial de datos de los sensores a un archivo CSV para su posterior análisis o registro[cite: 1].

---

### 🛠️ Requisitos del Proyecto

#### Hardware

* Placa Arduino (ej. Arduino UNO).
* Sensor de Temperatura y Humedad DHT11.
* LED.
* Resistencia de 220 Ohm.
* Cables de conexión.
* Cable USB para la conexión entre el Arduino y la computadora.

#### Software

* **Arduino IDE**: Para compilar y cargar el código en la placa Arduino.
* **Software de Java**:
    * **JDK (Java Development Kit)**: Versión 8 o superior.
    * **Entorno de Desarrollo (IDE)**: Se recomienda un IDE como NetBeans o Eclipse para trabajar con el código Java.
    * **Librerías de Comunicación Serial**: El proyecto requiere una librería de comunicación serial para Java, como `RXTXcomm` o `jSerialComm`, para establecer la conexión entre la aplicación y el Arduino.

---

### 📂 Estructura del Proyecto

* **`SensorHumedadTemeraturaArduino.ino`**: Código para el Arduino que lee los datos del sensor DHT11 y los envía a través del puerto serial. [cite_start]También gestiona el encendido y apagado de un LED[cite: 10].
* [cite_start]**`Main.java`**: El punto de entrada principal de la aplicación de escritorio[cite: 2].
* **`Controlador.java`**: La clase central del proyecto. [cite_start]Gestiona la lógica de la aplicación, el procesamiento de los datos seriales y las interacciones del usuario a través de la interfaz gráfica[cite: 1].
* [cite_start]**`PanelControl.java`**: Panel de la interfaz de usuario que contiene los botones para controlar el monitoreo y las funciones de exportación[cite: 4].
* [cite_start]**`PanelDatos.java`**: Panel que muestra los valores numéricos de temperatura y humedad en tiempo real y el estado de la conexión[cite: 5].
* [cite_start]**`PanelGrafico.java`**: Panel que dibuja el gráfico de líneas con los datos históricos de temperatura y humedad[cite: 7].
* [cite_start]**`PanelHumedad.java`**: Componente de la interfaz de usuario que muestra específicamente los datos de humedad[cite: 8].
* [cite_start]**`PanelLog.java`**: Panel dedicado a la visualización de los mensajes de registro y errores[cite: 9].

---

### 🚀 Instrucciones de Uso

1.  **Configuración del Arduino**:
    * Instala la librería `DHT sensor library` en el Arduino IDE.
    * Abre el archivo `SensorHumedadTemeraturaArduino.ino` en el Arduino IDE.
    * Conecta el circuito y el Arduino a tu computadora.
    * Sube el código a la placa.
2.  **Ejecución de la Aplicación Java**:
    * Abre el proyecto en tu IDE de Java (ej. NetBeans).
    * Verifica que el puerto serial en el código `Controlador.java` (`private final String PUERTO_ARDUINO = "COM5";`) coincida con el puerto al que está conectado tu Arduino.
    * Ejecuta la clase `Main.java` para iniciar la aplicación.
3.  **Monitoreo y Control**:
    * Una vez iniciada la aplicación, se conectará automáticamente al Arduino y comenzará a recibir datos.
    * Utiliza los botones de la interfaz para iniciar, detener, exportar datos o controlar el LED del Arduino.
