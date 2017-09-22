package com.tinker_demotest.tinker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.tinker_demotest.tinker.module.BasePatch;
import com.tinker_demotest.tinker.module.PatchInfo;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 75095 on 2017/8/29.
 * 应用程序Tinker更新服务
 * 利用Service让用户没有感知的将APP更新好
 * 1.从服务器下载patch文件
 * 2.使用TinkerManager完成patch文件加载
 * 3.patch文件会在下次进程启动时生效
 */

public class TinkerService extends Service {
    private static final String TAG = TinkerService.class.getSimpleName();
    private static final int UPDATE_PATCH = 0x02;   //检查是否有patch更新
    private static final int DOWNLOAD_PATCH = 0x01; //下载patch文件信息
    private static final String FILE_END = ".apk";  //文件后缀名


    private String apkUrl; //Apk下载路径
    private String mPatchFileDir;   //patch要保存的文件夹
    private String mPatchFile; //patch文件夹存放的路径
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_PATCH:
                    checkPatchUpdate();
                    break;
                case DOWNLOAD_PATCH:
                    downLoadPatch();
                    break;
            }
        }


    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       //检查是否有patch更新
        mHandler.sendEmptyMessage(UPDATE_PATCH);
        return START_NOT_STICKY;    //被系统回收不再重启
    }

    //完成文件目录的构造
    private void init() {
        //对文件夹创建的初始化
        mPatchFileDir = getExternalCacheDir().getAbsolutePath()+"/tpatch/";
        File patchDir = new File(mPatchFileDir);
        try {
            if (patchDir == null || !patchDir.exists()){
                patchDir.mkdir(); //文件夹不存在则创建
            }
        }catch (Exception e){
            e.printStackTrace();
            stopSelf(); //无法正常创建文件，则终止服务
        }

    }


    /**
     * 对外提供启动service方法
     * @param context
     */
    public static void runTinkerService(Context context){
        try {
            Intent intent = new Intent(context,TinkerService.class);
            context.startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private BasePatch mBasePatchInfo;  //服务器patch信息

    //检查服务器是否有patch文件
    private void checkPatchUpdate() {
        /**
         *这里写自己封装好的网络请求，去请求服务器上的.apatch是否有文件的url,如果存在
         * url则将文件以流的形式下载下来，保存到手机指定的File文件路径里；
         * 可以将返回回来的BasePatchInfo对象类：
         *   String downloadUrl:    //不为空则表明有更新；
         *   String versionName:    //本次patch包的版本号；
         *   String patchMessage;   //本次patch包含的相关信息，例如：主要做了那些改动
         *
         *   注意：
         *   如果请求成功onSuccess()并且mBasePatchInfo.data.downloadUrl不为空,则下载.apatch文件
         *   否则调用stopSelf();
         *   如果请求失败onFailure,则调用stopSelf();
         *
         */
        BmobQuery<PatchInfo> bmobQuery = new BmobQuery<PatchInfo>();
        bmobQuery.getObject("M4Qg999C", new QueryListener<PatchInfo>() {
            @Override
            public void done(PatchInfo patchInfo, BmobException e) {
                if(e==null){
                    if (patchInfo.getDownLoadUrl() != null){
                        apkUrl = patchInfo.getDownLoadUrl();
                        //请求成功后，将消息传递出去
                        mHandler.sendEmptyMessage(DOWNLOAD_PATCH);
                    }
                    Log.e(TAG,"请求成功");
                }else{
                    Log.e(TAG, "查询失败：" + e.getMessage() );
                }
            }
        });



    }

    private void downLoadPatch() {
        //初始化patch文件下载路径,String.valueOf(System.currentTimeMillis())获取当前时间作为文件名
        mPatchFile = mPatchFileDir.concat(String.valueOf(System.currentTimeMillis()))
                .concat(FILE_END);
        /**
         * 这里是自定义封装好的网络请求下载方法
         * RequestCenter.downloadFile();向服务器请求并下载文件
         * 在请求成功onSuccess()方法里调用：TinkerManager.loadPatch(mPatchFile);
         * 在请求失败onFailure()方法里调用：stopSelf();
         */

        BmobFile bmobFile = new BmobFile("atch_signed.apk","",apkUrl);
        File saveFile = new File(mPatchFileDir,bmobFile.getFilename());
        System.out.println("保存的路径："+saveFile.toString());
        bmobFile.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                toast("开始下载");
            }

            @Override
            public void done(String savePath, BmobException e) {
                if(e==null){
                    toast("下载成功,保存路径:"+savePath);

                    TinkerManager.loadPatch(savePath);
                }else{
                    toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }
        });




    }

    private void toast(String s){
        Toast.makeText(TinkerService.this, s, Toast.LENGTH_SHORT).show();
    }
}
