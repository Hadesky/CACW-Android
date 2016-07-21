package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

/**
 *  团队搜索
 * Created by dzysg on 2016/7/20 0020.
 */
public interface TeamSearchView extends BaseView
{
    void showSearchResult(List<TeamBean> list);

}
