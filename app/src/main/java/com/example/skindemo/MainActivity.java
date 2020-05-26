
package com.example.skindemo;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import com.itxiao.skinlib.SkinActivity;
import com.itxiao.skinlib.SkinManager;

import java.io.File;

public class MainActivity extends SkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void changeSkin(View view) {
        String path = Environment.getExternalStorageDirectory()+
                File.separator+"skin2.zip";
        File file = new File(path);
        Log.d("itxiaox",path+"; "+file.exists());
        SkinManager.getInstance().loadSkinApk(path);
        changeSkin();
    }
}