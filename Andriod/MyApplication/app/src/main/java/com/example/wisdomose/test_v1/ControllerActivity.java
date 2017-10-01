package com.example.wisdomose.test_v1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by wisdomose on 2017. 9. 30..
 */

public class ControllerActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Button UpBt = (Button) findViewById(R.id.UpBt);
        Button LeftBt = (Button) findViewById(R.id.LeftBt);
        Button RightBt = (Button) findViewById(R.id.RightBt);
        Button DownBt = (Button) findViewById(R.id.DownBt);

        //하나의 페이지에 처리해야할 리스너가 1개라면 한번에 해도된다.
        //하지만 처리해야할 버튼이 여러개인 경우 리스너를 중간변수로하여 각 버튼에 등록해주어야한다.
        //즉 리스너 생성은 한번만! 하라는 것.
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //추가적으로 생각한것이 버튼을 누름과 동시에 방향 값 전달하고
                //(전달하면 계속 바퀴는 해당명령을 수행하면서 굴러간다.)
                //버튼을 뗌과 동시에 '멈춤' 명령을 주어서 따로 멈추게하지않아도 되게끔해야겠다.
                // --> onClick리스너 말고, onTouch리스너 사용해야할듯.
                switch (v.getId())
                {
                    /* Touch 작동을 확인하기 위해 주석처리함
                    case R.id.UpBt:
                        //전진 방향 누를 때
                        Log.i("moving","전진");
                        break;
                        */
                    case R.id.LeftBt:
                        //좌측 방향 누를 때
                        Log.i("moving","좌회전");
                        break;
                    case R.id.RightBt:
                        //우측 방향 누를 때
                        Log.i("moving","우회전");
                        break;
                    case R.id.DownBt:
                        //후진 방향 누를 때
                        Log.i("moving","후진");
                        break;
                }
            }

        };

        //리스너 등록
        //UpBt.setOnClickListener(listener);
        LeftBt.setOnClickListener(listener);
        RightBt.setOnClickListener(listener);
        DownBt.setOnClickListener(listener);

        //UpBt에 대한 onTouchLisener 등록
        //UpBt에서 성공적으로 실행되면 모든 방향에 onTouchListener 등록
        View.OnTouchListener tclistener = new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        //터치하고 있는 상태, 전진 값을 전송
                        Log.i("TouchMoving","Touch리스너로 전진값 전송(누르는중)");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //터치를 뗀 상태, 멈춤 값을 전송
                        Log.i("TouchMoving","Touch리스너로 멈춤값 전송(키뗌)");
                        break;


                    default:
                        break;

                }
                //OnTouchListener 후에도 다른 Listener들이 동작하게 함.
                return false;
            }

        };

        //리스너 등록
        UpBt.setOnTouchListener(tclistener);


    }

    public void DisConnectBt(View view) {
        finish();
    }
}
