// IBookManager.aidl
package com.cnting.ipc.aidl;
import com.cnting.ipc.aidl.Book;
import com.cnting.ipc.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
