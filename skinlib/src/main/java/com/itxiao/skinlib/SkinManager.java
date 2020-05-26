package com.itxiao.skinlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;

public class SkinManager {
    private Context mContext;
    private static SkinManager skinManager ;
    private String packageName;
    private Resources resources;
    private SkinManager(){}
    public static SkinManager getInstance(){
        if (skinManager==null){
            synchronized (SkinManager.class){
                skinManager = new SkinManager();
            }
        }
        return skinManager;
    }

    public void setContext(Context context){
        this.mContext = context;
    }

    /**
     * 根据路径去加载皮肤管理资源包,获取资源对象，Resources
     * @param path
     */
    public void loadSkinApk(String path){
       //获取到包管理器,  利用加载管理器加载资源包
        PackageManager packageManager = mContext.getPackageManager();
        //这里是为了拿到包名
        //获取资源包的包信息类
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        if (packageInfo==null){
            throw new RuntimeException("packageInfo is null");
        }
        packageName = packageInfo.packageName;
        Log.d("skin", "loadSkinApk: packageName="+packageName);
        //通过反射获取到AssetManager的对象
        try{
            AssetManager assetManager = AssetManager.class.newInstance();
            //通过反射获取addAssetPath这个方法
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,path);
            //自己创建一个Resources,但需要AssetManager，所以需要提前创建一个AssetManager
             resources = new Resources(assetManager,
                    mContext.getResources().getDisplayMetrics()
            ,mContext.getResources().getConfiguration());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取资源颜色
     * @param resId 当前App中需要更新资源的ID
     * @return
     */
    public int getColor(@ColorInt int resId){
        if (resourcesIsNull()){
            return  resId;
        }
       int identifier = identifierId(resId);
        if (identifier==0){
            //没有匹配到
            return resId;
        }
        return resources.getColor(identifier);
    }
    public ColorStateList getColorStateList(int resId) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(mContext, resId);
        if (resourcesIsNull()) {
            return colorStateList;
        }
        int identifier = identifierId(resId);
        return identifier == 0 ? colorStateList : resources.getColorStateList(identifier);
    }
    /**
     * 从资源包中拿到drawable的资源Id
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId){
        if (resourcesIsNull()){
            return ContextCompat.getDrawable(mContext,resId);
        }
        int identifier = identifierId(resId);
        if (identifier==0){
            //没有匹配到
            return ContextCompat.getDrawable(mContext,resId);
        }
        return resources.getDrawable(identifier);
    }


    public float getDimension(int resId){
        if (resourcesIsNull()){
            return resources.getDimension(resId);
        }
        int identifier = identifierId(resId);
        if (identifier==0){
            //没有匹配到
            return resources.getDimension(resId);
        }
        return resources.getDimension(identifier);
    }

//    /**
//     * 从资源包中拿到drawable的资源Id
//     * @param resId
//     * @return
//     */
//    public int getDrawableId(int resId){
//        if (resourcesIsNull()){
//            return resId;
//        }
//        int identifier = identifierId(resId);
//        if (identifier==0){
//            //没有匹配到
//            return resId;
//        }
//        return identifier;
//    }

    private int identifierId(int resId){
        //通过资源ID得到资源的类型和名字，然后去下载的资源包中进行匹配，匹配到后替换
        String resourcesTypeName = mContext.getResources().getResourceTypeName(resId);
        String resourcesEntryName = mContext.getResources().getResourceEntryName(resId);
//        packageName = "com.example.skindemo";
        Log.d("skin",String.format("resourcesTypeName=%s,resourcesEntryName=%s,packageName=%s",resourcesTypeName,resourcesEntryName,packageName));
        //去匹配
       int identifier = resources.getIdentifier(resourcesEntryName,resourcesTypeName,packageName);
        Log.d("skin",String.format("resourcesTypeName resId=%s,identifier=%s",resId,identifier));
        return identifier;
    }

    public  boolean resourcesIsNull(){
       return resources==null;
    }
}
