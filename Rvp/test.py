import serial #For using Serial
import sys

ser = serial.Serial("/dev/ttyACM0",9600) #Arduino's root, port

print ser.portstr #check serial port nomally opened

operation = True

while operation:
	print("Moving : 1,2,3,4 / Exit : 5")
	value = raw_input("input your data-> ")
	if value == "5":
		operation = False
		ser.close()
	else:
		ser.write(value)

print("Operation is stopped")
