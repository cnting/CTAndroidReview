package com.cnting.view.scroll_conflict;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnting.view.R;

import java.util.ArrayList;

/**
 * Created by cnting on 2022/11/22
 */
public class TestScrollConflictActivity extends AppCompatActivity {

    HorizontalScrollViewEx listContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict);

        initView();
    }

    private void initView() {
        listContainer = findViewById(R.id.container);

        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.content_layout, listContainer, false);
            TextView title = layout.findViewById(R.id.title);
            title.setText("page " + i);
            ListView listView = layout.findViewById(R.id.list);

            ArrayList<String> datas = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                datas.add("item:" + j);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, datas);
            listView.setAdapter(adapter);

            listContainer.addView(layout);
        }
    }
}
