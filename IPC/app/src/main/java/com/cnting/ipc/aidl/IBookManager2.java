package com.cnting.ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by cnting on 2021/1/19
 * 手动实现跨进程调用
 */
interface IBookManager2 extends IInterface {

    List<Book> getBookList() throws android.os.RemoteException;

    void addBook(Book book) throws android.os.RemoteException;

    void registerListener(IOnNewBookArrivedListener listener) throws android.os.RemoteException;

    void unregisterListener(IOnNewBookArrivedListener listener) throws android.os.RemoteException;

    /**
     * 生成唯一标识
     */
    static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_unregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);

    /**
     * IBookManager本地对象
     */
    abstract class Stub extends Binder implements IBookManager2 {

        private static final java.lang.String DESCRIPTOR = "com.cnting.ipc.aidl.IBookManager";

        /**
         * 这个方法给客户端用的，当客户端ServiceConnection()里获取到IBinder时，通过这个方法转换成IBookMananger2
         */
        public static IBookManager2 asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            //查找Binder本地对象，如果找到了说明Client和Server在同一个进程
            IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IBookManager2) {
                return (IBookManager2) iin;
            }
            return new Proxy(binder);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_getBookList:
                    data.enforceInterface(DESCRIPTOR);
                    List<Book> _result = getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    break;

            }
            return super.onTransact(code, data, reply, flags);
        }

        /**
         * 如果不是在同一个进程，使用代理类调用对应方法
         */
        static class Proxy implements IBookManager2 {

            IBinder remote;

            public Proxy(IBinder remote) {
                this.remote = remote;
            }

            @Override
            public List<Book> getBookList() throws android.os.RemoteException {
                Parcel _data = Parcel.obtain();   //获取参数
                Parcel _reply = Parcel.obtain();  //写入返回值
                List<Book> _result;
                _data.writeInterfaceToken(DESCRIPTOR);
                try {
                    boolean _status = remote.transact(TRANSACTION_getBookList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);  //类型转换
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    _data.writeInt(1);
                    book.writeToParcel(_data, 0);
                } else {
                    _data.writeInt(0);
                }
                try {
                    boolean _status = remote.transact(TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _data.recycle();
                    _reply.recycle();
                }
            }

            @Override
            public void registerListener(IOnNewBookArrivedListener listener) {

            }

            @Override
            public void unregisterListener(IOnNewBookArrivedListener listener) {

            }

            @Override
            public IBinder asBinder() {
                return remote;
            }
        }
    }
}
