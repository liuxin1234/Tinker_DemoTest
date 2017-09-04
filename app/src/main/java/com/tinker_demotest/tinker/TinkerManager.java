package com.tinker_demotest.tinker;

import android.content.Context;


import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * Created by renzhiqiang on 17/4/27.
 *
 * @functon 对Tinker的所有api做一层封装
 */
public class TinkerManager {

    private static boolean isInstalled = false;

    private static ApplicationLike mAppLike;



    /**
     * 完成Tinker的初始化
     *
     * @param applicationLike
     */
    public static void installTinker(ApplicationLike applicationLike) {
        mAppLike = applicationLike;
        if (isInstalled) {
            return;
        }

        TinkerInstaller.install(mAppLike); //完成tinker初始化

        isInstalled = true;
    }

    //完成Patch文件的加载
    public static void loadPatch(String path){

        if (Tinker.isTinkerInstalled()) {

            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        }
    }

    //通过ApplicationLike获取Context
    private static Context getApplicationContext() {
        if (mAppLike != null) {
            return mAppLike.getApplication().getApplicationContext();
        }
        return null;
    }
}
