package com.hadesky.cacw.bean;

/**
 * Created by dzysg on 2015/10/16 0016.
 */
public class TaskMemberBean
{

    public TaskMemberBean()
    {

    }
    public TaskMemberBean(int type)
    {
        mType = type;
    }
    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    private int mType = 1;

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    private String Name;
}
