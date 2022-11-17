package com.cnting.ipc.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by cnting on 2022/11/17
 */
public class BinderPoolService extends Service {
    private Binder binderPool = new BinderPool.BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }
}
