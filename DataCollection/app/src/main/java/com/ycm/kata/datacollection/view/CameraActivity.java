package com.ycm.kata.datacollection.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.entity.ImageInfo;
import com.ycm.kata.datacollection.service.LocationService;
import com.ycm.kata.datacollection.utils.AppConstant;
import com.ycm.kata.datacollection.utils.CameraUtil;
import com.ycm.kata.datacollection.utils.CommonUtils;
import com.ycm.kata.datacollection.utils.SystemUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;

    //屏幕宽高
    private static int screenWidth;
    private static int screenHeight;
    private LinearLayout home_custom_top_relative;
    private ImageView camera_delay_time;
    private View homeCustom_cover_top_view;
    private View homeCustom_cover_bottom_view;
    private View home_camera_cover_top_view;
    private View home_camera_cover_bottom_view;
    private ImageView flash_light;
    private TextView camera_delay_time_text;
    private ImageView camera_square;
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    //延迟时间
    private int delay_time;
    private int delay_time_temp;
    private boolean isview = false;
    private boolean is_camera_delay;
    private ImageView camera_frontback;
    private ImageView camera_close;
    private RelativeLayout homecamera_bottom_relative;
    private ImageView img_camera;
    private int picHeight;
    private static String desFileName;
    private static UpdateImageHandler updateImageHandler;
    private SaveImageThread saveImageThread;
    private CameraActivity cameraActivity;
    private TextView tvAddress;
    private LocationService locationService;
    private String address;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        locationService = ((MyApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tvAddress.setText(msg.getData().getString("address"));
            }
        };
        cameraActivity = this;
        context = this;
        initView();
        initData();

    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);

        //关闭相机界面按钮
        camera_close = (ImageView) findViewById(R.id.camera_close);
        camera_close.setOnClickListener(this);

        //top 的view
        home_custom_top_relative = (LinearLayout) findViewById(R.id.home_custom_top_relative);
        home_custom_top_relative.setAlpha(0.5f);

        //前后摄像头切换
        camera_frontback = (ImageView) findViewById(R.id.camera_frontback);
        camera_frontback.setOnClickListener(this);

        //延迟拍照时间
        camera_delay_time = (ImageView) findViewById(R.id.camera_delay_time);
        camera_delay_time.setOnClickListener(this);

        //正方形切换
        camera_square = (ImageView) findViewById(R.id.camera_square);
        camera_square.setOnClickListener(this);

        //切换正方形时候的动画
        homeCustom_cover_top_view = findViewById(R.id.homeCustom_cover_top_view);
        homeCustom_cover_bottom_view = findViewById(R.id.homeCustom_cover_bottom_view);

        homeCustom_cover_top_view.setAlpha(0.5f);
        homeCustom_cover_bottom_view.setAlpha(0.5f);

        //拍照时动画
        home_camera_cover_top_view = findViewById(R.id.home_camera_cover_top_view);
        home_camera_cover_bottom_view = findViewById(R.id.home_camera_cover_bottom_view);
        home_camera_cover_top_view.setAlpha(1);
        home_camera_cover_bottom_view.setAlpha(1);

        //闪光灯
        flash_light = (ImageView) findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);
        tvAddress = findViewById(R.id.address);
        camera_delay_time_text = (TextView) findViewById(R.id.camera_delay_time_text);

        homecamera_bottom_relative = (RelativeLayout) findViewById(R.id.homecamera_bottom_relative);
    }

    private void initData() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        animHeight = (screenHeight - screenWidth - menuPopviewHeight - SystemUtils.dp2px(context, 44)) / 2;
        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
        RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        homecamera_bottom_relative.setLayoutParams(bottomParam);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case AppConstant.WHAT.SUCCESS:
                    if (delay_time > 0) {
                        camera_delay_time_text.setText("" + delay_time);
                    }

                    try {
                        if (delay_time == 0) {
                            captrue();
                            is_camera_delay = false;
                            camera_delay_time_text.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        return;
                    }

                    break;

                case AppConstant.WHAT.ERROR:
                    is_camera_delay = false;
                    break;

            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                if (isview) {
                    if (delay_time == 0) {
                        switch (light_num) {
                            case 0:
                                //关闭
                                CameraUtil.getInstance().turnLightOff(mCamera);
                                break;
                            case 1:
                                CameraUtil.getInstance().turnLightOn(mCamera);
                                break;
                            case 2:
                                //自动
                                CameraUtil.getInstance().turnLightAuto(mCamera);
                                break;
                        }
                        captrue();
                    } else {
                        camera_delay_time_text.setVisibility(View.VISIBLE);
                        camera_delay_time_text.setText(String.valueOf(delay_time));
                        is_camera_delay = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (delay_time > 0) {
                                    //按秒数倒计时
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        mHandler.sendEmptyMessage(AppConstant.WHAT.ERROR);
                                        return;
                                    }
                                    delay_time--;
                                    mHandler.sendEmptyMessage(AppConstant.WHAT.SUCCESS);
                                }
                            }
                        }).start();
                    }
                    isview = false;
                }
                break;

            case R.id.camera_square:
                if (index == 0) {
                    camera_square_0();
                } else if (index == 1) {
                    camera_square_1();
                }
                break;

            //前后置摄像头拍照
            case R.id.camera_frontback:
                switchCamera();
                break;

            //退出相机界面 释放资源
            case R.id.camera_close:
                if (is_camera_delay) {
                    Toast.makeText(CameraActivity.this, "正在拍照请稍后...", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
                break;

            //闪光灯
            case R.id.flash_light:
                if (mCameraId == 1) {
                    //前置
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //自动
                        light_num = 2;
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_auto);
                        break;
                    case 2:
                        //关闭
                        light_num = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                        break;
                }

                break;

            //延迟拍照时间
            case R.id.camera_delay_time:
                switch (delay_time) {
                    case 0:
                        delay_time = 3;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_3);
                        break;

                    case 3:
                        delay_time = 5;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_5);
                        break;

                    case 5:
                        delay_time = 10;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_10);
                        break;

                    case 10:
                        delay_time = 0;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_0);
                        break;

                }
        }
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 正方形拍摄
     */
    public void camera_square_0() {
        camera_square.setImageResource(R.drawable.btn_camera_size1_n);

        //属性动画
        ValueAnimator anim = ValueAnimator.ofInt(0, animHeight);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, SystemUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }

        });
        anim.start();

        homeCustom_cover_top_view.bringToFront();
        home_custom_top_relative.bringToFront();
        homeCustom_cover_bottom_view.bringToFront();
        index++;
    }

    /**
     * 长方形方形拍摄
     */
    public void camera_square_1() {
        camera_square.setImageResource(R.drawable.btn_camera_size2_n);

        ValueAnimator anim = ValueAnimator.ofInt(animHeight, 0);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                RelativeLayout.LayoutParams Params = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                Params.setMargins(0, SystemUtils.dp2px(context, 44), 0, 0);
                homeCustom_cover_top_view.setLayoutParams(Params);

                RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(screenWidth, currentValue);
                bottomParams.setMargins(0, screenHeight - menuPopviewHeight - currentValue, 0, 0);
                homeCustom_cover_bottom_view.setLayoutParams(bottomParams);
            }
        });
        anim.start();
        index = 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);
                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);
                if (index == 1) {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, animHeight + SystemUtils.dp2px(context, 44), screenWidth, screenWidth);
                } else {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, 0, screenWidth, screenWidth * 4 / 3);
                }

//                String img_path = CommonUtils.getImageFilePath(System.currentTimeMillis());


//                final String rootFilePath = file.getPath();
//                final File rootFile = new File(rootFilePath);
//                if (!rootFile.exists()) {
//                    progressBar.setVisibility(View.GONE);
//                    tvHint.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(), "拍照失败", Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                Bitmap bitmap = BitmapFactory.decodeFile(rootFilePath);
                desFileName = CommonUtils.getImageFilePath(System.currentTimeMillis()); /*getFileName(System.currentTimeMillis())*//*imageRootPath + File.separator + formatDate2(System.currentTimeMillis()) + ".png"*/
                saveBitmap = CameraUtil.getInstance().drawTextToRightBottom(getBaseContext(), saveBitmap, address, 12, getResources().getColor(R.color.Gray), 30, 30);
                saveImage(saveBitmap, desFileName);


//                ImageInfo imageInfo = new ImageInfo(desFileName, screenWidth, picHeight);
//                updateImageHandler = new UpdateImageHandler(cameraActivity, imageInfo);
//                saveImageThread = new SaveImageThread(saveBitmap, desFileName, updateImageHandler, cameraActivity, imageInfo);
//                saveImageThread.start();
                Intent intent = new Intent();
                intent.setClass(cameraActivity, ShowPicActivity.class);
                intent.putExtra(AppConstant.KEY.IMG_PATH, desFileName /*imageInfoWeakReference.get().getPath()*/);
                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth /*imageInfoWeakReference.get().getWidth()*/);
                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, screenHeight /*imageInfoWeakReference.get().getHeight()*/);
//                intent.putExtra(AppConstant.KEY.PIC_PRE, saveBitmap /*imageInfoWeakReference.get().getHeight()*/);
                cameraActivity.startActivity(intent);
                cameraActivity.finish();

//                BitmapUtils.saveJPGE_After(context, saveBitmap, img_path, 100);

//                if (!bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
//
//                if (!saveBitmap.isRecycled()) {
//                    saveBitmap.recycle();
//                }

//                Intent intent = new Intent();
//                intent.putExtra(AppConstant.KEY.IMG_PATH, desFileName);
//                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth);
//                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
//                setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
//                finish();


                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
//                Log.d("bitmapWidth==", bitmap.getWidth() + "");
//                Log.d("bitmapHeight==", bitmap.getHeight() + "");
            }
        });
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 480);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Camera.Size pictureSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 480);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        camera.setParameters(parameters);

        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */

        picHeight = (screenWidth * pictureSize.width) / pictureSize.height;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictureSize.width) / pictureSize.height);
        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
        //params.gravity = Gravity.CENTER;
        surfaceView.setLayoutParams(params);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    private static class UpdateImageHandler extends Handler {
        WeakReference<CameraActivity> mainActivityWeakReference;
        WeakReference<Bitmap> bitmapWeakReference;
        WeakReference<ImageInfo> imageInfoWeakReference;

        UpdateImageHandler(CameraActivity mainActivity, Bitmap bitmap) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            bitmapWeakReference = new WeakReference<>(bitmap);
        }

        UpdateImageHandler(Bitmap bitmap) {
            bitmapWeakReference = new WeakReference<>(bitmap);
        }

        UpdateImageHandler(CameraActivity mainActivity, ImageInfo imageInfo) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
            imageInfoWeakReference = new WeakReference<>(imageInfo);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            intent.setClass(mainActivityWeakReference.get(), ShowPicActivity.class);
            intent.putExtra(AppConstant.KEY.IMG_PATH, desFileName /*imageInfoWeakReference.get().getPath()*/);
            intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth /*imageInfoWeakReference.get().getWidth()*/);
            intent.putExtra(AppConstant.KEY.PIC_HEIGHT, screenHeight /*imageInfoWeakReference.get().getHeight()*/);
            mainActivityWeakReference.get().startActivity(intent);
            mainActivityWeakReference.get().finish();
//            Bitmap bitmap = bitmapWeakReference.get();
//            DisplayMetrics dm = new DisplayMetrics();
//            mainActivityWeakReference.get().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int screenWidth = dm.widthPixels;
//            mainActivityWeakReference.get().showBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
//            mainActivityWeakReference.get().ivPicture.setImageBitmap(mainActivityWeakReference.get().showBitmap);// 将图片显示在ImageView里
//            mainActivityWeakReference.get().tvHint.setVisibility(View.GONE);
//            mainActivityWeakReference.get().progressBar.setVisibility(View.GONE);
//            mainActivityWeakReference.get().ivPicture.setVisibility(View.VISIBLE);
//            mainActivityWeakReference.get().btnAdd.setEnabled(true);
//            mainActivityWeakReference.get().btnSave.setEnabled(true);
//            CommonUtils.destroyBitmap(bitmap);
        }
    }

    private static class SaveImageThread extends Thread {
        private WeakReference<Bitmap> bitmapWeakReference;
        private WeakReference<UpdateImageHandler> handlerWeakReference;
        WeakReference<CameraActivity> mainActivityWeakReference;
        WeakReference<ImageInfo> imageInfoWeakReference;
        private String fileName;

        SaveImageThread(Bitmap bitmap, String fileName, UpdateImageHandler handler) {
            bitmapWeakReference = new WeakReference<>(bitmap);
            handlerWeakReference = new WeakReference<>(handler);
            this.fileName = fileName;
        }

        SaveImageThread(Bitmap bitmap, String fileName,
                        UpdateImageHandler handler,
                        CameraActivity cameraActivity, ImageInfo imageInfo) {
            bitmapWeakReference = new WeakReference<>(bitmap);
            handlerWeakReference = new WeakReference<>(handler);
            mainActivityWeakReference = new WeakReference<>(cameraActivity);
            imageInfoWeakReference = new WeakReference<>(imageInfo);
            this.fileName = fileName;
        }

        @Override
        public void run() {
            Bitmap bitmap = bitmapWeakReference.get();
//            bitmap = getZoomImage(bitmap, 650);
            FileOutputStream b = null;
            ByteArrayOutputStream outputStream = bitmapToByteArray(bitmap, false);
            try {
                File imageFile = new File(fileName);
                if (!imageFile.exists() && !imageFile.createNewFile()) {
                    return;
                }
                b = new FileOutputStream(fileName);
                b.write(outputStream.toByteArray());
//                handlerWeakReference.get().sendEmptyMessage(123);

                Intent intent = new Intent();
                intent.setClass(mainActivityWeakReference.get(), ShowPicActivity.class);
                intent.putExtra(AppConstant.KEY.IMG_PATH, imageInfoWeakReference.get().getPath());
                intent.putExtra(AppConstant.KEY.PIC_WIDTH, imageInfoWeakReference.get().getWidth());
                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, imageInfoWeakReference.get().getHeight());
                mainActivityWeakReference.get().startActivity(intent);
                mainActivityWeakReference.get().finish();
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


    private void saveImage(Bitmap bitmap, String fileName) {
//        bitmap = getZoomImage(bitmap, 650);
        FileOutputStream b = null;
        ByteArrayOutputStream outputStream = bitmapToByteArray(bitmap, false);
        try {
            File imageFile = new File(fileName);
            if (!imageFile.exists() && !imageFile.createNewFile()) {
                return;
            }
            b = new FileOutputStream(fileName);
            b.write(outputStream.toByteArray());
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

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            address = location.getAddrStr();
            Bundle bundle = new Bundle();
            bundle.putString("address", address);
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };
}
