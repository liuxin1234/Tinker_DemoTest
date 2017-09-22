package com.tinker_demotest.tinker.module;

import cn.bmob.v3.BmobObject;

/**
 * Created by 75095 on 2017/8/29.
 */

public class BasePatch extends BmobObject {
    public Number ecode;
    public String emsg;
    public PatchInfo data;

    public BasePatch(Number ecode, String emsg, PatchInfo data) {
        this.ecode = ecode;
        this.emsg = emsg;
        this.data = data;
    }

    public BasePatch(String tableName, Number ecode, String emsg, PatchInfo data) {
        super(tableName);
        this.ecode = ecode;
        this.emsg = emsg;
        this.data = data;
    }

    public Number getEcode() {
        return ecode;
    }

    public void setEcode(Number ecode) {
        this.ecode = ecode;
    }

    public String getEmsg() {
        return emsg;
    }

    public void setEmsg(String emsg) {
        this.emsg = emsg;
    }

    public PatchInfo getData() {
        return data;
    }

    public void setData(PatchInfo data) {
        this.data = data;
    }
}
