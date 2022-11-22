package com.cnting.ipc.sharememory;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by cnting on 2022/11/22
 * 测试共享内存
 */
public class TestShareMemoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ShareMemoryService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    //通过binder机制跨进程调用服务端的接口
                    service.transact(1, data, reply, 0);
                    FileDescriptor fd = reply.readFileDescriptor().getFileDescriptor();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fd));
                    Log.i("===>", "client read:" + bufferedReader.readLine());
                } catch (RemoteException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}
