package com.example.aspectjdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aspectjdemo.track.TrackEvent;
import com.example.aspectjdemo.track.TrackParameter;

/**
 * Created by cnting on 2022/7/29
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //这里还不能用语法糖
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("===>", "点击了");
            }
        });
//        testAfterReturning();
//        testAfterThrowing();
//        testAround();
//        testWithInCode();
//        test();
        trackMethod(100,"ct");
    }

    private TextView testAfterReturning() {
        return findViewById(R.id.tv);
    }

    private void testAfterThrowing() {
        TextView textView = null;
        textView.setText("aaa");
    }

    private void testAround() {
        TextView textView = null;
        textView.setText("aaa");
    }

    private void testWithInCode() {
        Person person = new Person();
        person.setAge(20);
    }

    private void test() {
        testAfterReturning();
        testAround();
        testWithInCode();
    }

    @TrackEvent(eventName = "点击按钮",eventId = "100")
    private void trackMethod(@TrackParameter("uid")int uid,String name){
        Log.d("===>","埋点");
    }
}
