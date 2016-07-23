package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.MessageBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/23 0023.
 */
public interface ChatView extends BaseView
{
    void showChatList(List<MessageBean> list);

}
