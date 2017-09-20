package com.ycm.kata.datacollection.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends Activity implements GetDataListener, OnItemClickListener, View.OnClickListener {
    private Button btnAdd;
    private Button btnExport;
    private Button btnPrevious;
    private Button btnNext;
    private ListView listView;
    private MyAdapter myAdapter;
    private static List<ProjectEntity> dataSource;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private GetData getData;
    private int index = 0;
    private static final int VIEW_COUNT = 6;
    private UpdateViewHandler handler;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        btnAdd = findViewById(R.id.add_new_btn);
        btnAdd.setOnClickListener(this);
        btnExport = findViewById(R.id.export_btn);
        btnExport.setOnClickListener(this);
        btnPrevious = findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        btnNext = findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        listView = findViewById(R.id.list);
        dataSource = new ArrayList<>();
        handler = new UpdateViewHandler(this);
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        myAdapter = new MyAdapter(this, this);
        listView.setAdapter(myAdapter);
        View empty = LayoutInflater.from(this).inflate(R.layout.empy_layout, null);
        listView.setEmptyView(empty);
//        listView.setOnItemClickListener(this);
//        getData = new GetData(projectEntityDao, this);
//        getData.start();
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
//        myAdapter.setDataSource(dataSource);
//        myAdapter.notifyDataSetChanged();
        handler.sendEmptyMessage(123);

    }

    private static class UpdateViewHandler extends Handler {
        private WeakReference<DataListActivity> weakReference;

        UpdateViewHandler(DataListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                weakReference.get().myAdapter.setDataSource(dataSource);
                weakReference.get().myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataSource();
    }

    private void getDataSource() {
        getData = new GetData(projectEntityDao, this);
        getData.start();
    }

    @Override
    public void textOnclickListener(int position) {
        Intent intent = new Intent();
        intent.setClass(this, EditActivity.class);
        intent.putExtra("projectEntity", dataSource.get(position));
        intent.putExtra("tag", "edit");
        startActivity(intent);
    }

    @Override
    public void buttonOnclickListener(int position) {
        deleteById(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_btn:
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.export_btn:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
                    printer();
                }
                break;
            case R.id.previous_btn:
                preView();
                break;
            case R.id.next_btn:
                nextView();
                break;
        }
    }

    private static class GetData extends Thread {
        private WeakReference<ProjectEntityDao> weakReference;
        private WeakReference<GetDataListener> listenerWeakReference;

        GetData(ProjectEntityDao projectEntityDao, GetDataListener listener) {
            weakReference = new WeakReference<>(projectEntityDao);
            listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            super.run();
            dataSource = weakReference.get().loadAll();
            listenerWeakReference.get().loadSuccess(dataSource);
        }
    }

    private void deleteById(int position) {
        if (dataSource == null || dataSource.isEmpty()) {
            return;
        }

        ProjectEntity pro = dataSource.get(position);
        projectEntityDao.delete(pro);
        getDataSource();
    }

    // 点击左边的Button，表示向前翻页，索引值要减1.
    public void preView() {
        myAdapter.setIndex(index--);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
        // 检查Button是否可用。
        checkButton();
    }

    // 点击右边的Button，表示向后翻页，索引值要加1.
    public void nextView() {
        myAdapter.setIndex(index++);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();

        // 检查Button是否可用。
        checkButton();
    }

    public void checkButton() {
        // 索引值小于等于0，表示不能向前翻页了，以经到了第一页了。
        // 将向前翻页的按钮设为不可用。
        if (index <= 0) {
            btnNext.setEnabled(false);
        } else {
            btnPrevious.setEnabled(true);
        }
        // 值的长度减去前几页的长度，剩下的就是这一页的长度，如果这一页的长度比View_Count小，表示这是最后的一页了，后面在没有了。
        // 将向后翻页的按钮设为不可用。
        if (dataSource.size() - index * VIEW_COUNT <= VIEW_COUNT) {
            btnNext.setEnabled(false);
        }
        // 否则将2个按钮都设为可用的。
        else {
            btnNext.setEnabled(true);
        }

    }

    /**
     * 为了保证模板的可用，最好在现有的模板上复制后修改
     */
    private void printer() {
        try {
            saveFile("demo.doc", this, R.raw.demo);//文件目录res/raw
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //现场检查记录
        String aafileurl = Environment.getExternalStorageDirectory() + "/inspection/demo.doc";
        final String bbfileurl = Environment.getExternalStorageDirectory() + "/inspection/demo_printer.doc";
        //获取模板文件
        File demoFile = new File(aafileurl);
        //创建生成的文件
        File newFile = new File(bbfileurl);
        if (newFile.exists()) {
            newFile.delete();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("$companyName$", a1.getText().toString().trim());
        map.put("$companyAddress$", a2.getText().toString().trim());
        map.put("$companyPic$", a3.getText().toString().trim());
        map.put("$companyWork$", a4.getText().toString().trim());
        map.put("$companyPhone$", a5.getText().toString().trim());
        map.put("$CheckAddress$", a6.getText().toString().trim());

        map.put("$userName$", a7.getText().toString().trim());
        map.put("$userNum$", a8.getText().toString().trim());
        map.put("$content$", a9.getText().toString().trim());
        writeDoc(demoFile, newFile, map);

    }

    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
    public boolean writeDoc(File demoFile, File newFile, Map<String, String> map) {
        try {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // 读取word文本内容
            Range range = hdt.getRange();
            // 替换文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将文件复制到SD卡，并返回该文件对应的数据库对象
     *
     * @return
     * @throws IOException
     */
    public void saveFile(String fileName, Context context, int rawid) throws IOException {

        // 首先判断该目录下的文件夹是否存在
        File dir = new File(Environment.getExternalStorageDirectory() + "/inspection/");
        if (!dir.exists()) {
            // 文件夹不存在 ， 则创建文件夹
            dir.mkdirs();
        }

        // 判断目标文件是否存在
        File file1 = new File(dir, fileName);

        if (!file1.exists()) {
            file1.createNewFile(); // 创建文件

        }
        InputStream input = context.getResources().openRawResource(rawid); // 获取资源文件raw
        try {

            FileOutputStream out = new FileOutputStream(file1); // 文件输出流、用于将文件写到SD卡中
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = (input.read(buffer))) != -1) { // 读取文件，-- 进到内存

                out.write(buffer, 0, len); // 写入数据 ，-- 从内存出
            }
            input.close();
            out.close(); // 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                printer();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}