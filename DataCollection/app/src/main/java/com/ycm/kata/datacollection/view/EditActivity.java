package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by changmuyu on 2017/9/20.
 * Description:
 */

public class EditActivity extends Activity implements TextWatcher, View.OnClickListener {
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
    private ProgressBar progressBar;
    private Button btnSave;
    private Button btnAdd;
    private ProjectEntityDao projectEntityDao;
//    private ProjectEntity projectEntity;
    private String dateStr;
    private long time;
    private ProjectEntity pje;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        Intent intent = getIntent();
        if (intent != null) {
            if (TextUtils.equals(intent.getStringExtra("tag"), "edit")) {
                pje = (ProjectEntity) intent.getSerializableExtra("projectEntity");
                setContent(pje);
            }
        }
    }

    private void initView() {
        etDate = findViewById(R.id.date_et);
        etDate.setText(dateStr);
        etDate.setEnabled(false);
        etName = findViewById(R.id.name_et);
        etName.addTextChangedListener(this);
        etProject = findViewById(R.id.project_et);
        etProject.addTextChangedListener(this);
        etBlock = findViewById(R.id.block_et);
        etBlock.addTextChangedListener(this);
        etPile = findViewById(R.id.pile_et);
        etPile.addTextChangedListener(this);
        etRemark = findViewById(R.id.remark_et);
        etRemark.addTextChangedListener(this);
        etDefect = findViewById(R.id.defects_et);
        etDefect.addTextChangedListener(this);
        tvHint = findViewById(R.id.text_hint_tv);
        ivPicture = findViewById(R.id.image_iv);
        tvHint.setOnClickListener(this);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        btnSave = findViewById(R.id.save_btn);
        btnSave.setOnClickListener(this);
        btnAdd = findViewById(R.id.add_btn);
        btnAdd.setOnClickListener(this);
    }

    private void setContent(ProjectEntity pje) {
        etName.setText(pje.getProjectName());
        etDate.setText(formatDate(pje.getCheckDate()));
        etDefect.setText(pje.getDefects());
        etRemark.setText(pje.getRemark());
        etPile.setText(pje.getPilNo());
        etBlock.setText(pje.getBlock());
        etProject.setText(pje.getUnitEngineering());
        tvHint.setVisibility(View.GONE);
        ivPicture.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(pje.getImagePath())) {
            ivPicture.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            Glide.with(this).load(pje.getImagePath()).crossFade().into(ivPicture);
        } else {
            ivPicture.setVisibility(View.GONE);
            tvHint.setVisibility(View.VISIBLE);
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
        projectEntity.setId(pje.getId());
        projectEntity.setBlock(block);
        projectEntity.setPilNo(pile);
        projectEntity.setDefects(defect);
        projectEntity.setProjectName(name);
        projectEntity.setRemark(remark);
        projectEntity.setUnitEngineering(project);
        projectEntity.setUpdateTime(time);
        projectEntity.setImagePath(pje.getImagePath());
        return projectEntity;
    }


    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
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

    @Override
    public void onClick(View view) {
        if (view == btnSave) {
            ProjectEntity projectEntity = getProjectEntity();
            projectEntityDao.update(projectEntity);
            finish();
        } else if (view == btnAdd) {
            ProjectEntity projectEntity = getProjectEntity();
            projectEntityDao.update(projectEntity);
        }
    }
}
