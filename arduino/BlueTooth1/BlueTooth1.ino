#include <SoftwareSerial.h>

SoftwareSerial BT(8,9);
char x;
int number;
int key = 1;
int drugCode[1024]; 
const int ledPin1 = 2;
const int ledPin2 = 3;
const int ledPin3 = 4;
const int ledPin4 = 5;
int i=0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("蓝牙连接正常");
  BT.begin(9600);
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ledPin4, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(BT.available()){
    if(key == 1){
        x = BT.read();
        Serial.print(x);
       Serial.print(key);
        Serial.print("\n");
        key=2;
        if (x == '0'){
       BT.print("OK");
       key=1;
       }
    }else if(key == 2){
      number = BT.read() - '0';
      Serial.print(number);
      Serial.print(key);
      Serial.print("\n");
      if (x == '1') {
      digitalWrite(ledPin1, HIGH); // 点亮1号LED
      delay(3000*number); // 延时5秒
      digitalWrite(ledPin1, LOW); // 熄灭1号LED
    } else if (x == '2') {
      digitalWrite(ledPin2, HIGH); // 点亮2号LED
      delay(3000*number); // 延时5秒
      digitalWrite(ledPin2, LOW); // 熄灭2号LED
    } else if (x == '3') {
      digitalWrite(ledPin3, HIGH); // 点亮3号LED
      delay(3000*number); // 延时5秒
      digitalWrite(ledPin3, LOW); // 熄灭3号LED
    } else if (x == '4') {
      digitalWrite(ledPin4, HIGH); // 点亮4号LED
      delay(3000*number); // 延时5秒
      digitalWrite(ledPin4, LOW); // 熄灭4号LED
     }
     key=1;
    }
  }
}

