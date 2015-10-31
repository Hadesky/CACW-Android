package com.hadesky.cacw.Model;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.database.ProjectDAO;

import java.util.List;

/**
 *
 * Created by 45517 on 2015/10/31.
 */
public class MyProjectModel {
    private GetMyProjectCallBack getMyProjectCallBack;
    private ProjectDAO projectDAO;

    public MyProjectModel(GetMyProjectCallBack getMyProjectCallBack) {
        projectDAO = new ProjectDAO();
        this.getMyProjectCallBack = getMyProjectCallBack;
    }
    public void GetMyProjectByCache() {
        List<ProjectBean> list = projectDAO.getAllMyProject();
        getMyProjectCallBack.onSucceed(list);
    }

    public void GetMyProjectByNetwork() {
        getMyProjectCallBack.onFailure("服务器暂时无法使用！");
    }



    public interface GetMyProjectCallBack{
        void onSucceed(List<ProjectBean> list);
        void onFailure(String error);
    }
}
