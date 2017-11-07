#python3

import socket
import sys

#Server ip
HOST = '172.30.1.12'
#Server port
PORT = 8888

#1. open Socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Soekt created')

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
    #5. send data
    data = conn.recv(1024)
    if not data:
        break
    #but I dont need receive function. so i tagged #
    #conn.send(data)
    print(data)
    if data == 'exit':
        print('TTTTTHE end')
        conn.close()
        s.close()
        break

conn.close()
s.close()
print 'FFFFFFINISH'


