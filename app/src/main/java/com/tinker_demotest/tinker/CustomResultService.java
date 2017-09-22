package com.tinker_demotest.tinker;

import android.widget.Toast;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * Created by 75095 on 2017/9/1.
 * 本类的作用，决定再patch安装完以后的后续操作，默认实现是杀进程
 * onPatchResult()中将系统默认的杀死进程代码给去掉
 * 这样用户手机更新成功安装patch文件时就不会出现闪退的现象
 */

public class CustomResultService extends DefaultTinkerResultService {
    private static final String TAG = "Tinker.SampleResultService";


    /**
     * 返回patch文件的最终安装结果
     */
    @Override
    public void onPatchResult(PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "DefaultTinkerResultService received null result!!!!");
            Toast.makeText(this, "result == null", Toast.LENGTH_SHORT).show();
            return;
        }
        TinkerLog.i(TAG, "DefaultTinkerResultService received a result:%s ", result.toString());
        //first, we want to kill the recover process
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());
        // if success and newPatch, it is nice to delete the raw file, and restart at once
        // only main process can load an upgrade patch!
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));
            Toast.makeText(this, "isSuccess", Toast.LENGTH_SHORT).show();
            //以下是系统默认杀死程序进程的代码，删除掉提高用户体验
//            if (checkIfNeedKill(result)) {
//                android.os.Process.killProcess(android.os.Process.myPid());
//            } else {
//                TinkerLog.i(TAG, "I have already install the newly patch version!");
//            }
        }

    }
}
