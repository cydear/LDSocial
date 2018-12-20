package com.ldsocial.app;

import com.ldsocial.app.base.BaseApplication;
import com.ldsocial.app.ldlogin.applogic.LoginAppLogic;

/**
 * App 主Application
 *
 * @author lary.huang
 * @version v 1.4.8 2018/12/20 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public class LDSocialApplication extends BaseApplication {
    @Override
    protected void initAppLogic() {
        //注册各module独立的配置文件
        registerModuleAppLogic(LoginAppLogic.class);
    }
}
