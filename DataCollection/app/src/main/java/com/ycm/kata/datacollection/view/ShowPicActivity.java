package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.event.EventManager;
import com.ycm.kata.datacollection.event.PhotoEvent;
import com.ycm.kata.datacollection.utils.AppConstant;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public class ShowPicActivity extends BaseActivity implements View.OnClickListener{

    private ImageView img;
    private int picWidth;
    private int picHeight;
    private TextView tvRepeat;
    private TextView tvConfirm;
    private Intent intent;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);

        tvConfirm = findViewById(R.id.confirm);
        tvRepeat = findViewById(R.id.repeat);
        img = findViewById(R.id.img);
        tvRepeat.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        intent = getIntent();
        picWidth = intent.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = intent.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);

//        Bitmap bitmap = BitmapFactory.decodeFile(intent.getStringExtra(AppConstant.KEY.IMG_PATH));
//        Bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
//        img.setImageURI(Uri.parse(getIntent().getStringExtra(AppConstant.KEY.IMG_PATH)));
        imagePath = intent.getStringExtra(AppConstant.KEY.IMG_PATH);
        Uri uri = Uri.fromFile(new File(imagePath));
        img.setImageURI(uri);
//        img.setImageBitmap(bitmap);
//        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat:
                Intent i = new Intent();
                i.setClass(this, CameraActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.confirm:
//                Intent ii = intent;
//                ii.putExtra(AppConstant.KEY.IMG_PATH, imagePath);
//                setResult(123, ii);
                PhotoEvent photoEvent = new PhotoEvent();
                photoEvent.setImagePath(imagePath);
                EventManager.GetInstance().getEventBus().post(photoEvent);
//                EventBus.getDefault().post(photoEvent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
