// IBookManager.aidl
package com.cnting.ipc.aidl;
import com.cnting.ipc.aidl.Book;
import com.cnting.ipc.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    Book addInBook(in Book book);
    Book addOutBook(out Book book);
    Book addInoutBook(inout Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

     oneway void testOneway(in Book book);
     Book testNoOneway(out Book book);
}
