package com.example.wisdomose.test_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String HostAddr = null;
    String PortAddr = null;
    EditText EtHostAddr;
    EditText EtPortAddr;
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
            Intent go_activity_controller  = new Intent(this, ControllerActivity.class);
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

        //EditText ID 받아옴
        EtHostAddr = (EditText)findViewById(R.id.EtHostAddr);
        EtPortAddr = (EditText)findViewById(R.id.EtPortAddr);

        //각 EditText의 값을 저장
        HostAddr = EtHostAddr.getText().toString();
        PortAddr = EtPortAddr.getText().toString();

        if(HostAddr.length() >= 6 && PortAddr.length() == 4)
        {
            Intent go_activity_cmn  = new Intent(this,CmnActivity.class);
            //Intent에 HostAddr/PortAddr 이란 이름이도 데이터를 담음
            go_activity_cmn.putExtra("HostAddr",HostAddr);
            go_activity_cmn.putExtra("PortAddr",PortAddr);
            startActivity(go_activity_cmn);
        }
        else
        {
            //둘 중 하나라도 null이 있으면 Toast띄움
            Toast.makeText(getApplicationContext(), "주소값을 확인하세요.", Toast.LENGTH_LONG).show();
        }




    }

    public void CloseBt(View view) {
        /* 현재프로세스 죽임, Task상에서 없애버림 */
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
