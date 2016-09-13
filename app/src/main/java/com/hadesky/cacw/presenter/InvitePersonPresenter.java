package com.hadesky.cacw.presenter;

/**
 * 这是用在SearchPersonFragment上的View接口，定义了将SearchPersonFragment适配为邀请新成员时需要的接口
 * Created by 45517 on 2016/7/23.
 */
public interface InvitePersonPresenter extends SearchPresenter{

    void inviteUser(String s, int position);

}
