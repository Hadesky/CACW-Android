package com.hadesky.cacw.model.network;

import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/** Cookie管理 ，用于okhttp
 * Created by dzysg on 2016/8/31 0031.
 */
public class CookieManager implements CookieJar
{

    private String session;
    private static List<Cookie> mCookies = new ArrayList<>();

    public static void clearCookie()
    {

        MyApp.getSessionManager().clear();
        mCookies.clear();
    }

    public CookieManager()
    {
        initCookie();
    }


    private void initCookie()
    {
        session = MyApp.getSessionManager().getSession();
        if (session != null)
        {
            Cookie c = new Cookie.Builder().name("sessionId").value(session).domain("192.168.199.234").build();
            if(mCookies==null)
                mCookies = new ArrayList<>();
            mCookies.add(c);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        if (url.toString().contains("/login"))
        {
            for(Cookie c : cookies)
            {
                if (c.name().equals("sessionId"))
                {
                    //本地保存cookie
                    MyApp.getSessionManager().saveSeesion(c.value());
                }else if(c.name().equals("uid"))
                {
                    //本地保存userid
                    MyApp.getSessionManager().setCurrentUser(c.value());
                    MyApp.setCurrentId(Integer.parseInt(c.value()));
                }
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        if (mCookies == null || mCookies.size() == 0)
            initCookie();
        return mCookies;
    }
}
