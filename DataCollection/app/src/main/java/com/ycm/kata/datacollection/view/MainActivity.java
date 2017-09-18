package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener, TextWatcher {
    private String photoFileCachePath = "";
    private EditText etDate;
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
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private String dateStr;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = System.currentTimeMillis();
        dateStr = formatDate(time);
        initView();
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
    }

    private void initView() {
        etDate = (EditText) findViewById(R.id.date_et);
        etDate.setText(dateStr);
        etDate.setEnabled(false);
        etName = (EditText) findViewById(R.id.name_et);
        etName.addTextChangedListener(this);
        etProject = (EditText) findViewById(R.id.project_et);
        etProject.addTextChangedListener(this);
        etBlock = (EditText) findViewById(R.id.block_et);
        etBlock.addTextChangedListener(this);
        etPile = (EditText) findViewById(R.id.pile_et);
        etPile.addTextChangedListener(this);
        etRemark = (EditText) findViewById(R.id.remark_et);
        etRemark.addTextChangedListener(this);
        etDefect = (EditText) findViewById(R.id.defects_et);
        etDefect.addTextChangedListener(this);
        tvHint = (TextView) findViewById(R.id.text_hint_tv);
        ivPicture = (ImageView) findViewById(R.id.image_iv);
        tvHint.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.save_btn);
        btnSave.setOnClickListener(this);
        btnAdd = (Button) findViewById(R.id.add_btn);
        btnAdd.setOnClickListener(this);
    }

    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
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
            case R.id.save_btn:
                projectEntity = getProjectEntity();
                insert(projectEntity);
                break;
            case R.id.add_btn:
                break;
        }
    }

    private ProjectEntity getProjectEntity() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setCheckDate(time);
        String name = "";
        Editable nameEditable = etName.getText();
        if (nameEditable != null) {
            name = nameEditable.toString();
        }

        String project = "";
        Editable projectEditable = etProject.getText();
        if (projectEditable != null) {
            project = projectEditable.toString();
        }

        String block = "";
        Editable blockEditable = etBlock.getText();
        if (blockEditable != null) {
            block = blockEditable.toString();
        }

        String pile = "";
        Editable pileEditable = etPile.getText();
        if (pileEditable != null) {
            pile = pileEditable.toString();
        }

        String remark = "";
        Editable remarkEditable = etRemark.getText();
        if (remarkEditable != null) {
            remark = remarkEditable.toString();
        }

        String defect = "";
        Editable defectEditable = etDefect.getText();
        if (defectEditable != null) {
            defect = defectEditable.toString();
        }
        projectEntity.setBlock(block);
        projectEntity.setPilNo(pile);
        projectEntity.setDefects(defect);
        projectEntity.setProjectName(name);
        projectEntity.setRemark(remark);
        projectEntity.setUnitEngineering(project);
        projectEntity.setUpdateTime(time);
        projectEntity.setImagePath(photoFileCachePath);
        return projectEntity;
    }

    private void insert(ProjectEntity projectEntity) {
        projectEntityDao.insert(projectEntity);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
