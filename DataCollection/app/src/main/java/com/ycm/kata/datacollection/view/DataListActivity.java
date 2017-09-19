package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
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

public class DataListActivity extends Activity implements GetDataListener, OnItemClickListener{
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
        myAdapter = new MyAdapter(this, this);
        listView.setAdapter(myAdapter);
//        listView.setOnItemClickListener(this);
        getData = new GetData(projectEntityDao, this);
        getData.start();
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
        myAdapter.setDataSource(dataSource);
        myAdapter.notifyDataSetChanged();

    }

    @Override
    public void textOnclickListener(int position) {
        Intent intent = new Intent();
        intent.putExtra("projectEntity", dataSource.get(position));
        intent.putExtra("tag", "edit");
        startActivity(intent);
    }

    @Override
    public void buttonOnclickListener(int position) {

    }

    private static class GetData extends Thread {
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