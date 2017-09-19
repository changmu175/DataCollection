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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private ProgressBar progressBar;
    private Button btnSave;
    private Button btnAdd;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private String dateStr;
    private long time;
    private String filePath = "";
    private File file;
    private static UpdateImageHandler updateImageHandler;
    private SaveImageThread saveImageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = System.currentTimeMillis();
        dateStr = formatDate(time);
        initView();
        Intent intent = getIntent();
        ProjectEntity pje;
        if (intent != null) {
            if (TextUtils.equals(intent.getStringExtra("tag"), "edit")) {
                pje = (ProjectEntity) intent.getSerializableExtra("projectEntity");
                setContent(pje);
                return;
            }
        }
        updateImageHandler = new UpdateImageHandler(this);
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
    }

    private void setContent(ProjectEntity pje) {
        etDate.setText(formatDate(pje.getCheckDate()));
        etDefect.setText(pje.getDefects());
        etRemark.setText(pje.getRemark());
        etPile.setText(pje.getPilNo());
        etBlock.setText(pje.getBlock());
        etProject.setText(pje.getProjectName());
        tvHint.setVisibility(View.GONE);
        ivPicture.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(pje.getImagePath());
        ivPicture.setImageBitmap(bitmap);
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
        btnSave.setEnabled(false);
        btnAdd.setEnabled(false);
    }

    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public void startToPhoto() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //文件保存路径
            String imageRootPath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "data_collection";
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return;
            }

            File imageRootPathFile = new File(imageRootPath);
            if (!imageRootPathFile.exists() && !imageRootPathFile.mkdir()) {
                return;
            }

            //文件名称
            photoFileCachePath = getFileName(System.currentTimeMillis());/*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/
            ;
            //后缀
            //启动相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file = new File(photoFileCachePath)));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, 123);
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
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), DataListActivity.class);
                startActivity(intent);
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
        projectEntity.setImagePath(filePath);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            progressBar.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            //文件保存路径
            final String imageRootPath = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "data_collection";
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return;
            }

            File imageRootPathFile = new File(imageRootPath);
            if (!imageRootPathFile.exists() && !imageRootPathFile.mkdir()) {
                return;
            }

            final String rootFilePath = file.getAbsolutePath();
            final File rootFile = new File(rootFilePath);
            if (!rootFile.exists()) {
                return;
            }
            long time1 = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeFile(rootFilePath);
            long time2 = System.currentTimeMillis();
            Log.d("ddddddddddddd", time2 - time1 + "");
            ivPicture.setImageBitmap(bitmap);// 将图片显示在ImageView里
            tvHint.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            ivPicture.setVisibility(View.VISIBLE);
            final String fileName = getFileName(System.currentTimeMillis())/*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/;
            filePath = fileName;
            btnAdd.setEnabled(true);
            btnSave.setEnabled(true);
            saveImageThread = new SaveImageThread(bitmap, fileName, rootFile, updateImageHandler);
            saveImageThread.start();
        }
    }

    private String getFileName(long time) {
        final String imageRootPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection";
        File imageRootPathFile = new File(imageRootPath);
        if (!imageRootPathFile.exists() && !imageRootPathFile.mkdir()) {
            return null;
        }

        return imageRootPath + File.separator + formatDate(time) + File.separator + formatDate2(time) + ".png";

    }

    private static class UpdateImageHandler extends Handler {
        WeakReference<MainActivity> mainActivityWeakReference;

        UpdateImageHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mainActivityWeakReference.get().btnAdd.setEnabled(true);
            mainActivityWeakReference.get().btnSave.setEnabled(true);
        }
    }

    private static class SaveImageThread extends Thread {
        private WeakReference<Bitmap> bitmapWeakReference;
        private WeakReference<UpdateImageHandler> handlerWeakReference;
        private String fileName;
        private File rootFile;

        SaveImageThread(Bitmap bitmap, String fileName, File rootFile, UpdateImageHandler handler) {
            bitmapWeakReference = new WeakReference<Bitmap>(bitmap);
            handlerWeakReference = new WeakReference<UpdateImageHandler>(handler);
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
                    bitmap.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static String formatDate2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
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
}
