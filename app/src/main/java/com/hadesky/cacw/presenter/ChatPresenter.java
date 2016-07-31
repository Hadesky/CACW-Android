package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.MessageBean;

/**
 *
 * Created by dzysg on 2016/7/23 0023.
 */
public interface ChatPresenter
{

    void loadChatMessage();

    void send(String text);

    void loadMore();

    void onDestroy();

    void AcceptJoinTeam(MessageBean bean);
    void rejectJoinTeam(MessageBean bean);


    void deleteChat();
}
