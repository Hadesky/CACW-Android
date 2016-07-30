package com.hadesky.cacw.JPush;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by dzysg on 2016/7/30 0030.
 */
public class JPushManager
{

    private OkHttpClient mOkHttpClient;
    private String key;
    private static String mAppkey;
    private static String mSecret;

    public static void init(String appKey, String secret)
    {
        mAppkey = appKey;
        mSecret = secret;
    }


    public JPushManager(OkHttpClient client)
    {
        if (mAppkey == null || mSecret == null)
        {
            throw new RuntimeException("you must call JPushManager.init first");
        }
        mOkHttpClient = client;
        StringBuilder sb = new StringBuilder();
        sb.append(mAppkey).append(":").append(mSecret);
        //key = Base64.encodeToString(sb.toString().getBytes(),Base64.DEFAULT);
        // TODO: 2016/7/30 0030  调用Base64加密会产生非ascII码，无法添加到http header,这里的字符暂时写死
        key = "ZTFkZGY2YjdkM2JiOWM2YmEyNTQ1YTU1OmY4ZGQ1OGNmNDczNmU1OWQ1OGEyODQzMg==";
        key = "Basic " + key;
    }


    public Call sendMsg(JPushSender sender)
    {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sender.toString());
        Request request = new Request.Builder()
                .url("https://api.jpush.cn/v3/push")
                .addHeader("Authorization",key)
                .post(body).build();
        return mOkHttpClient.newCall(request);
    }

}
