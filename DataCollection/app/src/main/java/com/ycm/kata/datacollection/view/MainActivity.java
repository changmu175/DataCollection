package com.ycm.kata.datacollection.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.event.PhotoEvent;
import com.ycm.kata.datacollection.model.LocationInfoDao;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.LocationInfo;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.service.LocationService;
import com.ycm.kata.datacollection.utils.ActivityStack;
import com.ycm.kata.datacollection.utils.CameraUtil;
import com.ycm.kata.datacollection.utils.CommonUtils;
import com.ycm.kata.datacollection.utils.PermissionUtils;
import com.ycm.kata.datacollection.utils.SharePreferenceUtil;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private String photoFileCachePath = "";
    private EditText etDate;
    private EditText etName;
    private EditText etProject;
    private EditText etBlock;
    private EditText etPile;
    private EditText etRemark;
    private EditText etDefect;
    private ImageView ivPicture;
    private ImageView ivCamera;
    private ImageView ivList;
    private RelativeLayout llTakePhoto;
    private ProgressBar progressBar;
    private Button btnSave;
    private Button btnAdd;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private String dateStr;
    private long time;
    private String filePath = "";
    private Uri imageUri = null;
    private File file;
    private static UpdateImageHandler updateImageHandler;
    private SaveImageThread saveImageThread;
    private LocationService locationService;
    private String address;
    //    private LocationInfoDao locationInfoDao;
    private String pileContent = "";
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStack.getInstanse().pushActivity(this);
        sharePreferenceUtil = SharePreferenceUtil.getInstance("address", this);
        time = System.currentTimeMillis();
        dateStr = CommonUtils.formatDate(time);
//        locationInfoDao = MyApplication.getInstances().getDaoSession().getLocationInfoDao();
        locationService = ((MyApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        initView();

        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        if (!(PermissionUtils.checkPermission(this, PermissionUtils.CODE_CAMERA) == PackageManager.PERMISSION_GRANTED)
                || !(PermissionUtils.checkPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                || !(PermissionUtils.checkPermission(this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || !(PermissionUtils.checkPermission(this, PermissionUtils.CODE_ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestPermission();
        }

    }

    private void initView() {
        etDate = findViewById(R.id.date_et);
        etDate.setText(dateStr);
        etDate.setEnabled(false);
        etName = findViewById(R.id.name_et);
        etProject = findViewById(R.id.project_et);
        etBlock = findViewById(R.id.block_et);
        etPile = findViewById(R.id.pile_et);
        etPile.addTextChangedListener(new PileEditTextChangeWatcher());
        etPile.setOnFocusChangeListener(this);
        etRemark = findViewById(R.id.remark_et);
        etDefect = findViewById(R.id.defects_et);
        llTakePhoto = findViewById(R.id.take_photo);
        llTakePhoto.setOnClickListener(this);
        ivPicture = findViewById(R.id.image_iv);
        ivPicture.setEnabled(false);
        ivCamera = findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        ivList = findViewById(R.id.list_btn);
        ivList.setOnClickListener(this);
        btnSave = findViewById(R.id.save_btn);
        btnSave.setOnClickListener(this);
        btnAdd = findViewById(R.id.add_btn);
        btnAdd.setOnClickListener(this);
    }

    public void startToPhoto() {
        //启动相机拍照
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //文件名称
            photoFileCachePath = CommonUtils.getImageFilePath(System.currentTimeMillis());
            //启动相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(photoFileCachePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, 123);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.destroyBitmap(showBitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
                CameraUtil.getInstance().camera(this);
                break;
            case R.id.take_photo:
                if (imageUri == null) {
                    return;
                }
                Intent ii = new Intent();
                ii.setData(imageUri);
                ii.setClass(this, ShowPicActivity.class);
                startActivity(ii);
                break;
            case R.id.save_btn:
                projectEntity = getProjectEntity();
                if (!isLegal(projectEntity)) {
                    Toast.makeText(getBaseContext(), "数据不能全为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (insert(projectEntity) != -1) {
                    Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), DataListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.add_btn:
                if (projectEntity == null) {
                    projectEntity = getProjectEntity();
                    if (!isLegal(projectEntity)) {
                        Toast.makeText(getBaseContext(), "数据不能全为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (insert(projectEntity) != -1) {
                        Toast.makeText(getBaseContext(), "新增成功", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                ProjectEntity pro = getProjectEntity();
                if (!isLegal(pro)) {
                    Toast.makeText(getBaseContext(), "数据不能全为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pro.equals(projectEntity)) {
                    projectEntity = pro;
                    if (insert(projectEntity) != -1) {
                        Toast.makeText(getBaseContext(), "新增成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "你没做任何改变", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.list_btn:
                Intent intent1 = new Intent();
                intent1.setClass(this, DataListActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    public boolean isLegal(ProjectEntity projectEntity) {
        return !(TextUtils.isEmpty(projectEntity.getPilNo())
                && TextUtils.isEmpty(projectEntity.getBlock())
                && TextUtils.isEmpty(projectEntity.getProjectName())
                && TextUtils.isEmpty(projectEntity.getRemark())
                && TextUtils.isEmpty(projectEntity.getUnitEngineering())
                && TextUtils.isEmpty(projectEntity.getDefects()));
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

    private long insert(ProjectEntity projectEntity) {
        if (projectEntityDao == null) {
            return -1;
        }
        return projectEntityDao.insert(projectEntity);
    }

    private void update(ProjectEntity projectEntity) {
        if (projectEntityDao == null) {
            return;
        }
        projectEntityDao.update(projectEntity);
    }

    private ProjectEntity getP(long id) {
        if (projectEntityDao == null) {
            return null;
        }

        if (id == -1) {
            return null;
        }
        return projectEntityDao.load(id);
    }

    private class PileEditTextChangeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == null) {
                return;
            }
            pileContent = editable.toString();
        }
    }

    @Subscribe
    public void onEventMainThread(PhotoEvent photoEvent) {
        filePath = photoEvent.getImagePath();
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        imageUri = Uri.fromFile(new File(filePath));
        ivPicture.setVisibility(View.VISIBLE);
        ivPicture.setImageURI(imageUri);
        ivPicture.setEnabled(true);
    }

    private Bitmap showBitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (file == null) {
                Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            final String rootFilePath = file.getPath();
            final File rootFile = new File(rootFilePath);
            if (!rootFile.exists()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
                return;
            }

//            Bitmap bitmap = BitmapFactory.decodeFile(rootFilePath);
            final String desFileName = CommonUtils.getImageFilePath(System.currentTimeMillis()); /*getFileName(System.currentTimeMillis())*//*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/
//            filePath = desFileName;

            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.isKeepSampling = true;
            options.outfile = desFileName;

            Tiny.getInstance().source(rootFilePath).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap, String outfile) {
                    //return the compressed file path and bitmap object
                    if (isSuccess) {
                        progressBar.setVisibility(View.GONE);
                        ivPicture.setImageBitmap(bitmap);
                        ivPicture.setVisibility(View.VISIBLE);
                        filePath = outfile;
                    } else {
                        Toast.makeText(getBaseContext(), "压缩图片出错", Toast.LENGTH_SHORT).show();
                    }
                }
            });


//            updateImageHandler = new UpdateImageHandler(this, desFileName);
//            saveImageThread = new SaveImageThread(bitmap, desFileName, rootFile, updateImageHandler);
//            saveImageThread.start();
        }

//        if (requestCode == 0x3) {
//            if (data != null) {
//                progressBar.setVisibility(View.VISIBLE);w
//                tvHint.setVisibility(View.GONE);
//                //文件保存路径
//                final String imageRootPath = Environment.getExternalStorageDirectory().getPath()
//                        + File.separator + "data_collection";
//                if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                    return;
//                }
//
//                File imageRootPathFile = new File(imageRootPath);
//                if (!imageRootPathFile.exists() && !imageRootPathFile.mkdir()) {
//                    return;
//                }
//
////                final String rootFilePath = file.getPath();
////                final File rootFile = new File(rootFilePath);
////                if (!rootFile.exists()) {
////                    return;
////                }
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = bundle.getParcelable("data");
//                ivPicture.setImageBitmap(bitmap);// 将图片显示在ImageView里
//                tvHint.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                ivPicture.setVisibility(View.VISIBLE);
//                final String fileName = getFileName(System.currentTimeMillis())/*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/;
//                filePath = fileName;
//                btnAdd.setEnabled(true);
//                btnSave.setEnabled(true);
//                saveImageThread = new SaveImageThread(bitmap, fileName, null, updateImageHandler);
//                saveImageThread.start();
//            } else {
//                return;
//            }
//        }
    }

    String tempContent;

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.pile_et:
                if (!b) {
                    tempContent = pileContent;
                    etPile.setText(CommonUtils.combinationStr(pileContent));
                } else {
                    etPile.setText(tempContent);
                }
                break;
        }
    }

    private static class UpdateImageHandler extends Handler {
        WeakReference<MainActivity> mainActivityWeakReference;
        WeakReference<Bitmap> bitmapWeakReference;
        String path;

        UpdateImageHandler(MainActivity mainActivity, Bitmap bitmap) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            bitmapWeakReference = new WeakReference<>(bitmap);
        }

        UpdateImageHandler(MainActivity mainActivity, String path) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            this.path = path;
        }

        UpdateImageHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Bitmap bitmap = bitmapWeakReference.get();
            DisplayMetrics dm = new DisplayMetrics();
            mainActivityWeakReference.get().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
//            mainActivityWeakReference.get().showBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
            Uri uri = Uri.fromFile(new File(path));
            mainActivityWeakReference.get().ivPicture.setImageURI(uri);
//            mainActivityWeakReference.get().ivPicture.setImageBitmap(mainActivityWeakReference.get().showBitmap);// 将图片显示在ImageView里
            mainActivityWeakReference.get().progressBar.setVisibility(View.GONE);
            mainActivityWeakReference.get().ivPicture.setVisibility(View.VISIBLE);
//            CommonUtils.destroyBitmap(bitmap);
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


    public void requestPermission() {
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

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            address = location.getAddrStr();
            sharePreferenceUtil.addData("address", address);
        }
    };

}
