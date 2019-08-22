package com.jiacc.myvedioplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingService extends Service implements FloatManager{

    private View displayView;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    public FloatingService() {
    }
    Intent curentIntent;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        curentIntent = intent;
        showFloating();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void showFloating() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)){
                //获取WindowService
                windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
                //新建悬浮窗
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                displayView = layoutInflater.inflate(R.layout.float_view,null);
                MyVedioView myVedioView = displayView.findViewById(R.id.myVideoView);
                myVedioView.setPath(curentIntent.getStringExtra("url"),null);
                myVedioView.load();
                layoutParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }

                layoutParams.format = PixelFormat.RGBA_8888;
                layoutParams.width = 500;
                layoutParams.height = 500;
                layoutParams.x = 300;
                layoutParams.y = 300;
                displayView.findViewById(R.id.myVideoView).setOnTouchListener(mFloatingOnTouchListener);
                windowManager.addView(displayView,layoutParams);

            }
        }
    }
    FloatingOnTouchListener mFloatingOnTouchListener = new FloatingOnTouchListener();


    @Override
    public void removeFloating() {
        windowManager.removeViewImmediate(displayView);
    }

    private class FloatingOnTouchListener implements View.OnTouchListener{
        private int x;
        private int y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;

                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(displayView, layoutParams);
            }

            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFloating();
    }
}
