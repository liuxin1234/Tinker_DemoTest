package com.tinker_demotest.tinker;

import android.content.Context;


import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tinker_demotest.util.Utils;

/**
 * Created by 75095 on 2017/9/1.
 *  1.校验patch文件是否合法
 *  2.启动service去安装patch文件
 *  这里使用MD5文件校验的原因是怕被其他人劫持了，篡改我们的应用，造成影响
 */

public class CustomPatchListener extends DefaultPatchListener {

    private String currentMD5;

    public void setCurrentMD5(String currentMD5) {
        this.currentMD5 = currentMD5;
    }

    public CustomPatchListener(Context context) {
        super(context);
    }

    @Override
    protected int patchCheck(String path) {
        //patch文件MD5值与服务器上传过来的MD5值进行校验，如果不相等则返回报错信息
        if (!Utils.isFileMD5Matched(path,currentMD5)){
            return ShareConstants.ERROR_PATCH_DISABLE;
        }
        return super.patchCheck(path);
    }
}
