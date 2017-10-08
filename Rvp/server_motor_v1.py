#python3

import socket
import serial #For using Serial
import sys
from _io import StringIO

ser = serial.Serial("/dev/ttyACM0",9600) #Arduino's root, port

print ser.portstr #check serial port nomally opened

#Server ip
HOST = '192.168.0.15'
#Server port
PORT = 8888

#1. open Socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')

#2. bind Ip with Port, do try/catch process
try:
    s.bind((HOST,PORT))
except socket.error as msg:
    # if bind is failed, exit from this program
    print('Bind Failed. Error code: ' + str(msg[0]) + 'Message: ' + msg[1])
    sys.exit()

print ('Socket bind complete')

#3. Listening status, waiting for connection. in Listen(a), a is a number of Waiting for connection
s.listen(10)
print('Socket now Listening')

#4. if it success connection, print addr
conn, addr = s.accept()
print ('Connected with ' + addr[0] + ':' + str(addr[1]))

while 1:
    data = str(conn.recv(1024))
    if data.startswith("G"):
        ser.write("1")
        print 'rev Go'
    if data.startswith("L"):
        ser.write("2")
        print 'rev Left'
    if data.startswith("R"):
        ser.write("3")
        print 'rev Right'
    if data.startswith("B"):
        ser.write("4")
        print 'rev Back'
    if data.startswith("S"):
        ser.write("0")
        print 'rev Stop'
    if not data:
        break
    #but I dont need receive function. so i tagged #
    #conn.send(data)
    print(data)
    if data.startswith("e"):
       print('rev Exit')
       conn.close()
       s.close()
       break

conn.close()
s.close()
ser.close()
print 'Server is closed'






