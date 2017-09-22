package com.tinker_demotest.tinker;

import android.content.Context;

import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
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

//    private static CustomPatchListener mPatchListener;

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
//        mPatchListener = new CustomPatchListener(getApplicationContext());
//        TinkerInstaller.install(mAppLike); //完成tinker初始化
        /**
         * 因为这里用到了MD5的文件校验所以需要复杂的install()
         * 这里 DefaultLoadReporter类中 onLoadPatchListenerReceiveFail（）方法报错
         *.DefaultLoadReporter: patch loadReporter onLoadPatchListenerReceiveFail:
         * patch receive fail:/storage/emulated/0/Android/data/com.imooc/cache/tpatch/atch_signed.apk, code:-1
         * 所以这里需要用到applicationLike.getApplication，否则会找不到context
         */
        LoadReporter loadReporter = new DefaultLoadReporter(applicationLike.getApplication());
        PatchReporter patchReporter = new DefaultPatchReporter(applicationLike.getApplication());
        PatchListener patchListener = new DefaultPatchListener(applicationLike.getApplication());
        AbstractPatch upgradePatchProcessor = new UpgradePatch();
        TinkerInstaller.install(applicationLike,
                loadReporter,
                patchReporter,
                patchListener,
                CustomResultService.class,
                upgradePatchProcessor);//完成Tinker的初始化
        isInstalled = true;
    }

    //完成Patch文件的加载
    public static void loadPatch(String path) {

        if (Tinker.isTinkerInstalled()) {
//            mPatchListener.setCurrentMD5(md5Value);
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
