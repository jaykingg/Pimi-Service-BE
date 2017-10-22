package com.example.wisdomose.test_v1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.os.StrictMode.setThreadPolicy;

/**
 * !!!!!! 연결됐으면 됐다고, 실패했으면 실패했다고 띄워줘야함!!!!!!!!!!!!!!
 * Toast쓰면 쓰레드 중지된다.★★★
 * 이 액티비티 클래스는 다음을 담당한다.
 * 0. 생성과 동시에 서버와 연결, 연결실패시 MainActivity로 다시 이동
 * 1. Moving에 관한 Control
 * 2. Camera에 관한 output
 */

public class ControllerActivity extends Activity{
    private String HostAddr;
    private String PortAddr;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Button UpBt = (Button) findViewById(R.id.UpBt);
        Button LeftBt = (Button) findViewById(R.id.LeftBt);
        Button RightBt = (Button) findViewById(R.id.RightBt);
        Button DownBt = (Button) findViewById(R.id.DownBt);
        WebView Streaming = (WebView) findViewById(R.id.Streaming);

        //Streaming 관련 WebView 부분
        Streaming.setWebViewClient(new WebViewClient());
        Streaming.setBackgroundColor(255);
        //영상을 폭에 꽉 차게 할려고 했지만 먹히지 않음???
        Streaming.getSettings().setLoadWithOverviewMode(true);
        Streaming.getSettings().setUseWideViewPort(true);
        //이건 최신 버전에서는 사용하지 않게됨
        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        WebSettings webSettings = Streaming.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //영상을 폭을 꽉 차게 하기 위해 직접 html태그로 작성함.
        //이곳이 라즈베리파이 서버의 아이피/포트 부분 들어간곳
        //Streaming.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} div{overflow: hidden;} </style></head><body><div><img src='http://192.168.0.21:8080/stream/video.mjpeg'/></div></body></html>" ,"text/html",  "UTF-8");
        Streaming.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} div{overflow: hidden;} </style></head><body><div><img src='http://117.17.142.139:6900/stream/video.mjpeg'/></div></body></html>" ,"text/html",  "UTF-8");
        Streaming.loadUrl("http://117.17.142.139:6900/stream/video.mjpeg");


        /* Intent를 통해 데이터를 전달받기 위한 변수 */
        Intent intent  = getIntent();
        HostAddr = intent.getStringExtra("HostAddr");
        PortAddr = intent.getStringExtra("PortAddr");

         /* 안드로이드 OS 최신 버전은 Main Thread 내 Socket 사용 못함
         * Socket 통신 부분을 별도의 Thread로 뺴지 않을 때 아래 코드를 첨부함
         * But AVD에서는 정상작동하지만 Phoneㄴ에서는 죽는다는 말이 있음 -> 확인 요망 */

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);

        //Button 대한 onTouchLisener 등록
        View.OnTouchListener tclistener = new View.OnTouchListener()
        {
            /* 이동메시지변수 */
            String Go  = "Go";
            String Left = "Left";
            String Right = "Right";
            String Back= "Back";
            String Stop = "Stop";
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(v.getId())
                {
                    //전진
                    case R.id.UpBt:
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                //터치하고 있는 상태, 전진 값을 전송
                                sendServer(Go);
                                Log.i("TouchMoving","Touch리스너로 전진값 전송(누르는중)");
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                //터치를 뗀 상태, 멈춤 값을 전송
                                sendServer(Stop);
                                Log.i("TouchMoving","Touch리스너로 멈춤값 전송(키뗌)");
                                break;
                            default:
                                break;
                        }
                        break;
                    //좌회전
                    case R.id.LeftBt:
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                //터치하고 있는 상태, 좌회전 값을 전송
                                sendServer(Left);
                                Log.i("TouchMoving","Touch리스너로 좌회전값 전송(누르는중)");
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                //터치를 뗀 상태, 멈춤 값을 전송
                                sendServer(Stop);
                                Log.i("TouchMoving","Touch리스너로 멈춤값 전송(키뗌)");
                                break;
                            default:
                                break;
                        }
                        break;
                    //우회전
                    case R.id.RightBt:
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                //터치하고 있는 상태, 우회전 값을 전송
                                sendServer(Right);
                                Log.i("TouchMoving","Touch리스너로 우회전값 전송(누르는중)");
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                //터치를 뗀 상태, 멈춤 값을 전송
                                sendServer(Stop);
                                Log.i("TouchMoving","Touch리스너로 멈춤값 전송(키뗌)");
                                break;
                            default:
                                break;
                        }
                        break;
                    //후진
                    case R.id.DownBt:
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                //터치하고 있는 상태, 후진 값을 전송
                                sendServer(Back);
                                Log.i("TouchMoving","Touch리스너로 후진값 전송(누르는중)");
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                //터치를 뗀 상태, 멈춤 값을 전송
                                sendServer(Stop);
                                Log.i("TouchMoving","Touch리스너로 멈춤값 전송(키뗌)");
                                break;
                            default:
                                break;
                        }
                        break;
                }

                //OnTouchListener 후에도 다른 Listener들이 동작하게 함.
                return false;
            }

        };

        //리스너 등록
        UpBt.setOnTouchListener(tclistener);
        LeftBt.setOnTouchListener(tclistener);
        RightBt.setOnTouchListener(tclistener);
        DownBt.setOnTouchListener(tclistener);

        /* onCreate 안에다가 Thread X */
        /* !!!이거 연결버튼에다가 Mapping시켜야할듯!!! 앱시작하면 연결하니까 에러사항많음
         * 즉 연결 버튼누르면 SocketThread 실행되게끔해야함  */
        SocketThconn = new SocketThread();

        /* setDaemon(true) -> Main Thread가 죽으면 Serv Thread를 죽여 동기화시킴 */
        SocketThconn.setDaemon(true);
        SocketThconn.start();

    }
    /* 이거 앱종료 버튼에 추가할 것
    * !!!!다만 이거 실행되도 서버는 살아있게끔 코딩 다시 할 것 */
    private void DisconnectAlert()
    {
        /* alert알림창 띄우기 위함 */
        AlertDialog.Builder alert = new AlertDialog.Builder(ControllerActivity.this);

        /* alert알림창의 '확인'버튼에 대한 Listener */
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //socket.close 를 통해서 연결이 끊어진다 -> 서버도 닫힌다. 서버는 특정상황이지 않는이상 종료되지 않게 해야한다.
                String Exit = "exit";
                sendServer(Exit);
                if(socket.isConnected()) {
                    try {
                        socket.close();

                    }catch(IOException ioe)
                    {

                    }
                }
                /* 현재 실행 중인 App을 Background로 전환시킴
                *  즉 finish()하면 전에 있던 Activity가 뜨는데 moveTasktoBack은 그걸 막으면서 홈화면으로 전환함 */
                //moveTaskToBack(true);
                finish();

                /* 현재프로세스 죽임, Task상에서 없애버림 */
                //android.os.Process.killProcess(android.os.Process.myPid());

            }

        });
        alert.setMessage("초기화면으로 돌아갑니다.");
        alert.show();
    }

    /* BufferWriter에 Msg쓰기 */
    private void sendServer(String msg)
    {
        sockPrintWriter.println(msg);
    }

    /* 규정상 onCreate안에 만들지 못했던 Thread 내 Socket 선언 함수 */
    class SocketThread extends Thread{
        public void run(){

            /* 라즈베리파이 IP가 들어가야함 */
            String ipaddr = HostAddr;

            /* 라즈베리파이 서버와 포트통일 */
            int ipport = Integer.parseInt(PortAddr);

            try
            {
                /* 소켓 열고 */
                socket = new Socket(ipaddr, ipport);

                /* !!!연결 비연결 확인
                 *  뒤페이지로 왔다갔다하면 연결 다시 안됨 !! -> 위에서 언급한것처럼 버튼 누를 때마다 연결 혹은
                 *  뒤페이지 이동버튼 누르면 연결 끊어지ㄱㅔ해야함 */
/*                if(socket != null && socket.isConnected())
                {
                    Toast.makeText(getApplicationContext(), "서버 연결 성공", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                }*/

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

    public void DisConnectBt(View view) {
        DisconnectAlert();
    }
}
