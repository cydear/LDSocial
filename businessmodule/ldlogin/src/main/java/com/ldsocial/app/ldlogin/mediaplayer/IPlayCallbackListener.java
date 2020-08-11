package com.ldsocial.app.ldlogin.mediaplayer;

/**
 * @ClassName IPlayCallbackListener
 * @Description: 播放回调监听
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 8:57 PM
 * @Version: 1.0
 */
public interface IPlayCallbackListener {
    /**
     * 播放总时长
     *
     * @param duration
     */
    void onTotalDuration(int duration);

    /**
     * 播放状态改变
     *
     * @param state
     */
    void onStateChanged(int state);

    /**
     * 播放完成
     */
    void onPlayCompleted();

    /**
     * 更新播放进度
     *
     * @param currentDuration
     */
    void onPlayerUpdateProgress(int currentDuration);
}
