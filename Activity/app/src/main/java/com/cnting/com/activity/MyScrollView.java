package com.cnting.com.activity;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;

/**
 * Created by cnting on 2021/6/7
 */
class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float lastX = 0;
    float lastY = 0;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        float x = ev.getX();
//        float y = ev.getY();
//        int action = ev.getActionMasked();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                lastX = x;
//                lastY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float deltaX = Math.abs(x - lastX);
//                float deltaY = Math.abs(y - lastY);
//                if (deltaX > deltaY) {
//                    return false;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }


//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        super.onInterceptTouchEvent(ev);
//        Log.d("===>", "parent.onInterceptTouchEvent():" + MotionEvent.actionToString(ev.getActionMasked()));
//        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            return false;
//        } else {
//            return true;
//        }
//    }
}
