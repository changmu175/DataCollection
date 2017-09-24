package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.utils.CameraUtil;
import com.ycm.kata.datacollection.utils.CommonUtils;
import com.ycm.kata.datacollection.utils.PermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private RelativeLayout llTakePhoto;
    private ProgressBar progressBar;
    private Button btnSave;
    private Button btnAdd;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private String filePath = "";
    private static UpdateImageHandler updateImageHandler;
    private SaveImageThread saveImageThread;
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
        llTakePhoto = findViewById(R.id.take_photo);
        llTakePhoto.setOnClickListener(this);
        tvHint = findViewById(R.id.text_hint_tv);
        ivPicture = findViewById(R.id.image_iv);
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

    private ProjectEntity getNewProjectEntity() {
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
        switch (view.getId()) {
            case R.id.take_photo:
//                CameraUtil.getInstance().camera(this);
                startToPhoto();
                break;
        }
        if (view == btnSave) {
            ProjectEntity projectEntity = getProjectEntity();
            projectEntityDao.update(projectEntity);
            finish();
        } else if (view == btnAdd) {
            if (pje == null) {
                pje = getProjectEntity();
                if (insert(pje) != -1) {
                    Toast.makeText(getBaseContext(), "新增成功", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            ProjectEntity pro = getNewProjectEntity();
            if (!pro.equals(pje)) {
                pje = pro;
                if (insert(pro) != -1) {
                    Toast.makeText(getBaseContext(), "新增成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "你没做任何改变", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private long insert(ProjectEntity projectEntity) {
        if (projectEntityDao == null) {
            return -1;
        }
        return projectEntityDao.insert(projectEntity);
    }

    private File file;
    public void startToPhoto() {
        //启动相机拍照
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //文件名称
            photoFileCachePath = CommonUtils.getImageFilePath(System.currentTimeMillis());
            //启动相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file = new File(photoFileCachePath)));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (file == null) {
                Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            ivPicture.setVisibility(View.GONE);
            final String rootFilePath = file.getPath();
            final File rootFile = new File(rootFilePath);
            if (!rootFile.exists()) {
                progressBar.setVisibility(View.GONE);
                ivPicture.setVisibility(View.VISIBLE);
//                tvHint.setVisibility(View.VISIBLE);
                Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmap = BitmapFactory.decodeFile(rootFilePath);

            final String desFileName = CommonUtils.getImageFilePath(System.currentTimeMillis()); /*getFileName(System.currentTimeMillis())*//*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/

            filePath = desFileName;
            updateImageHandler = new UpdateImageHandler(this, bitmap);
            saveImageThread = new SaveImageThread(bitmap, desFileName, rootFile, updateImageHandler);
            saveImageThread.start();

//            btnAdd.setEnabled(true);
//            btnSave.setEnabled(true);
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int screenWidth = dm.widthPixels;
//            showBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
//            ivPicture.setImageBitmap(showBitmap);// 将图片显示在ImageView里
//            tvHint.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
//            ivPicture.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap showBitmap = null;
    private static class UpdateImageHandler extends Handler {
        WeakReference<EditActivity> mainActivityWeakReference;
        WeakReference<Bitmap> bitmapWeakReference;
        UpdateImageHandler(EditActivity mainActivity, Bitmap bitmap) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            bitmapWeakReference = new WeakReference<>(bitmap);
        }

        UpdateImageHandler(EditActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = bitmapWeakReference.get();
            DisplayMetrics dm = new DisplayMetrics();
            mainActivityWeakReference.get().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            mainActivityWeakReference.get().showBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
            mainActivityWeakReference.get().ivPicture.setImageBitmap(mainActivityWeakReference.get().showBitmap);// 将图片显示在ImageView里
            mainActivityWeakReference.get().tvHint.setVisibility(View.GONE);
            mainActivityWeakReference.get().progressBar.setVisibility(View.GONE);
            mainActivityWeakReference.get().ivPicture.setVisibility(View.VISIBLE);
            CommonUtils.destroyBitmap(bitmap);
        }
    }

    private static class SaveImageThread extends Thread {
        private WeakReference<Bitmap> bitmapWeakReference;
        private WeakReference<UpdateImageHandler> handlerWeakReference;
        private String fileName;
        private File rootFile;

        SaveImageThread(Bitmap bitmap, String fileName, File rootFile, UpdateImageHandler handler) {
            bitmapWeakReference = new WeakReference<>(bitmap);
            handlerWeakReference = new WeakReference<>(handler);
            this.fileName = fileName;
            this.rootFile = rootFile;
        }

        @Override
        public void run() {
            Bitmap bitmap = bitmapWeakReference.get();
            bitmap = getZoomImage(bitmap, 650);
            FileOutputStream b = null;
            ByteArrayOutputStream outputStream = bitmapToByteArray(bitmap, false);
            try {
                File imageFile = new File(fileName);
                if (!imageFile.exists() && !imageFile.createNewFile()) {
                    return;
                }
                b = new FileOutputStream(fileName);
                b.write(outputStream.toByteArray());
                rootFile.delete();
                handlerWeakReference.get().sendEmptyMessage(123);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (b != null) {
                        b.flush();
                        b.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        ByteArrayOutputStream outputStream;
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        outputStream = bitmapToByteArray(bitmap, false);
        double currentSize = outputStream.toByteArray().length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            outputStream = bitmapToByteArray(bitmap, false);
            currentSize = outputStream.toByteArray().length / 1024;
        }

        return bitmap;
    }


    public static ByteArrayOutputStream bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

//        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
        }
//        return result;
        return output;
    }


    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }


    public void showCamera() {
        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CAMERA:
                    Toast.makeText(getBaseContext(), "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(getBaseContext(), "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
}
