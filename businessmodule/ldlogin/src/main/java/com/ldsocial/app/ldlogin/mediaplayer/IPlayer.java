package com.ldsocial.app.ldlogin.mediaplayer;

/**
 * @ClassName IPlayer
 * @Description: player
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 9:00 PM
 * @Version: 1.0
 */
public interface IPlayer {
    /**
     * 正在播放
     */
    int PLAYING = 0;
    /**
     * 暂停播放
     */
    int PAUSE = 1;
    /**
     * 播放器重置
     */
    int PLAYER_RESET = 2;
    /**
     * 播放完成
     */
    int PLAYING_COMPLETE = 3;
    /**
     * 媒体资源加载完成
     */
    int LOAD_MEDIA_COMPLETE = 4;
    /**
     * 媒体资源加载中
     */
    int LOADING_MEDIA = 5;
    /**
     * 播放器播放失败
     */
    int PLAYER_ERROR = -1;

    /**
     * 加载媒体资源
     *
     * @param sourceUrl
     */
    void loadMediaSource(String sourceUrl);

    /**
     * 资源释放
     */
    void release();

    /**
     * 判断是否正在播放
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 开始播放
     */
    void play();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 重置播放器
     */
    void reset();

    /**
     * 资源加载完成
     */
    void prepareCompleted();

    /**
     * 滑动到某个位置
     *
     * @param position
     */
    void seekTo(int position);
}
