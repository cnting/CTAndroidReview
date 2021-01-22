package com.cnting.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.cnting.ipc.Constants.MSG_FROM_CLIENT;
import static com.cnting.ipc.Constants.MSG_FROM_SERVICE;

/**
 * Created by cnting on 2021/1/14
 */
public class MessengerService extends Service {


    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private class MessengerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Log.d("服务端", "接受到来自客户端的消息:" + msg.getData().getString("msg"));
                    Messenger replyMessenger = msg.replyTo;
                    Message replyMsg = Message.obtain();
                    replyMsg.what = MSG_FROM_SERVICE;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "我是来自服务端的消息啊");
                    replyMsg.setData(bundle);
                    try {
                        replyMessenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
