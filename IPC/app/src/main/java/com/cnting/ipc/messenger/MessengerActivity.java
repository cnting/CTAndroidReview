package com.cnting.ipc.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnting.ipc.R;

import static com.cnting.ipc.Constants.MSG_FROM_CLIENT;
import static com.cnting.ipc.Constants.MSG_FROM_SERVICE;

/**
 * Created by cnting on 2021/1/14
 */
public class MessengerActivity extends AppCompatActivity {

    private Messenger serviceMessenger;
    private Messenger clientMessenger = new Messenger(new ClientHandler());

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    Log.d("客户端", "接受来自服务端的消息:" + msg.getData().getString("msg"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("msg", "来自客户端的问候");
            msg.setData(bundle);
            msg.what = MSG_FROM_CLIENT;
            msg.replyTo = clientMessenger;
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
