package com.cnting.ipc.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnting.ipc.R;

import java.util.List;

/**
 * Created by cnting on 2021/1/18
 */
public class BookManagerActivity extends AppCompatActivity {
    final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager bookManager;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> books = bookManager.getBookList();
                Log.d("===>", "onServiceConnected():" + books.toString() + ",当前线程:" + Thread.currentThread().getName());
                Book book = new Book(3, "Android开发艺术探索");
                bookManager.addBook(book);
                books = bookManager.getBookList();
                Log.d("===>", "添加一本书后:" + books.toString());
                bookManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("===>", "onServiceDisconnected()");
            bookManager = null;
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d("===>", "receive new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onBookArrived(Book book) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (bookManager != null) {
                            try {
                                bookManager.getBookList();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookManager != null && bookManager.asBinder().isBinderAlive()) {
            Log.d("===>", "unregister listener:" + listener);
            try {
                bookManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
    }
}
