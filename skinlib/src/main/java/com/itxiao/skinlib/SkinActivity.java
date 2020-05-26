package com.itxiao.skinlib;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

public abstract class SkinActivity extends Activity {
    SkinFactory skinFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().setContext(this);
         skinFactory = new SkinFactory();
        //它会拦截到布局中所有的控件
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),skinFactory );
        //执行setContentView之前，收集所有换肤的控件
    }

    public void changeSkin(){
        skinFactory.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        skinFactory.apply();
    }
}
