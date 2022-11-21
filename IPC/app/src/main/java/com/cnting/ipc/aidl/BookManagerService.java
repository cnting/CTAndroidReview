package com.cnting.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cnting on 2021/1/18
 */
public class BookManagerService extends Service {
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();
    private AtomicBoolean isServiceDestoryed = new AtomicBoolean();


    private Binder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
//            SystemClock.sleep(5000);
            return bookList;
        }

        @Override
        public Book addInBook(Book book) throws RemoteException {
            book.bookId = -1;
            book.bookName = book.bookName + "-in";
            bookList.add(book);
            return book;
        }

        @Override
        public Book addOutBook(Book book) throws RemoteException {
            book.bookId = -1;
            book.bookName = book.bookName + "-out";
            bookList.add(book);
            return book;
        }

        @Override
        public Book addInoutBook(Book book) throws RemoteException {
            book.bookId = -1;
            book.bookName = book.bookName + "-inout";
            bookList.add(book);
            return book;
        }


        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);

            int size = listeners.beginBroadcast();
            listeners.finishBroadcast();
            Log.d("===>", "registerListener()：" + size);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);

            int size = listeners.beginBroadcast();
            listeners.finishBroadcast();
            Log.d("===>", "unregisterListener()：" + size);
        }

        @Override
        public void testOneway(Book book) throws RemoteException {
            Log.d("===>", "服务端：test oneway start");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("===>", "服务端：test oneway end");
        }

        @Override
        public Book testNoOneway(Book book) throws RemoteException {
            Log.d("===>", "服务端：test no oneway start");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("===>", "服务端：test no oneway end");
            return null;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(1, "Android"));
        bookList.add(new Book(2, "iOS"));

        //listener 测试
//        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestoryed.set(true);
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!isServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = bookList.size() + 1;
                Book book = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        bookList.add(book);
        int size = listeners.beginBroadcast();
        for (int i = 0; i < size; i++) {
            listeners.getBroadcastItem(i).onBookArrived(book);
        }
        listeners.finishBroadcast();
    }
}
