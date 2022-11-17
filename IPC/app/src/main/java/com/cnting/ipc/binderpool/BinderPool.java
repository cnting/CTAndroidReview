package com.cnting.ipc.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.cnting.ipc.aidl.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by cnting on 2022/11/17
 */
public class BinderPool {
    private static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context context;
    private IBinderPool binderPool;
    private static volatile BinderPool instance;
    private CountDownLatch connectBinderPoolCountDownLatch;

    private BinderPool(Context context) {
        this.context = context;
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    private synchronized void connectBinderPoolService() {
        connectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(context, BinderPoolService.class);
        context.bindService(service, binderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            connectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        if (binderPool != null) {
            try {
                binder = binderPool.queryBinder(binderCode);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    private ServiceConnection binderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            connectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            binderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            binderPool = null;
            connectBinderPoolService();

        }
    };

    /**
     * Service调用
     */
    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_SECURITY_CENTER: {
                    binder = new SecurityCenterImpl();
                    break;
                }
                case BINDER_COMPUTE: {
                    binder = new ComputeImpl();
                    break;
                }
                default:
                    break;
            }
            return binder;
        }
    }

}
