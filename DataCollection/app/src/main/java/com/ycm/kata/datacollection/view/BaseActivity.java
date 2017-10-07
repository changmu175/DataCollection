package com.ycm.kata.datacollection.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ycm.kata.datacollection.R;
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

    public void showConfirmCancelDialog(String title, String confirmTitle, String cancelTitle, String detail, final View.OnClickListener confirmListener, final View.OnClickListener cancelListener) {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        View layout = getLayoutInflater().inflate(R.layout.dialog_confirmcancel, null);
        // set the dialog title
        TextView titleView = (TextView) layout.findViewById(R.id.dialog_confirmcancel_title);
        titleView.setText(title);
        TextView confirmView = (TextView) layout.findViewById(R.id.dialog_confirmcancel_confirm);
        if (confirmTitle != null) {
            confirmView.setText(confirmTitle);
        }

        TextView cancelView = (TextView) layout.findViewById(R.id.dialog_confirmcancel_cancel);
        if (!TextUtils.isEmpty(cancelTitle)) {
            cancelView.setText(cancelTitle);
        }

        if (detail != null) {
            TextView textView = (TextView) layout.findViewById(R.id.dialog_confirmcancel_detail);
            textView.setVisibility(View.VISIBLE);
            textView.setText(detail);
        }
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
                dialog.cancel();
            }
        });

        dialog.setCancelable(false);
        dialog.setContentView(layout);
        dialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0];
            int top = l[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getRawX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
