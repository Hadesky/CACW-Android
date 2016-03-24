package com.hadesky.cacw.presenter;

import com.hadesky.cacw.model.MyProjectModel;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.ui.view.MyProjectView;
import com.hadesky.cacw.util.NetworkUtils;

import java.util.List;

/**我的项目Presenter实现
 * Created by 45517 on 2015/10/31.
 */
public class MyProjectPresenterImpl implements MyProjectPresenter {
    private List<ProjectBean> mData;
    private MyProjectView myProjectView;
    private MyProjectModel myProjectModel;
    private MyProjectModel.GetMyProjectCallBack getMyProjectCallBack = new MyProjectModel.GetMyProjectCallBack() {
        @Override
        public void onSucceed(List<ProjectBean> list) {
            myProjectView.showData(list);
            myProjectView.hideProgress();
        }

        @Override
        public void onFailure(String error) {
            myProjectView.onFailure(error);
        }
    };

    public MyProjectPresenterImpl(MyProjectView myProjectView) {
        this.myProjectView = myProjectView;
        myProjectModel = new MyProjectModel(getMyProjectCallBack);
    }

    @Override
    public void loadProject() {
        myProjectView.showProgress();
        myProjectModel.GetMyProjectByCache();
        if (NetworkUtils.isNetworkConnected(MyApp.getAppContext())) {
            myProjectModel.GetMyProjectByNetwork();
        }
    }
}
