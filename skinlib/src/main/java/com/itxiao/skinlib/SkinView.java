package com.itxiao.skinlib;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class SkinView {
   public View view;
   public List<SkinItem> skinItems;

    public SkinView(View view, List<SkinItem> skinItems) {
        this.view = view;
        this.skinItems = skinItems;
    }

    public void apply(){
        for (SkinItem skinItem : skinItems) {
            Log.d("skin", "apply:skinItem= "+skinItem.toString());
            if (skinItem.name.equals("background")){
                //1.设置的是color颜色，2.设置的是图片
                if (skinItem.typeName.equals("color")){
                    //将资源Id, 传给ResouceManager 去进行资源匹配，如果匹配到了，就直接设置给控件
                    //如果没有匹配到，就把之前的资源ID，设值控件
                    if (SkinManager.getInstance().resourcesIsNull()){
                        view.setBackgroundResource(SkinManager.getInstance().getColor(skinItem.resId));
                    }else {
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.resId));
                    }
                }else if (skinItem.typeName.equals("drawable")||skinItem.typeName.equals("mipmap")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        view.setBackground(SkinManager.getInstance().getDrawable(skinItem.resId));
                    }else {
                        view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.resId));
                    }
                }
            }else if (skinItem.name.equals("src")){
                if (skinItem.typeName.equals("drawable")||skinItem.typeName.equals("mipmap")){
                    Log.d("skin","ImageView set Src ");
                    ImageView imageView = (ImageView) view;
                    //此种方法无限，Android9.0
//                    imageView.setImageResource(SkinManager.getInstance().getDrawableId(skinItem.resId));
                    imageView.setImageDrawable(SkinManager.getInstance().getDrawable(skinItem.resId));
                }
            }else if (skinItem.name.equals("textColor")){
                //TextView: 包含TextView/Button/EditText的父类
                //todo，这里拿到的资源Id,无法通过textView.setTextColor进行设置，此方法无效
                ((TextView)view).setTextColor(SkinManager.getInstance().getColorStateList(skinItem.resId));

            }else if (skinItem.name.equals("textSize")){
                //TextView: 包含TextView/Button/EditText的父类
                float textSize = SkinManager.getInstance().getDimension(skinItem.resId);
                Log.d("skin", "apply: textSize="+textSize);
                ((TextView)view).setTextSize(textSize);

            }
        }
    }
}
