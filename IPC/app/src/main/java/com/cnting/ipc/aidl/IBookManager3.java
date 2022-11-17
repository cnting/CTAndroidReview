package com.cnting.ipc.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by cnting on 2022/11/16
 * 再次手写跨进程通信
 */
public interface IBookManager3 extends IInterface {
    java.lang.String DESCRIPTOR = "com.cnting.ipc.aidl.IBookManager";
    int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;


}

class BookManagerImpl extends Binder implements IBookManager3 {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * 用于将服务端的Binder对象转换成客户端所需的AIDL接口类型的对象
     */
    public static IBookManager3 asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManager3) {
            //如果客户端和服务端位于同一个进程，返回服务端的Stub对象本身
            return (IBookManager3) iin;
        } else {
            //否则返回代理对象
            return new BookManagerImpl.Proxy(obj);
        }
    }

    /**
     * 用于返回当前的Binder对象
     *
     * @return
     */
    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 运行在服务端的Binder线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法来处理
     *
     * @param code  确定请求的目标方法
     * @param data  目标方法所需参数
     * @param reply 当目标方法执行完毕，向reply写入返回值
     * @param flags
     * @return 如果返回false，客户端的请求会失败
     * @throws RemoteException
     */
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }


    @Override
    public List<Book> getBookList() throws RemoteException {
        //服务端的方法，待实现
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        //服务端的方法，待实现
    }

    private static class Proxy implements IBookManager3 {
        private IBinder mRemote;

        Proxy(IBinder remote) {
            this.mRemote = remote;
        }

        /**
         * 这个方法运行在客户端，调用服务端方法
         *
         * @return
         * @throws RemoteException
         */
        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();  //存放参数
            Parcel replay = Parcel.obtain(); //存放返回值
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, replay, 0);
                replay.readException();
                result = replay.createTypedArrayList(Book.CREATOR);
            } finally {
                replay.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, replay, 0);
                replay.readException();
            } finally {
                replay.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
    }

}
