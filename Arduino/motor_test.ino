const int IN1 = 7;
const int IN2 = 6;

const int IN3 = 5;
const int IN4 = 4;

void setup()
{
  pinMode(IN1,OUTPUT);
  pinMode(IN2,OUTPUT);
  pinMode(IN3,OUTPUT);
  pinMode(IN4,OUTPUT);
}

void loop()
{
  bothMotorStart();
  delay(1000);
}

void bothMotorStart()
{
 digitalWrite(IN1,HIGH);
 digitalWrite(IN2,LOW);
 digitalWrite(IN3,HIGH);
 digitalWrite(IN4,LOW); 
}
