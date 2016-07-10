package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.config.MyApp;

/**
 * Created by 45517 on 2015/11/22.
 */
public interface RegisterView extends BaseView{

    MyApp getMyApp();

    void disableGetCodeBt();

    void close();
}
