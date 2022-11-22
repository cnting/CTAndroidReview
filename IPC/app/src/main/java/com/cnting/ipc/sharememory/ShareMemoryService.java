package com.cnting.ipc.sharememory;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cnting on 2022/11/22
 * 匿名共享内存
 * https://www.jianshu.com/p/62db83a97a5c
 */
public class ShareMemoryService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                try {
                    String str = "ct test";
                    byte[] contentBytes = str.getBytes();
                    //创建匿名共享内存
                    MemoryFile memoryFile = new MemoryFile("memfile", contentBytes.length);
                    //写入字符数据
                    memoryFile.writeBytes(contentBytes, 0, 0, contentBytes.length);
                    Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                    //通过反射获得文件句柄
                    FileDescriptor fd = (FileDescriptor) method.invoke(memoryFile);
                    ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.dup(fd);
                    //将文件句柄写到binder调用的返回值中
                    reply.writeFileDescriptor(fd);
                    return true;
                } catch (IOException | NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
