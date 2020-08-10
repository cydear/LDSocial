package com.ldsocial.app.ldlogin.audio;

import android.media.AudioFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName PcmToWavUtils
 * @Description: pcm格式转换成wav格式
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 2:35 PM
 * @Version: 1.0
 */
public class PcmToWavUtil {
    /**
     * 缓存的音频大小
     */
    private int mBufferSize;
    /**
     * 采样率
     */
    private int mSimpleRate;
    /**
     * 声道数
     */
    private int mChannel;

    /**
     * @param bufferSize 缓存音频大小
     * @param simpleRate 采样率
     * @param channel    声道数
     */
    private PcmToWavUtil(int bufferSize, int simpleRate, int channel) {
        this.mBufferSize = bufferSize;
        this.mSimpleRate = simpleRate;
        this.mChannel = channel;
    }

    /**
     * @param bufferSize 缓存音频大小
     * @param simpleRate 采样率
     * @param channel    声道数
     */
    public static PcmToWavUtil newInstance(int bufferSize, int simpleRate, int channel) {
        return new PcmToWavUtil(bufferSize, simpleRate, channel);
    }

    /**
     * 将pcm文件转换成wav文件
     *
     * @param sourceFile 源文件
     * @param targetFile 生成的目标文件
     */
    public void convertPcmToWav(String sourceFile, String targetFile) {
        if (FileUtil.isFileExist(targetFile)) {
            FileUtil.deleteFile(targetFile);
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long totalAudioSize = 0;
        long totalDataSize = 0;
        long simpleRate = mSimpleRate;
        int channels = mChannel == AudioFormat.CHANNEL_IN_MONO ? 1 : 2;
        long byteRate = 16 * mSimpleRate * channels / 8;
        byte[] data = new byte[mBufferSize];
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            totalAudioSize = fis.getChannel().size();
            totalDataSize = totalAudioSize + 36;

            addWavFileHeader(fos, totalAudioSize, totalDataSize, simpleRate, channels, byteRate);
            while (fis.read(data) != -1) {
                fos.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为Pcm文件增加Wav文件头
     *
     * @param fos
     * @param totalAudioSize
     * @param totalDataSize
     * @param simpleRate
     * @param channels
     * @param byteRate
     */
    private void addWavFileHeader(FileOutputStream fos, long totalAudioSize, long totalDataSize, long simpleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        // RIFF/WAVE header
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataSize & 0xff);
        header[5] = (byte) ((totalDataSize >> 8) & 0xff);
        header[6] = (byte) ((totalDataSize >> 16) & 0xff);
        header[7] = (byte) ((totalDataSize >> 24) & 0xff);
        //WAVE
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        // 'fmt ' chunk
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        // 4 bytes: size of 'fmt ' chunk
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        // format = 1
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (simpleRate & 0xff);
        header[25] = (byte) ((simpleRate >> 8) & 0xff);
        header[26] = (byte) ((simpleRate >> 16) & 0xff);
        header[27] = (byte) ((simpleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // block align
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        // bits per sample
        header[34] = 16;
        header[35] = 0;
        //data
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioSize & 0xff);
        header[41] = (byte) ((totalAudioSize >> 8) & 0xff);
        header[42] = (byte) ((totalAudioSize >> 16) & 0xff);
        header[43] = (byte) ((totalAudioSize >> 24) & 0xff);
        fos.write(header, 0, 44);
    }
}
