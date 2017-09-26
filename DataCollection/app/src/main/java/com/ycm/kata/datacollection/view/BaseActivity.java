package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ycm.kata.datacollection.event.EventManager;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by changmuyu on 2017/9/25.
 * Description:
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.GetInstance().getEventBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.GetInstance().getEventBus().unregister(this);
    }

    @Subscribe
    public void onEvent(String event) {
        Log.d("Event", "default handler");
    }
}
