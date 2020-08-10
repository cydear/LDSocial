package com.ldsocial.app.ldlogin.audio;

/**
 * 获取录音音频流
 */
public interface RecordStreamListener {
    void recordOfByte(byte[] data, int begin, int end);
}