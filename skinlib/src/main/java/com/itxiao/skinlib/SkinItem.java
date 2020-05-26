package com.itxiao.skinlib;

/**
 * 将每个属性封装为一个对象
 */
public class SkinItem {
    public String name;//属性的名字，如： textColor text background
    public String typeName;//属性的值的类型， color/mipmap
    public String entryName;//属性的值的名字； colorPrimary
    public int resId;//属性的资源ID

    public SkinItem(String name, String typeName, String entryName, int resId) {
        this.name = name;
        this.typeName = typeName;
        this.entryName = entryName;
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "SkinItem{" +
                "name='" + name + '\'' +
                ", typeName='" + typeName + '\'' +
                ", entryName='" + entryName + '\'' +
                ", resId=" + resId +
                '}';
    }
}
