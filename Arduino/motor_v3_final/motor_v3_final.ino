
int A_1A = 6;
int A_1B = 11;
int B_1A = 3;//left
int B_1B = 5;//left
int speed = 255;

void setup()
{
 //open port number 9600
 Serial.begin(9600);
 //setting pinMode from using  
  pinMode(A_1A, OUTPUT);
  pinMode(A_1B, OUTPUT);
  pinMode(B_1A, OUTPUT);
  pinMode(B_1B, OUTPUT);
  digitalWrite(A_1A, LOW);
  digitalWrite(A_1B, LOW);
  digitalWrite(B_1A, LOW);
  digitalWrite(B_1B, LOW);
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
     //모터A 정회전
      analogWrite(A_1A, speed-30);
      analogWrite(A_1B, 0);
      //모터B 정회전
      analogWrite(B_1A, speed-20);
      analogWrite(B_1B, 0);
      delay(500);
      analogWrite(A_1A, speed-20);
      
    }
    if(value == "4") //Go back
    {
     //just msg
     Serial.println("Go down"); 
     //real code for moving
       //모터A 역회전
      analogWrite(A_1A, 0);
      analogWrite(A_1B, speed-20);
      //모터B 역회전
      analogWrite(B_1A, 0);
      analogWrite(B_1B, speed);
    }
    if(value == "3") //Go Right
    {
     //just msg
     Serial.println("Go right");  
      //real code for moving
      analogWrite(A_1A, 0);
      analogWrite(A_1B, 0);
      //모터B 역회전
      analogWrite(B_1A, speed-100);
      analogWrite(B_1B, 0);
    }
    if(value == "2") //Go Left
    {
     //just msg
     Serial.println("Go left"); 
     //real code for moving
     //모터A 역회전
      analogWrite(A_1A, speed-100);
      analogWrite(A_1B, 0);
      //모터B 정회전
      analogWrite(B_1A, 0);
      analogWrite(B_1B, 0);
    }
    if(value == "0") // Stop
    {
     //just msg
     Serial.println("Go stop");
     //real code for moving
    //모터A 정지
      analogWrite(A_1A, 0);
      analogWrite(A_1B, 0);
      //모터B 정지
      analogWrite(B_1A, 0);
      analogWrite(B_1B, 0);
    }
   }
}


