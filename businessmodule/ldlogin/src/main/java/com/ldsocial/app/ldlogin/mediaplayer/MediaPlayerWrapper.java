package com.ldsocial.app.ldlogin.mediaplayer;

import android.media.MediaPlayer;
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

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if (callbackListener != null) {
                callbackListener.onStateChanged(PLAYING);
            }
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (callbackListener != null) {
                callbackListener.onStateChanged(PAUSE);
            }
        }
    }

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
