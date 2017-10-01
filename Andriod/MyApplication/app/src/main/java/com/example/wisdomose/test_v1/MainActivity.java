package com.example.wisdomose.test_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //Lisenter 등록부분(Connect btn)
        Button ConnectBt  = (Button) findViewById(R.id.ConnectBt);
        ConnectBt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            //여기에 눌렀을 때 메소드 입력
            //activity_Controller로 이동하기위한 Intent 사용
            //Intent 변수명 = new Intent(this(해당 액티비티,가고자하는 액티비티.class);
            Intent go_activity_controller  = new Intent(this,ControllerActivity.class);
            startActivity(go_activity_controller);
        }
        });
        */

    }

    public void ConnectBt(View view) {
        //여기에 눌렀을 때 메소드 입력
        //이 자리에 서버에서 연결확인하는 작업 필요하다.(포트 등등...)
        //activity_Controller로 이동하기위한 Intent 사용
        //Intent 변수명 = new Intent(this(해당 액티비티,가고자하는 액티비티.class);
        Intent go_activity_controller  = new Intent(this,ControllerActivity.class);
        startActivity(go_activity_controller);

    }
}
