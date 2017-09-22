package com.tinker_demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.tinker_demotest.tinker.TinkerService;
import com.tinker_demotest.tinker.module.PatchInfo;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * adb push ./app/build/outputs/tinkerPatch/release/patch_signed.apk /storage/emulated/0/Android/data/com.imooc/cache/tpatch/tinker.apk
 *
 * 注意这里我的后台用的bmob云服务，作为下载patch文件的服务器
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBmobSDK();
        //启动AndFixService,这里最好是在欢迎界面启动
        TinkerService.runTinkerService(this);
    }
    private void initBmobSDK() {
        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "e92ba05a9212ab223475f67d85637f4e");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

    }


    public void toastClikc(View view) {

        BmobQuery<PatchInfo> bmobQuery = new BmobQuery<PatchInfo>();
        //多条数据查询
        bmobQuery.addWhereEqualTo("versionInfo","热更新信息");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<PatchInfo>() {
            @Override
            public void done(List<PatchInfo> list, BmobException e) {
                if(e==null){
                    if (!list.isEmpty()){
                        Log.e(TAG,list.toString());
                        Toast.makeText(MainActivity.this, "热修复成功: ", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG,"请求成功");
                }else{
                    Log.e(TAG, "查询失败：" + e.getMessage() );
                }
            }
        });
        //单条数据查询
//        bmobQuery.getObject("9rxI4445", new QueryListener<BasePatch>() {
//            @Override
//            public void done(BasePatch basePatch, BmobException e) {
//                if(e==null){
//                    if (basePatch.getData() != null){
//                        Toast.makeText(MainActivity.this, "热修复成功: ", Toast.LENGTH_SHORT).show();
//                    }
//                    Log.e(TAG,"请求成功");
//                }else{
//                    Log.e(TAG, "查询失败：" + e.getMessage() );
//                }
//            }
//        });



    }
}
