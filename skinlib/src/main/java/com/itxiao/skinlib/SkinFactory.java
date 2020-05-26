package com.itxiao.skinlib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {
    private static final String TAG = "SkinFactory";
    //拦截到布局中的布局中的所有控件
    //所有的官方控件、系统控件都是在三个包下
    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    //收集需要换肤的控件的容器
    private List<SkinView> viewList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;
        //第一种，带包名，一般为自定义的控件
        if (name.contains(".")){
            view =  onCreateView(name,context,attrs);
        }else {
        //第二种，不带包名，一般为系统控件
            for (String s : prxfixList) {
                String viewName = s + name; //包名+控件名，
                view = onCreateView(viewName,context,attrs);
                if (view!=null){
                    break;
                }
            }
        }
        //收集控件
        if (view !=null){
            paserView(view,name,attrs);
        }

        return view;
    }

    public void apply(){
        for (SkinView skinView : viewList) {
            Log.d(TAG, "apply: skinView="+skinView.skinItems.size());
            skinView.apply();
        }
    }

    /**
     *收集需要换肤的控件
     * @param view
     * @param name
     * @param attrs
     */
    private void paserView(View view, String name,AttributeSet attrs){
        List<SkinItem> skinItems = new ArrayList<>();
        //遍历的是这个当前进来的控件的所有的属性
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //得到属性的名字
            String attributeName = attrs.getAttributeName(i);
            //只有控制中包含background/textColor/src，就需要换肤，实际项目中可以采用自定义属性changeSkin来控制是否换肤
            if (attributeName.contains("background")||
                    attributeName.contains("textColor")||
                    attributeName.contains("textSize")||
                    attributeName.contains("src")){
                //认为是需要收集的
                //资源ID
                String attributeValue = attrs.getAttributeValue(i);
                int resId = Integer.parseInt(attributeValue.substring(1));
                //资源ID的类型
                String resourceTypeName = view.getResources().getResourceTypeName(resId);
                //自然ID的名字
                String resourceEntryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attributeName,resourceTypeName,resourceEntryName,resId);
                skinItems.add(skinItem);
            }
        }

        //如果一个控件里有需要换肤的属性
        if (skinItems.size()>0){
            SkinView skinView = new SkinView(view,skinItems);
            viewList.add(skinView);
        }
    }

    //将控件实例化
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;

        try{
            Class aClass = context.getClassLoader().loadClass(name);
            //首先，获 取到第二个构造方法
            Constructor<? extends View> constructor =  aClass.getConstructor(Context.class,AttributeSet.class);
            view = constructor.newInstance(context,attrs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

}
