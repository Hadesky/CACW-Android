package com.hadesky.cacw.presenter;

/**
 *
 * Created by 45517 on 2016/7/22.
 */
public interface SearchPresenter {

    void search(String key);

    void LoadNextPage();

    void onDestroy();
}

