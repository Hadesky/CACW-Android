package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.MessageBean;

/**
 *  消息列表
 * Created by dzysg on 2016/7/23 0023.
 */
public interface MessageListPresenter
{
    void onDestroy();
    void LoadMessage();

    void deleteMessage(MessageBean bean);

}
