package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.view.UCropView;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.event.EventManager;
import com.ycm.kata.datacollection.event.PhotoEvent;
import com.ycm.kata.datacollection.utils.ActivityStack;
import com.ycm.kata.datacollection.utils.AppConstant;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public class ShowPicActivity extends BaseActivity implements View.OnClickListener {
    private int picWidth;
    private int picHeight;
    private String imagePath;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        ActivityStack.getInstanse().pushActivity(this);
        TextView tvConfirm = findViewById(R.id.confirm);
        TextView tvRepeat = findViewById(R.id.repeat);
        ImageView img = findViewById(R.id.img);
        tvRepeat.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        Intent intent = getIntent();
        imageUri = intent.getData();
        imagePath = imageUri.getPath();
        picWidth = intent.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = intent.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        img.setImageURI(imageUri);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(imageUri.getPath()).getAbsolutePath(), options);
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
                PhotoEvent photoEvent = new PhotoEvent();
                photoEvent.setImagePath(imagePath);
                EventManager.GetInstance().getEventBus().post(photoEvent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, ShowPicActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
