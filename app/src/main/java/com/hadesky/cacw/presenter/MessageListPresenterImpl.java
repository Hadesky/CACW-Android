package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.MessageBean;
import com.hadesky.cacw.model.MessageRepertory;
import com.hadesky.cacw.model.RxSubscriber;
import com.hadesky.cacw.ui.view.MessageListView;

import java.util.List;

/**
 * 消息列表
 * Created by dzysg on 2016/9/9 0009.
 */
public class MessageListPresenterImpl implements MessageListPresenter
{

    private MessageListView mView;
    private MessageRepertory mMessageRepertory;
    private List<MessageBean> mMessageList;

    public MessageListPresenterImpl(MessageListView view)
    {
        mView = view;
        mMessageRepertory = new MessageRepertory();
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public void loadMessage()
    {
        mMessageRepertory.getMessageList().subscribe(new RxSubscriber<List<MessageBean>>()
        {
            @Override
            public void _onError(String msg)
            {
                mView.hideProgress();
                mView.showMsg(msg);
            }

            @Override
            public void _onNext(List<MessageBean> list)
            {
                mView.hideProgress();
                if (list != null && list.size() != 0)
                {
                    mView.showMessage(list);
                    mMessageList = list;
                }
            }
        });
    }


    @Override
    public void deleteMessage(MessageBean bean)
    {
        mMessageRepertory.deleteUserMessage(bean.getOther().getId()).subscribe(new RxSubscriber<Integer>()
        {
            @Override
            public void _onError(String msg)
            {

            }

            @Override
            public void _onNext(Integer integer)
            {

            }
        });
    }
}
