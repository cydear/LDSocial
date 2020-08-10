package com.ldsocial.app.ldlogin.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @ClassName MediaManager
 * @Description: MediaPlayer
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 8:39 PM
 * @Version: 1.0
 */
public class MediaPlyerManager {
    public static void playMusic(String url) {
        try {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
