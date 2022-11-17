// IBinderPool.aidl
package com.cnting.ipc.aidl;


interface IBinderPool {
    IBinder queryBinder(int binderCode);
}