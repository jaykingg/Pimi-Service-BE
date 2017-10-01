void setup()
{
 Serial.begin(9600); 
}

void loop()
{
   if(Serial.available())
   {
    String value = Serial.readString();
    if(value == "1")
    {
     Serial.println("Go up"); 
    }
    if(value == "2")
    {
     Serial.println("Go down"); 
    }
    if(value == "3")
    {
     Serial.println("Go left"); 
    }
    if(value == "4")
    {
     Serial.println("Go right"); 
    }
   }
   delay(10); 
}


