#include <DHT.h>

#define DHTPIN 2
#define DHTTYPE DHT11
#define LED_PIN 13 

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  pinMode(LED_PIN, OUTPUT);
  dht.begin();
  while (!Serial); 
}

void loop() {
  // Leer comandos desde Java
  if (Serial.available() > 0) {
    String command = Serial.readStringUntil('\n');
    command.trim();
    
    if (command == "ON") {
      digitalWrite(LED_PIN, HIGH);
    } else if (command == "OFF") {
      digitalWrite(LED_PIN, LOW);
    }
  }

  // Enviar datos del sensor (como antes)
  delay(2000);
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  if (isnan(h) || isnan(t)) {
    Serial.println("ERROR"); 
    return;
  }

  String data = "TEMP:" + String(t, 1) + ",HUM:" + String(h, 1) + ";";
  Serial.println(data); 
}