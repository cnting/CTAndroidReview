package com.example.aspectjdemo.aspectJ;

import android.util.Log;
import android.widget.TextView;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by cnting on 2022/7/29
 */
@Aspect
public class TextViewAspectJ {
    // "textView"必须和下面参数名称一样
    @AfterReturning(pointcut = "execution(* *..*.testAfterReturning())",returning = "textView")
    public void getTextView(TextView textView){
        Log.d("===>","textView:"+textView.getText().toString());
    }
}
