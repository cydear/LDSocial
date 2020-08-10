package com.ldsocial.app.ldlogin.mediaplayer;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * @ClassName MediaPlayerThrowable
 * @Description: 播放器异常处理类
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 9:21 PM
 * @Version: 1.0
 */
public class MediaPlayerThrowable {
    private static final String TAG = "MediaPlayer.Log";

    public MediaPlayerThrowable() {

    }

    /**
     * 接管错误处理
     *
     * @param mp
     * @param what
     * @param extra
     */
    public void onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case -1004:
                Log.d(TAG, "MEDIA_ERROR_IO");
                break;
            case -1007:
                Log.d(TAG, "MEDIA_ERROR_MALFORMED");
                break;
            case 200:
                Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                break;
            case 100:
                Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                break;
            case -110:
                Log.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                break;
            case 1:
                Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                break;
            case -1010:
                Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED");
                break;
        }
        switch (extra) {
            case 800:
                Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING");
                break;
            case 702:
                Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                break;
            case 701:
                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                break;
            case 802:
                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                break;
            case 801:
                Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                break;
            case 1:
                Log.d(TAG, "MEDIA_INFO_UNKNOWN");
                break;
            case 3:
                Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                break;
            case 700:
                Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                break;
        }
    }
}
