// 定义LED引脚
const int ledPin = 5;
void setup() {
  // 初始化LED引脚为输出
  pinMode(ledPin, OUTPUT);
}

void loop() {
  // 读取按钮状态
  digitalWrite(ledPin,HIGH);
  delay(3000);
  digitalWrite(ledPin,LOW);
  delay(3000);
}
