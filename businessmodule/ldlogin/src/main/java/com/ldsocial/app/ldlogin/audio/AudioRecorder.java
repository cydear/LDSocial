package com.ldsocial.app.ldlogin.audio;

import android.media.AudioRecord;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AudioRecorder
 * @Description: 录音工具
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 3:02 PM
 * @Version: 1.0
 */
public class AudioRecorder {
    private final static String TAG = "Record.Log";
    /**
     * 音频输入-麦克风
     */
    private final static int AUDIO_INPUT = GlobalConfig.AUDIO_INPUT;
    /**
     * 采用频率
     * 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     * 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
     */
    private final static int AUDIO_SIMPLE_RATE = GlobalConfig.SAMPLE_RATE_INHZ;
    /**
     * 声道->单声道
     */
    private final static int AUDIO_CHANNEL = GlobalConfig.CHANNEL_CONFIG;
    /**
     * 编码
     */
    private final static int AUDIO_ENCODING = GlobalConfig.AUDIO_FORMAT;
    /**
     * 缓存区字节大小
     */
    private int mBufferSize = 0;
    /**
     * 录音对象
     */
    private AudioRecord mAudioRecord;
    /**
     * 录音状态
     */
    private int mAudioStatus = AudioStatus.STATUS_NO_READY.getCode();
    /**
     * 录音文件名
     */
    private String mAudioFileName;

    private AudioRecorder() {

    }

    private static class SingleHolder {
        private static AudioRecorder INSTANCE = new AudioRecorder();
    }

    public static AudioRecorder getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 创建录音对象
     *
     * @param fileName
     * @param audioSource
     * @param simpleRate
     * @param channel
     * @param audioFormat
     */
    public void createAudioRecord(String fileName, int audioSource, int simpleRate, int channel, int audioFormat) {
        //获得缓冲区字节大小
        mBufferSize = AudioRecord.getMinBufferSize(simpleRate, channel, channel);
        mAudioRecord = new AudioRecord(audioSource, simpleRate, channel, audioFormat, mBufferSize);
        this.mAudioFileName = fileName;
    }

    /**
     * 创建默认的录音文件
     *
     * @param fileName
     */
    public void createDefaultAudioRecord(String fileName) {
        //获得缓冲区字节大小
        mBufferSize = AudioRecord.getMinBufferSize(AUDIO_SIMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        mAudioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SIMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, mBufferSize);
        this.mAudioFileName = fileName;
        this.mAudioStatus = AudioStatus.STATUS_READY.getCode();
    }

    /**
     * 开始录音
     *
     * @param listener
     */
    public void startAudioRecord(final RecordStreamListener listener) {
        if (mAudioStatus == AudioStatus.STATUS_NO_READY.getCode() || TextUtils.isEmpty(mAudioFileName)) {
            throw new IllegalStateException("录音工具尚未初始化，清检查是否禁止了录音权限.");
        }
        if (mAudioStatus == AudioStatus.STATUS_START.getCode()) {
            throw new IllegalStateException("正在录音");
        }
        Log.d(TAG, "====startRecord====");
        //开始录音
        mAudioRecord.startRecording();
        mAudioStatus = AudioStatus.STATUS_START.getCode();
        //将录音文件写入文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToPcmFile(listener);
            }
        }).start();
    }

    /**
     * 暂停录音
     */
    public void pauseAudioRecord() {
        Log.d(TAG, "======pauseAudioRecord======");
        if (mAudioStatus == AudioStatus.STATUS_START.getCode()) {
            mAudioRecord.stop();
            mAudioStatus = AudioStatus.STATUS_PAUSE.getCode();
        } else {
            Log.d(TAG, "目前没有再录音");
        }
    }

    /**
     * 停止录音
     */
    public void stopAudioRecord() {
        Log.d(TAG, "======stopAudioRecord=======");
        if (mAudioStatus == AudioStatus.STATUS_START.getCode()) {
            mAudioRecord.startRecording();
            mAudioStatus = AudioStatus.STATUS_STOP.getCode();
            releaseAudioRecord();
        } else {
            Log.d(TAG, "目前没有再录音");
        }
    }

    /**
     * 取消录音
     */
    public void cancelAudioRecord() {
        mAudioFileName = null;
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mAudioStatus = AudioStatus.STATUS_NO_READY.getCode();
    }

    /**
     * 释放录音资源
     */
    private void releaseAudioRecord() {
        Log.d(TAG, "========release===========");
        try {
            if (!TextUtils.isEmpty(mAudioFileName)) {
                String pcmFilePath = FileUtil.getPcmFileAbsolutePath(mAudioFileName);
                String wavFilePath = FileUtil.getWavFileAbsolutePath(mAudioFileName);
                FileUtil.deleteFile(wavFilePath);
                if (FileUtil.isFileExist(pcmFilePath)) {
                    PcmToWavUtil.newInstance(mBufferSize, AUDIO_SIMPLE_RATE, AUDIO_CHANNEL).convertPcmToWav(
                            pcmFilePath,
                            wavFilePath
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mAudioStatus = AudioStatus.STATUS_NO_READY.getCode();
    }

    /**
     * 将录音数据保存为pcm格式文件
     *
     * @param listener
     */
    private void writeAudioDataToPcmFile(RecordStreamListener listener) {
        byte[] buffer = new byte[mBufferSize];

        FileOutputStream fos = null;
        int readSize = 0;
        try {
            File file = new File(FileUtil.getPcmFileAbsolutePath(mAudioFileName));
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将录音状态设置正在录音状态
        mAudioStatus = AudioStatus.STATUS_START.getCode();
        while (mAudioStatus == AudioStatus.STATUS_START.getCode()) {
            readSize = mAudioRecord.read(buffer, 0, mBufferSize);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize && fos != null) {
                try {
                    fos.write(buffer);
                    if (listener != null) {
                        listener.recordOfByte(buffer, 0, buffer.length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
