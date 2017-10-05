/*
*
*
* 17.10.05 수정 Ctrl + F 해서 !!! 있는부분 생각하여 수정
*
*
*
* */
package com.example.wisdomose.test_cmn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.os.StrictMode.*;

public class MainActivity extends AppCompatActivity {
    /* 소켓변수 */
    private Socket socket = null;

    /* 버퍼를 이용한 문자 입출력 스트림 */
    /* 왜 버퍼를 쓰느냐, 장치에 바로 바로 쓰는 것보다 일정크기만큼 모아놓았다가 쓰는것이 더 빠르기 때문 */
    private BufferedReader sockReader;
    private BufferedWriter sockWriter;

    /* 객체타입의 입출력 스트림 */
    /* 객체+버퍼의 장점들로 코딩하기 위해서 wrapping 방식으로 코딩함 */
    private PrintWriter sockPrintWriter;

    /* 하단 SocketThread 변수(onCreate에 Thread 생성안되니까) */
    SocketThread SocketThconn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 안드로이드 OS 최신 버전은 Main Thread 내 Socket 사용 못함
         * Socket 통신 부분을 별도의 Thread로 뺴지 않을 때 아래 코드를 첨부함
         * But AVD에서는 정상작동하지만 Phoneㄴ에서는 죽는다는 말이 있음 -> 확인 요망 */

        ThreadPolicy policy = new ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);

        /* 전송 버튼 */
        Button SendBt =  (Button)findViewById(R.id.Send);

        /* MSG text */
        final EditText Msg =  (EditText) findViewById(R.id.Msg);

        /* 전송 버튼 Listener */
        SendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //담고
                String sendMsg = Msg.getText().toString();
                //exit이면 앱종료 -> 종료 버튼으로 뺴기
                if(sendMsg.equals("exit"))
                {
                    sendServer(sendMsg);
                    SendMsgAlert();
                }
                //메세지 전송 / Toast 출력
                else
                {
                    sendServer(sendMsg);
                    Toast.makeText(getApplicationContext(), sendMsg + " 전송", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* onCreate 안에다가 Thread X */
        /* !!!이거 연결버튼에다가 Mapping시켜야할듯!!! 앱시작하면 연결하니까 에러사항많음
         * 즉 연결 버튼누르면 SocketThread 실행되게끔해야함  */
        SocketThconn = new SocketThread();

        /* setDaemon(true) -> Main Thread가 죽으면 Serv Thread를 죽여 동기화시킴 */
        SocketThconn.setDaemon(true);
        SocketThconn.start();

        /* 연결 비연결 확인
        *  뒤페이지로 왔다갔다하면 연결 다시 안됨 !! -> 위에서 언급한것처럼 버튼 누를 때마다 연결 혹은
        *  뒤페이지 이동버튼 누르면 연결 끊어지ㄱㅔ해야함 */
        if(socket != null && socket.isConnected())
        {
            Toast.makeText(getApplicationContext(), "서버 연결 성공", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
        }
    }

    /* BufferWriter에 Msg쓰기 */
    private void sendServer(String msg)
    {
        sockPrintWriter.println(msg);
    }

    /* 이거 앱종료 버튼에 추가할 것
     * !!!!다만 이거 실행되도 서버는 살아있게끔 코딩 다시 할 것 */
    private void SendMsgAlert()
    {
        /* alert알림창 띄우기 위함 */
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        /* alert알림창의 '확인'버튼에 대한 Listener */
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //socket.close 를 통해서 연결이 끊어진다 -> 서버도 닫힌다. 서버는 특정상황이지 않는이상 종료되지 않게 해야한다.
                if(socket.isConnected()) {
                    try {
                        socket.close();
                    }catch(IOException ioe)
                    {

                    }
                }
                /* 현재 실행 중인 App을 Background로 전환시킴
                *  즉 finish()하면 전에 있던 Activity가 뜨는데 그걸 막으면서 홈화면으로 전환함 */
                moveTaskToBack(true);
                finish();

                /* 현재프로세스 죽임, Task상에서 없애버림 */
                android.os.Process.killProcess(android.os.Process.myPid());

            }

        });
        alert.setMessage("앱을 종료합니다.");
        alert.show();
    }

    /* 규정상 onCreate안에 만들지 못했던 Thread 내 Socket 선언 함수 */
    class SocketThread extends Thread{
        public void run(){

            /* 라즈베리파이 IP가 들어가야함 */
            String ipaddr = "172.30.1.12";

            /* 라즈베리파이 서버와 포트통일 */
            int ipport = 8888;

            try
            {
                /* 소켓 열고 */
                socket = new Socket(ipaddr, ipport);

                //한글 깨질시 getInputStream 다음에 "EUC-KR" 넣어줌
                /* BufferWriter/Reader안에 넣는건 고정임(소켓통신간)*/
                sockWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                sockReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                /* Buffer에 쓴걸 객체로 담아쓰기위해 Wrapping함 */
                sockPrintWriter = new PrintWriter(sockWriter, true);
            }
            catch (UnknownHostException ue)
            {
                System.out.println(ue);
                ue.printStackTrace();
            } catch (IOException ie) {
                System.out.println(ie);
                ie.printStackTrace();
            }
        }
    }
}


