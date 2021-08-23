package com.cnting.com.activity;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by cnting on 2021/6/8
 */
class MyViewPager extends ViewPager {
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int lastX = -1;
    int lastY = -1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("===>", "child.dispatchTouchEvent():" + MotionEvent.actionToString(ev.getActionMasked()));
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                Log.i("===>", "dealtX:" + dealtX + ",dealtY:" + dealtY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                getParent().requestDisallowInterceptTouchEvent(dealtX >= dealtY);
                lastX = x;
                lastY = y;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
