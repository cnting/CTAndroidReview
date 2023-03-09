package com.cnting.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

/**
 * Created by cnting on 2023/3/8
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean isValid;
    private Handler handler;
    private Paint paint;

    public MySurfaceView(Context context) {
        super(context);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isValid = true;
        //开启子线程绘制
        startDrawThread();
    }

    private void startDrawThread() {
        HandlerThread handlerThread = new HandlerThread("SurfaceViewThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isValid = false;
    }

    @Override
    public void run() {
        while (isValid) {
            draw();
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            //canvas是之前使用过的canvas，不是一个新对象，之前的绘图操作都被保留，可以选择是否擦除
//            clearCanvas(canvas);

            doDraw(canvas);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                //保证每次都将绘图的内容提交
                holder.unlockCanvasAndPost(canvas);
            }
        }
        handler.postDelayed(this, 1000);
    }

    private int x;
    private final Path path = new Path();

    private void doDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);
        x += 1;
        int y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);
        path.lineTo(x, y);
        canvas.drawPath(path, paint);
    }

    private void clearCanvas(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }
}
