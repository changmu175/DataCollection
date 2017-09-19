package com.ycm.kata.datacollection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.a1)
    EditText a1;
    @Bind(R.id.a2)
    EditText a2;
    @Bind(R.id.a3)
    EditText a3;
    @Bind(R.id.a4)
    EditText a4;
    @Bind(R.id.a5)
    EditText a5;
    @Bind(R.id.a6)
    EditText a6;
    @Bind(R.id.a7)
    EditText a7;
    @Bind(R.id.a8)
    EditText a8;
    @Bind(R.id.a9)
    EditText a9;


    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_execute)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
                    printer();
                }

                break;
        }
    }


    /**
     * 为了保证模板的可用，最好在现有的模板上复制后修改
     */
    private void printer() {
        try {
            saveFile("demo.doc", MainActivity.this, R.raw.demo);//文件目录res/raw
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
