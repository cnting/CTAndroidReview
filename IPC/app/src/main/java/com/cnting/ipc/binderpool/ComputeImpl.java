package com.cnting.ipc.binderpool;

import android.os.RemoteException;

import com.cnting.ipc.aidl.ICompute;

/**
 * Created by cnting on 2022/11/17
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
