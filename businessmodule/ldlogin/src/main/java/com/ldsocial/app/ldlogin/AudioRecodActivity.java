package com.ldsocial.app.ldlogin;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ldsocial.app.ldlogin.audio.AudioRecorder;
import com.ldsocial.app.ldlogin.mediaplayer.IPlayCallbackListener;
import com.ldsocial.app.ldlogin.mediaplayer.IPlayCallbackListenerAdapter;
import com.ldsocial.app.ldlogin.mediaplayer.IPlayer;
import com.ldsocial.app.ldlogin.mediaplayer.MediaPlayerWrapper;
import com.ldsocial.app.ldlogin.mediaplayer.MediaPlyerManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * @ClassName AudioRecodActivity
 * @Description: 音频录制
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 11:28 AM
 * @Version: 1.0
 */
public class AudioRecodActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStart;
    private Button btnPause;
    private Button btnStop;
    private Button btnContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);

        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnContinue = findViewById(R.id.btn_continue);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        checkPemrission();
    }

    /**
     * 权限校验
     */
    private void checkPemrission() {
        AndPermission.with(this)
                .runtime()
                .permission(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .start();
    }

    @Override
    public void onClick(View view) {
        int _id = view.getId();
        if (_id == R.id.btn_start) {
            startAudioRecord();
        } else if (_id == R.id.btn_pause) {
            pauseAudioRecord();
        } else if (_id == R.id.btn_stop) {
            stopAudioRecord();
        } else if (_id == R.id.btn_continue) {
            continueAudioRecord();
        }
    }

    /**
     * 开始录音
     */
    private void startAudioRecord() {
        AudioRecorder.getInstance().createDefaultAudioRecord("sign_20200810");
        AudioRecorder.getInstance().startAudioRecord(null);
    }

    /**
     * 暂停录音
     */
    private void pauseAudioRecord() {
        AudioRecorder.getInstance().pauseAudioRecord();
    }

    /**
     * 停止录音
     */
    private void stopAudioRecord() {
        AudioRecorder.getInstance().stopAudioRecord();
    }

    /**
     * 继续录音
     */
    private void continueAudioRecord() {
        //AudioRecorder.getInstance().startAudioRecord(null);
        //MediaPlyerManager.playMusic("https://cdn-friendship.1sapp.com/friendship/friendship_app/json/loading/sign_20200810.wav");
        final MediaPlayerWrapper player = new MediaPlayerWrapper();
        player.setPlayCallbackListener(new IPlayCallbackListenerAdapter() {
            @Override
            public void onStateChanged(int state) {
                if (state == IPlayer.LOAD_MEDIA_COMPLETE) {
                    player.play();
                }
            }
        });
        player.loadMediaSource("https://cdn-friendship.1sapp.com/friendship/friendship_app/json/loading/sign_20200810.wav");
        //player.play();
    }
}
