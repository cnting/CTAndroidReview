package com.cnting.handler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(MainActivity.this, "toast", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ddd");
                        alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.hide();
                    }
                });
                Looper.loop();
            }
        }).start();



        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
//                throw new RuntimeException("主线程异常");
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        throw new NumberFormatException("子线程异常");
                    }
                }).start();
            }
        });

        btn1.post()

//        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewPropertyAnimator animator = v.animate().scaleY(2).setDuration(2000);
//                animator.start();
//                v.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);  //让主线程卡顿
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 1000);
//            }
//        });
//        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewPropertyAnimator animator = v.animate().scaleY(2).setDuration(2000);
//                setViewPropertyAnimatorRT(animator, createViewPropertyAnimatorRT(v));
//                animator.start();
//                v.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);  //让主线程卡顿
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 1000);
//            }
//        });
//
//        Handler handler = new Handler(Looper.getMainLooper());

    }

    private static Object createViewPropertyAnimatorRT(View view) {
        try {
            final Class<?> animRtCalzz = Class.forName("android.view.ViewPropertyAnimatorRT");
            final Constructor<?> animRtConstructor = animRtCalzz.getDeclaredConstructor(View.class);
            animRtConstructor.setAccessible(true);
            return animRtConstructor.newInstance(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setViewPropertyAnimatorRT(ViewPropertyAnimator animator, Object rt) {
        try {
            final Class<?> animClazz = Class.forName("android.view.ViewPropertyAnimator");
            final Field animRtField = animClazz.getDeclaredField("mRTBackend");
            animRtField.setAccessible(true);
            animRtField.set(animator, rt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void requestAQuestion() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();  //注意这里
                Toast.makeText(MainActivity.this, "我是Toast", Toast.LENGTH_LONG).show();
                Looper.loop();     //注意这里
            }
        }.start();
    }

    private void showQuestionInDialog(String title) {
        QuestionDialog dialog = new QuestionDialog(this);
        dialog.show(title);
    }

    class QuestionDialog extends Dialog {
        private TextView dialogTitle;
        private Button yesBtn, noBtn;

        QuestionDialog(Context context) {
            super(context);

            setContentView(R.layout.dialog_show_question);
            dialogTitle = findViewById(R.id.titleTv);
            yesBtn = findViewById(R.id.btnYes);
            noBtn = findViewById(R.id.btnNo);

            final Handler uiHandler = new Handler(Looper.getMainLooper());

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialogTitle.setText(dialogTitle.getText() + "?");
                        }
                    });
                }
            });
        }

        public void show(String title) {
            dialogTitle.setText(title);
            show();
        }
    }
}
