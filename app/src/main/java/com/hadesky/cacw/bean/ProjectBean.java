package com.hadesky.cacw.bean;

/**
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean {
    private String title;//Item的文字
    private int ResId;//头像所在的id，并不考虑吧头像作为一个对象传进来，不过具体怎么实现还需要想

    public ProjectBean() {

    }

    public ProjectBean(int resId, String title, boolean isMarked) {
        this.title = title;
        ResId = resId;
    }


    public String getTitle() {
        return title;
    }

    public int getResId() {
        return ResId;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setResId(int resId) {
        ResId = resId;
    }
}
