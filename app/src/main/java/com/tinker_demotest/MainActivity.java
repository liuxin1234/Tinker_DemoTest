package com.tinker_demotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tinker_demotest.tinker.TinkerManager;

import java.io.File;

/**
 * adb push ./app/build/outputs/tinkerPatch/release/patch_signed.apk /storage/emulated/0/Android/data/com.tinker_demotest/cache/tpatch/tinkerTest.apk
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILE_END = ".apk";
    private String mPatchDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPatchDir = getExternalCacheDir().getAbsolutePath()+"/tpatch/";
        System.out.println("文件路径："+mPatchDir);
        //是为了创建我们的文件夹
        File file = new File(mPatchDir);
        if (file == null || file.exists()){
            file.mkdir();
        }

        initView();

    }

    private void initView() {
        findViewById(R.id.btn_patch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinkerManager.loadPatch(getPatchName());
                Toast.makeText(MainActivity.this, "点击了:"+getPatchName(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了更新后的按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //构建patch文件名
    private String getPatchName(){
        return mPatchDir.concat("tinkerTest").concat(FILE_END);
    }
}
