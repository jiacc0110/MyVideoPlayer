package com.jiacc.myvedioplayer;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.MediaController;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyVedioView extends FrameLayout implements MediaController.MediaPlayerControl {
    private IMediaPlayer mMediaPlayer = null;
    private String mPath;
    private Map<String,String> mHeader;
    private AudioManager mAudioManager;

    public MyVedioView(Context context){
        super(context);
        init(context);
    }

    public MyVedioView( Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyVedioView( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context mContext;
    private void init(Context context){
        mContext = context;
        setBackgroundColor(Color.BLUE);
        createSurfaceView();

        mAudioManager = (AudioManager)mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        //mAudioFocusHelper = new AudioFocusHelper();
    }

    private SurfaceView mSurfaceView;
    private void createSurfaceView(){
       mSurfaceView = new SurfaceView(mContext);
       mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
           @Override
           public void surfaceCreated(SurfaceHolder holder) {
               if(mMediaPlayer!=null){
                   mMediaPlayer.setDisplay(holder);
               }
           }

           @Override
           public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

           }

           @Override
           public void surfaceDestroyed(SurfaceHolder holder) {

           }
       });

       LayoutParams layoutParams = new LayoutParams( LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT,
               Gravity.BOTTOM);
       addView(mSurfaceView,layoutParams);
    }

    private IMediaPlayer createPlayer(){
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"opensles",1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"overlay-format",IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzemaxduration", 100L);

        ijkMediaPlayer.setVolume(1.0f, 1.0f);

        return ijkMediaPlayer;
    }

    public void load(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = createPlayer();
        setListeners();
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(mPath),mHeader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
    }

    private void setListeners(){
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });

       mMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
           @Override
           public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

           }
       });

    }

    public void setPath(String path, Map<String,String> header){
        mPath = path;
        mHeader = header;
    }

    @Override
    public void start() {
       if(mMediaPlayer != null){
           mMediaPlayer.start();
       }
    }

    @Override
    public void pause() {
       if(mMediaPlayer != null){
           mMediaPlayer.pause();
       }
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
