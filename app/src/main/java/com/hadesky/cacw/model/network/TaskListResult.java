package com.hadesky.cacw.model.network;

import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class TaskListResult
{
    protected String error_msg;
    protected int state_code;
    List<TaskBean> data;
}
