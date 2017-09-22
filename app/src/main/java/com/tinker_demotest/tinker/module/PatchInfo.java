package com.tinker_demotest.tinker.module;

import cn.bmob.v3.BmobObject;

/**
 * Created by 75095 on 2017/8/29.
 */

public class PatchInfo extends BmobObject {

    public String downLoadUrl; //不为空则表面有更新

    public String versionName; //本次patch包的版本号

    public String patchMessage; //本次patch包含的相关信息,例如：主要做了那些改动

   // public String md5;  //patch文件正确的md5


    public PatchInfo(String downLoadUrl, String versionName, String patchMessage) {
        this.downLoadUrl = downLoadUrl;
        this.versionName = versionName;
        this.patchMessage = patchMessage;
    }


    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPatchMessage() {
        return patchMessage;
    }

    public void setPatchMessage(String patchMessage) {
        this.patchMessage = patchMessage;
    }


    @Override
    public String toString() {
        return "PatchInfo{" +
                "downLoadUrl='" + downLoadUrl + '\'' +
                ", versionName='" + versionName + '\'' +
                ", patchMessage='" + patchMessage + '\'' +
                '}';
    }
}
