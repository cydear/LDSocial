package com.ldsocial.app.base;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * application 基类
 *
 * @author lary.huang
 * @version v 1.4.8 2018/12/20 XLXZ Exp $
 * @email huangyang@xianglin.cn
 */
public abstract class BaseApplication extends Application {
    private List<Class<? extends BaseAppLogic>> mAppLogicClasses = new ArrayList<>();

    private List<BaseAppLogic> mAppLogics = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initAppLogic();
        createAppLogic();
    }

    protected abstract void initAppLogic();

    private void createAppLogic() {
        Observable.fromIterable(mAppLogicClasses)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Class<? extends BaseAppLogic>>() {
                    @Override
                    public void accept(Class<? extends BaseAppLogic> aClass) throws Exception {
                        BaseAppLogic appLogic = aClass.newInstance();
                        mAppLogics.add(appLogic);
                        //调用module初始化方法
                        appLogic.onCreate();
                    }
                });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Observable.fromIterable(mAppLogics)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseAppLogic>() {
                    @Override
                    public void accept(BaseAppLogic baseAppLogic) throws Exception {
                        if (baseAppLogic != null) {
                            baseAppLogic.onTerminate();
                        }
                    }
                });
    }

    /**
     * 注册各module的配置文件
     *
     * @param appLogicClass
     */
    protected void registerModuleAppLogic(Class<? extends BaseAppLogic> appLogicClass) {
        if (null != appLogicClass && !mAppLogicClasses.contains(appLogicClass)) {
            mAppLogicClasses.add(appLogicClass);
        }
    }
}
