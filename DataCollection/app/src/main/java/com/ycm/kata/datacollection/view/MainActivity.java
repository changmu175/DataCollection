package com.ycm.kata.datacollection.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycm.kata.datacollection.R;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String photoFileCachePath;
    private EditText etData;
    private EditText etName;
    private EditText etProject;
    private EditText etBlock;
    private EditText etPile;
    private EditText etRemark;
    private EditText etDefect;
    private TextView tvHint;
    private ImageView ivPicture;
    private Button btnSave;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initView() {
        etData = (EditText) findViewById(R.id.date_et);
        etName = (EditText) findViewById(R.id.name_et);
        etProject = (EditText) findViewById(R.id.project_et);
        etBlock = (EditText) findViewById(R.id.block_et);
        etPile = (EditText) findViewById(R.id.pile_et);
        etRemark = (EditText) findViewById(R.id.remark_et);
        etDefect = (EditText) findViewById(R.id.defects_et);
        tvHint = (TextView) findViewById(R.id.text_hint_tv);
        ivPicture = (ImageView) findViewById(R.id.image_iv);
        tvHint.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.save_btn);
        btnSave.setOnClickListener(this);
        btnAdd = (Button) findViewById(R.id.add_btn);
        btnAdd.setOnClickListener(this);
    }

    public void startToPhoto() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            //文件保存路径
            String imageRootPath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "data";
            File imgRootPath = new File(imageRootPath);
            if (!imgRootPath.exists() && !imgRootPath.mkdirs()) {
                return;
            }
            //文件名称
            photoFileCachePath = imageRootPath +        //父路径
                    System.currentTimeMillis() +        //名称（时间）
                    ".jpeg";                            //后缀
            //启动相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoFileCachePath)));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_hint_tv:
                startToPhoto();
                break;
        }
    }
}
