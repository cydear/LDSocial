package com.ldsocial.app.ldlogin.audio;

/**
 * @ClassName AudioState
 * @Description: 录音状态
 * @Author: Lary.huang
 * @CreateDate: 2020/8/10 2:57 PM
 * @Version: 1.0
 */
public enum AudioStatus {
    STATUS_NO_READY(001, "未开始"),
    STATUS_READY(002, "预备"),
    STATUS_START(003, "开始"),
    STATUS_PAUSE(004, "暂停"),
    STATUS_STOP(005, "停止");


    private int code;
    private String msg;

    AudioStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
