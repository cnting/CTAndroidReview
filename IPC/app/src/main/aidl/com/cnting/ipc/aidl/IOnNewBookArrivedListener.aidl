// IOnNewBookArrivedListener.aidl
package com.cnting.ipc.aidl;
import com.cnting.ipc.aidl.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onBookArrived(in Book book);
}
