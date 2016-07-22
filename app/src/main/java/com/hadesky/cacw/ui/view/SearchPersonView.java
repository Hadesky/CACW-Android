package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * Created by 45517 on 2016/7/22.
 */
public interface SearchPersonView extends BaseView {
    /**
     * 这是第一次获取或者调用刷新的时候进行的置换setData
     *
     * @param data 要置换的新Data
     */
    void setData(List<UserBean> data, boolean isFinal);

    /**
     * 这是每次加载出来的新Data，添加在已经set过的data后面
     *
     * @param data new data
     */
    void addData(List<UserBean> data, boolean isFinal);
}
