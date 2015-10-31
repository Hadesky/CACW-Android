package com.hadesky.cacw.ui.fragment;

import com.hadesky.cacw.bean.ProjectBean;

import java.util.List;

/**
 * Created by 45517 on 2015/10/31.
 */
public interface MyProjectView {
    void showProgress();

    void hideProgress();

    void showData(List<ProjectBean> data);

    void onFailure(String msg);
}
