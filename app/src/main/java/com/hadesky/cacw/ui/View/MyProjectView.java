package com.hadesky.cacw.ui.View;

import com.hadesky.cacw.bean.ProjectBean;

import java.util.List;

/**我的项目界面接口
 * Created by 45517 on 2015/10/31.
 */
public interface MyProjectView {
    void showProgress();

    void hideProgress();

    void showData(List<ProjectBean> data);

    void onFailure(String msg);
}
