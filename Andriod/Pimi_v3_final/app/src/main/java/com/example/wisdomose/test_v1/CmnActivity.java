package com.example.wisdomose.test_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * 이 액티비티 클래스는 다음을 담당한다.
 * 1. MainActivity로 부터 날라온 host Address를 이용하여 '주소확인'만 담당한다.
 * 실질적인 연결은 ControllerActivity가 작동되면서 한다.
 */

public class CmnActivity extends Activity{
    String HostAddr;
    String PortAddr;
    String FinalAddr;
    TextView Addr;

    /* 3초후 지연처리를 위한 Handler */
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmn);
         mHandler = new Handler();

        /* Intent값 받아올 intent 변수 from MainActivity */
        Intent intent  = getIntent();

        /* HostAddr에 해당하는 값을 받아옴 */
        HostAddr = intent.getStringExtra("HostAddr");
        /* PostAddr에 해당하는 값을 받아옴 */
        PortAddr = intent.getStringExtra("PortAddr");

        FinalAddr = HostAddr + ":" + PortAddr;

        Addr = (TextView)findViewById(R.id.Addr);
        /* TextView setting */
        Addr.setText(FinalAddr);

        mHandler.postDelayed(goNext,3000);

    }

    private Runnable goNext = new Runnable() {
        @Override
        public void run() {
            /* ControllerActivity로 가기위한 변수 */
            Intent go_activity_controller = new Intent(CmnActivity.this,ControllerActivity.class);
            go_activity_controller.putExtra("HostAddr",HostAddr);
            go_activity_controller.putExtra("PortAddr",PortAddr);
            startActivity(go_activity_controller);
            finish();
        }
    };

}
