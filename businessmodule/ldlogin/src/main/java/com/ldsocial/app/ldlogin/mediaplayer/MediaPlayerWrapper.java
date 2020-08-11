package com.ldsocial.app.ldlogin.mediaplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName MediaPlayerWrapper
 * @Description: 播放器封装
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 9:04 PM
 * @Version: 1.0
 */
public class MediaPlayerWrapper implements IPlayer, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    /**
     * 播放进度
     */
    private final static int UPDATE_MEDIA_PROGRESS = 1000;
    /**
     * 播放器对象
     */
    private MediaPlayer mediaPlayer;
    /**
     * 加载的资源地址
     */
    private String mediaUrl;
    /**
     * 播放器回调监听
     */
    private IPlayCallbackListener callbackListener;
    /**
     * 播放器播放失败处理类
     */
    private MediaPlayerThrowable throwable;
    /**
     * 显示播放进度
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_MEDIA_PROGRESS) {
                updatePlayerProgress();
                startMediaPlayerCountDownTime();
            }
        }
    };

    /**
     * 播放器倒计时
     */
    private void startMediaPlayerCountDownTime() {
        if (mediaPlayer != null && isPlaying()) {
            Message msg = Message.obtain();
            msg.what = UPDATE_MEDIA_PROGRESS;
            mHandler.sendMessageDelayed(msg, 100);
        }
    }

    /**
     * 更新播放进度
     */
    private void updatePlayerProgress() {
        if (mediaPlayer != null && isPlaying() && callbackListener != null) {
            Log.d("MediaPlayer.Log", "getCurrentPosition=>" + mediaPlayer.getCurrentPosition());
            callbackListener.onPlayerUpdateProgress(mediaPlayer.getCurrentPosition());
        }
    }

    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //失败信息处理类
            throwable = new MediaPlayerThrowable();
            //设置播放器播放完成监听
            mediaPlayer.setOnCompletionListener(this);
            //监听流媒体是否装载完成
            mediaPlayer.setOnPreparedListener(this);
            //监听媒体的错误信息
            mediaPlayer.setOnErrorListener(this);
        }
    }

    /**
     * 加载媒体资源文件
     *
     * @param sourceUrl
     */
    @Override
    public void loadMediaSource(String sourceUrl) {
        if (TextUtils.isEmpty(sourceUrl)) {
            Log.d("MediaPlayer.Log", "播放地址不能为空");
            return;
        }
        if (callbackListener != null) {
            callbackListener.onStateChanged(LOADING_MEDIA);
        }
        this.mediaUrl = sourceUrl;
        initMediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mediaUrl);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放播放器资源
     */
    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 播放器是否正在播放
     *
     * @return
     */
    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 播放
     */
    @Override
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            //开启计时器
            startMediaPlayerCountDownTime();
            if (callbackListener != null) {
                callbackListener.onStateChanged(PLAYING);
            }
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (callbackListener != null) {
                callbackListener.onStateChanged(PAUSE);
            }
        }
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        if (mediaPlayer != null) {
            loadMediaSource(mediaUrl);
            if (callbackListener != null) {
                callbackListener.onStateChanged(PLAYER_RESET);
            }
        }
    }

    /**
     * 媒体装载完成后操作
     */
    @Override
    public void prepareCompleted() {
        //媒体装载完成后操作
        int duration = mediaPlayer.getDuration();
        if (callbackListener != null) {
            callbackListener.onTotalDuration(duration);
            callbackListener.onStateChanged(LOAD_MEDIA_COMPLETE);
        }
    }

    /**
     * 跳转进度
     *
     * @param position
     */
    @Override
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    /**
     * 媒体装载完成回调
     *
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        prepareCompleted();
    }

    /**
     * 播放器播放完成监听
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (callbackListener != null) {
            callbackListener.onStateChanged(PLAYING_COMPLETE);
            callbackListener.onPlayCompleted();
        }
    }

    /**
     * 播放器遇到错误监听
     *
     * @param mediaPlayer
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        if (callbackListener != null) {
            callbackListener.onStateChanged(PLAYER_ERROR);
        }
        if (throwable != null) {
            throwable.onError(mediaPlayer, i, i1);
        }
        return false;
    }

    /**
     * 设置播放器回调监听
     *
     * @param callbackListener
     */
    public void setPlayCallbackListener(IPlayCallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }
}
