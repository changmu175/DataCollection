package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends Activity implements GetDataListener{
    Button btnAdd;
    Button btnExport;
    ListView listView;
    MyAdapter myAdapter;
    static List<ProjectEntity> dataSource;
    ProjectEntityDao projectEntityDao;
    ProjectEntity projectEntity;
    GetData getData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        btnAdd = findViewById(R.id.add_new_btn);
        btnExport = findViewById(R.id.export_btn);
        listView = findViewById(R.id.list);
        dataSource = new ArrayList<>();
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        getData = new GetData(projectEntityDao, this);
        getData.start();
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
        myAdapter.setDataSource(dataSource);
        myAdapter.notifyDataSetChanged();

    }

    static class GetData extends Thread {
        private WeakReference<ProjectEntityDao> weakReference;
        private WeakReference<GetDataListener> listenerWeakReference;

        GetData(ProjectEntityDao projectEntityDao, GetDataListener listener) {
            weakReference = new WeakReference<ProjectEntityDao>(projectEntityDao);
            listenerWeakReference = new WeakReference<GetDataListener>(listener);
        }

        @Override
        public void run() {
            super.run();
            dataSource = weakReference.get().loadAll();
            listenerWeakReference.get().loadSuccess(dataSource);
        }
    }

}