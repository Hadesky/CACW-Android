package com.hadesky.cacw.network;

import android.util.Log;

import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class CookieManager implements CookieJar
{

    private String session;
    private List<Cookie> mCookies= new ArrayList<>();

    public CookieManager()
    {
        session =  MyApp.getSessionManager().getSession();
        if(session!=null)
        {
            Cookie c = new Cookie.Builder().name("sessionId").value(session).domain(MyApp.getURL()).build();
            mCookies.add(c);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        if(url.toString().contains("/login"))
        {
            for(Cookie c:cookies)
            {
                if(c.name().equals("sessionId"))
                {
                    //本地保存cookie
                    MyApp.getSessionManager().saveSeesion(c.value());
                    session = c.value();
                    Log.e("tag","session get  "+session);
                    break;
                }
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        return mCookies;
    }
}
