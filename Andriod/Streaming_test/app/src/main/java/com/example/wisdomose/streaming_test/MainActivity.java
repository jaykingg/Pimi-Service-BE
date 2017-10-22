package com.example.wisdomose.streaming_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView Streaming = (WebView)findViewById(R.id.Streaming);
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
        //Streaming.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} div{overflow: hidden;} </style></head><body><div><img src='http://192.168.0.21:8080/stream/video.mjpeg'/></div></body></html>" ,"text/html",  "UTF-8");
        Streaming.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} div{overflow: hidden;} </style></head><body><div><img src='http://117.17.142.139:6900/stream/video.mjpeg'/></div></body></html>" ,"text/html",  "UTF-8");
        Streaming.loadUrl("http://117.17.142.139:6900/stream/video.mjpeg");



    }
}
