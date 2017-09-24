package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.utils.AppConstant;

import java.io.File;

public class ShowPicActivity extends Activity implements View.OnClickListener{

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
        tvRepeat.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        intent = getIntent();
        picWidth = intent.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = intent.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        img = findViewById(R.id.img);
//        Bitmap bitmap = BitmapFactory.decodeFile(intent.getStringExtra(AppConstant.KEY.IMG_PATH));
//        Bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / bitmap.getWidth(), true);
//        img.setImageURI(Uri.parse(getIntent().getStringExtra(AppConstant.KEY.IMG_PATH)));
        Uri uri = Uri.fromFile(new File(intent.getStringExtra(AppConstant.KEY.IMG_PATH)));
        img.setImageURI(uri);
//        img.setImageBitmap(bitmap);
        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
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
                break;
        }
    }
}
