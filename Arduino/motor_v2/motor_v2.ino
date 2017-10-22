//pin 7,6,5,4 activation
//motor1
const int IN1 = 7; 
const int IN2 = 6;

//motor2
const int IN3 = 5;
const int IN4 = 4;

void setup()
{
 //open port number 9600
 Serial.begin(9600);
 //setting pinMode from using  
 pinMode(IN1,OUTPUT);
 pinMode(IN2,OUTPUT);
 pinMode(IN3,OUTPUT);
 pinMode(IN4,OUTPUT);
}

void loop()
{
   //if it can communicate by Serial
   if(Serial.available())
   {
    String value = Serial.readString();

    //classify into value
    if(value == "1") //Go straight
    {
     //just msg
     Serial.println("Go up"); 
     //real code for moving
     digitalWrite(IN1,HIGH);
     digitalWrite(IN3,HIGH);
     digitalWrite(IN2,LOW);
     digitalWrite(IN4,LOW);       
    }
    if(value == "4") //Go back
    {
     //just msg
     Serial.println("Go down"); 
     //real code for moving
     digitalWrite(IN1,LOW);
     digitalWrite(IN3,LOW);
     digitalWrite(IN2,HIGH);
     digitalWrite(IN4,HIGH);
    }
    if(value == "3") //Go Right
    {
     //just msg
     Serial.println("Go left");  
     //real code for moving
      digitalWrite(IN1, LOW);
      digitalWrite(IN2, LOW);
      digitalWrite(IN3, HIGH);
      digitalWrite(IN4, LOW);
    }
    if(value == "2") //Go Left
    {
     //just msg
     Serial.println("Go right"); 
     //real code for moving
      digitalWrite(IN1, HIGH);
      digitalWrite(IN2, LOW);
      digitalWrite(IN3, LOW);
      digitalWrite(IN4, LOW);
    }
    if(value == "0") // Stop
    {
     //just msg
     Serial.println("Go stop");
     //real code for moving
     digitalWrite(IN1, LOW);
     digitalWrite(IN2, LOW);
     digitalWrite(IN3, LOW);
     digitalWrite(IN4, LOW);
    }
   }
   delay(10); 
}


