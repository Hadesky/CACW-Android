package com.hadesky.cacw.ui.View;

/**用户资源界面接口
 * Created by 45517 on 2015/11/13.
 */
public interface UserInfoView {
    void showProgressDialog();
    void hideProgressDialog();
    void onFailure();//获取资料时失败的回调
}