package com.cnting.ipc.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by cnting on 2022/11/17
 */
public class TCPServerService extends Service {
    private boolean mIsServiceDestroyed = false;
    private String[] definedMessages = new String[]{
            "你好啊",
            "请问你叫什么名字",
            "肋骨栗色丽丽色",
            "莉莉丝了几遍了日历"
    };


    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("establish tcp server failed，port：8688");
                e.printStackTrace();
            }
            while (!mIsServiceDestroyed) {
                //接收客户端请求
                final Socket client;
                try {
                    client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void responseClient(Socket client) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
        ) {
            out.println("欢迎来到聊天室");
            while (!mIsServiceDestroyed) {
                String str = in.readLine();
                System.out.println("msg from client：" + str);
                if (str == null) {
                    //断开连接
                    break;
                }
                int i = new Random().nextInt(definedMessages.length);
                String msg = definedMessages[i];
                out.println(msg);
                System.out.println("send:" + msg);
            }
            System.out.println("client quit");
        } finally {
            client.close();
        }
        //用于接收客户端消息
//        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//        //用于向客户端发送消息
//        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

    }
}
