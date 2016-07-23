package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.ui.activity.InviteMemberActivity;
import com.hadesky.cacw.ui.fragment.InvitePersonFragment;

import java.util.List;

/**
 * 这是用在SearchPersonFragment上的View接口，定义了将SearchPersonFragment适配为邀请新成员时需要的接口
 * Created by 45517 on 2016/7/23.
 */
public interface InvitePersonPresenter {
    /**
     * 当点击邀请按钮时调用此接口
     * @param listener listener
     */
    void setOnInviteListener(InvitePersonFragment.OnInviteListener listener);

    /**
     * 需要屏蔽已经加入Team的某些成员，因此在搜索结果中屏蔽部分已经是团队成员的结果的邀请按钮
     * @param teamMember team的成员
     */
    void setTeamMember(List<UserBean> teamMember);
}
