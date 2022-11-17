package com.cnting.ipc.binderpool;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnting.ipc.aidl.ICompute;
import com.cnting.ipc.aidl.ISecurityCenter;

/**
 * Created by cnting on 2022/11/17
 */
public class TestBinderPoolActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
    }

    void test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BinderPool binderPool = BinderPool.getInstance(TestBinderPoolActivity.this);
                IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
                ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(securityBinder);
                Log.d("===>", "visit ISecurityCenter");
                String msg = "helloworld-安卓";
                Log.d("===>", "content:" + msg);
                try {
                    String password = securityCenter.encrypt(msg);
                    Log.d("===>", "加密:" + password);
                    Log.d("===>", "解密:" + securityCenter.decrypt(password));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                Log.d("===>", "visit ICompute");
                IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
                ICompute compute = ComputeImpl.asInterface(computeBinder);
                try {
                    Log.d("===>","3+5="+compute.add(3,5));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
