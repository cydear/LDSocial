package com.ldsocial.app.base;

import android.content.res.Configuration;

/**
 * 各组业务组件通过实现此类做初始化工作
 *
 * @author lary.huang
 * @version v 1.4.8 2018/12/20 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public class BaseAppLogic {
    private BaseApplication mApplication;

    public BaseAppLogic() {

    }

    public void setApplication(BaseApplication application) {
        this.mApplication = application;
    }

    public void onCreate() {

    }

    public void onLowMemory() {

    }

    public void onTerminate() {

    }

    public void onTrimMemory(int level) {

    }

    public void onConfigurationChanged(Configuration newConfig) {

    }
}
