package com.cnting.ipc.contentprovider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by cnting on 2021/1/18
 */
public class ProviderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = Uri.parse("content://com.cnting.ipc.contentprovider.BookProvider");
        getContentResolver().query(uri, null, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null, null);
    }
}
