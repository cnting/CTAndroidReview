package com.cnting.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * Created by cnting on 2022/11/15
 */
public class HandlerActivity extends AppCompatActivity {

    Button btn1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn1.setText("关闭");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new MyHandler(this).sendEmptyMessageDelayed(0, 20000);
    }

    void changeBtn() {
        btn1.setText("222222");
    }

    static class MyHandler extends Handler {
        WeakReference<HandlerActivity> activity;

        MyHandler(HandlerActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HandlerActivity a = activity.get();
            if (a != null) {
                a.changeBtn();
            }
        }
    }
}
