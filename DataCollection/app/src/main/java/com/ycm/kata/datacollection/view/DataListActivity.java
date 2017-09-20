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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.utils.CommonUtil;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends Activity implements GetDataListener, OnItemClickListener, View.OnClickListener{
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
    private String imageFile;
    private String decFile;
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
                    String ss = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection" + File.separator + "file" + File.separator + System.currentTimeMillis()+ ".doc";
                    try {
                        writeImage(ss, dataSource.get(0));
                    } catch (IOException | InvalidFormatException e) {
                        e.printStackTrace();
                    }
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
            saveFile("template.doc", this, R.raw.template);//文件目录res/raw
        } catch (IOException e) {
            e.printStackTrace();
        }
        //现场检查记录
        String aafileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template.doc";
        final String bbfileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template_printer.doc";
        //获取模板文件
        File demoFile = new File(aafileurl);
        //创建生成的文件
        File newFile = new File(bbfileurl);
        if (newFile.exists()) {
            newFile.delete();
        }
        for (int i = 0; i < dataSource.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("$project_name$", dataSource.get(i).getProjectName());
            map.put("$check_date$", CommonUtil.formatDate(dataSource.get(i).getCheckDate()));
            map.put("$unit_engineering$", dataSource.get(i).getUnitEngineering());
            map.put("$block_pile$", dataSource.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
            map.put("$defects$", dataSource.get(i).getDefects());
            map.put("$remark$", dataSource.get(i).getRemark());
            map.put("$image$", dataSource.get(i).getImagePath());
            writeDoc(demoFile, newFile, map);
        }

    }

    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
    public boolean writeDoc(File demoFile, File newFile, Map<String, String> map) {
        try {
            String imagePath = "" ;
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // 读取word文本内容
            Range range = hdt.getRange();
            // 替换文本内容
            for (Map.Entry<String, String> entry : map.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());

                if (TextUtils.equals(entry.getKey(), "$image$")) {
                    imagePath = entry.getValue();

                }
            }
            XWPFDocument xwpfDocument = new XWPFDocument(in);
            if (!TextUtils.isEmpty(imagePath)) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    FileInputStream imageFileIn = new FileInputStream(imageFile);
                    xwpfDocument.addPictureData(imageFileIn, XWPFDocument.PICTURE_TYPE_PNG);
                    List<XWPFPictureData> pictureDatas = xwpfDocument.getAllPictures();
                    XWPFPictureData xwpfPictureData = pictureDatas.get(0);
                    XWPFTable table = xwpfDocument.createTable();

                }
            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getImageType(String imgFile) {
        byte format;
        if (imgFile.endsWith(".emf")) {
            format = 2;
        } else if (imgFile.endsWith(".wmf")) {
            format = 3;
        } else if (imgFile.endsWith(".pict")) {
            format = 4;
        } else if (!imgFile.endsWith(".jpeg") && !imgFile.endsWith(".jpg")) {
            if (imgFile.endsWith(".png")) {
                format = 6;
            } else if (imgFile.endsWith(".dib")) {
                format = 7;
            } else if (imgFile.endsWith(".gif")) {
                format = 8;
            } else if (imgFile.endsWith(".tiff")) {
                format = 9;
            } else if (imgFile.endsWith(".eps")) {
                format = 10;
            } else if (imgFile.endsWith(".bmp")) {
                format = 11;
            } else {
                format = 12;
            }
        } else {
            format = 5;
        }
        return format;
    }

    private void writeImage(String decFile, ProjectEntity pje) throws IOException, InvalidFormatException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();
        XWPFTable xwpfTable = r.getDocument().createTable(4, 4);
        xwpfTable.getRow(1).getCell(0).setText("项目名称");
        xwpfTable.getRow(1).getCell(1).setText(pje.getProjectName());
        xwpfTable.getRow(1).getCell(2).setText("检测日期");
        xwpfTable.getRow(1).getCell(3).setText(formatDate(pje.getCheckDate()));


        xwpfTable.getRow(2).getCell(0).setText("单位工程");
        xwpfTable.getRow(2).getCell(1).setText(pje.getUnitEngineering());
        xwpfTable.getRow(2).getCell(2).setText("标段及桩号");
        xwpfTable.getRow(2).getCell(3).setText(pje.getBlock() + " " + pje.getPilNo());

        File imageFilePath = new File(pje.getImagePath());
        FileInputStream fileInputStream = new FileInputStream(imageFilePath);
        xwpfTable.getRow(3).getCell(0).addParagraph().createRun().addPicture(fileInputStream, getImageType(pje.getImagePath()), pje.getImagePath(), Units.toEMU(200.0D), Units.toEMU(200.0D));
//        xwpfTable.getRow(3).getCell(1).;
        xwpfTable.getRow(3).getCell(2).setText("缺陷描述");
        xwpfTable.getRow(3).getCell(3).setText(pje.getDefects());

        xwpfTable.getRow(4).getCell(0).setText("备注");
        xwpfTable.getRow(4).getCell(1).setText(pje.getRemark());
        r.addBreak();
        FileOutputStream out = new FileOutputStream(decFile);
        doc.write(out);
        out.close();
        doc.close();
//        r.setText(imgFile);
//        r.addBreak();
//        FileOutputStream out = null;
//        try {
//            r.addPicture(new FileInputStream(imgFile), getImageType(imgFile), imgFile, Units.toEMU(200.0D), Units.toEMU(200.0D));
//            out = new FileOutputStream(decFile);
//        } catch (InvalidFormatException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            r.addBreak(BreakType.PAGE);
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                doc.write(out);
//                doc.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }


    }

    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    /**
     * 将文件复制到SD卡，并返回该文件对应的数据库对象
     *
     * @return
     * @throws IOException
     */
    public void saveFile(String fileName, Context context, int rawid) throws IOException {

        // 首先判断该目录下的文件夹是否存在
        File dir = new File(Environment.getExternalStorageDirectory() + "/data_collection/file/");
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
//                printer();
            } else {
                // Permission Denied
                Toast.makeText(getBaseContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}