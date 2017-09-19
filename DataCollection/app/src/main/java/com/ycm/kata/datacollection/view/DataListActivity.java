package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;

import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.util.List;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends Activity {
    private Button btnAdd;
    private Button btnExport;
    private ListView listView;
    private MyAdapter myAdapter;
    private List<ProjectEntity> dataSource;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        btnAdd = findViewById(R.id.add_new_btn);
        btnExport = findViewById(R.id.export_btn);
        listView = findViewById(R.id.list);
    }
}
