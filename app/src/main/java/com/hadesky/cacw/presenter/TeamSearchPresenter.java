package com.hadesky.cacw.presenter;

/**
 *  团队搜索
 * Created by dzysg on 2016/7/20 0020.
 */
public interface TeamSearchPresenter
{
    void onDestroy();
    void searchById(String id);
    void SearchByName(String name);
    void search(String t);
}
