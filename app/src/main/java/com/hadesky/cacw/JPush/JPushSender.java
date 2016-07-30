package com.hadesky.cacw.JPush;

import android.support.annotation.IntRange;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dzysg on 2016/7/30 0030.
 */
public class JPushSender
{

    public static int AndroidPlatform = 1;
    public static int AllPlatform = 2;
    public String mBody;

    private JPushSender(String body)
    {
        mBody = body;
        Log.e("tag", mBody);
    }

    @Override
    public String toString()
    {
        return mBody;
    }

    public static class SenderBuilder
    {
        JSONObject mObject;
        private String mPlatform;
        private JSONObject mAudience;
        private String mMessageTitle = "";
        private String mMessageContent = "";
        private JSONObject mMessage;
        private JSONArray mDeviceIds;
        private JSONArray mTags;
        private Map<String, String> mMsgExtra;


        public SenderBuilder()
        {
            mObject = new JSONObject();
        }

        public SenderBuilder Platform(@IntRange(from = 1, to = 2) int s)
        {
            if (s == 1)
                mPlatform = "android";
            else if (s == 2)
                mPlatform = "all";
            else
                throw new IllegalArgumentException("Platform argument error");
            return this;
        }

        public SenderBuilder addDeviceId(String id)
        {
            if (mDeviceIds == null)
                mDeviceIds = new JSONArray();
            mDeviceIds.put(id);
            return this;
        }

        public SenderBuilder Message(String title, String content)
        {
            mMessageTitle = title;
            mMessageContent = content;
            return this;
        }

        public SenderBuilder MessageExtra(String key, String value)
        {
            if (mMsgExtra == null)
                mMsgExtra = new HashMap<>();
            mMsgExtra.put(key, value);

            return this;
        }


        public SenderBuilder addTag(String tag)
        {

            if (mTags == null)
                mTags = new JSONArray();
            mTags.put(tag);

            return this;
        }


        public JPushSender build()
        {
            try
            {
                if (mPlatform == null)
                    mPlatform = "android";

                if (mDeviceIds!=null||mTags!=null)
                {
                    mAudience = new JSONObject();
                    if (mDeviceIds != null)
                        mAudience.accumulate("registration_id", mDeviceIds);
                    if (mTags != null)
                        mAudience.accumulate("tag", mTags);
                }


                mMessage = new JSONObject();
                mMessage.accumulate("msg_content", mMessageContent);
                mMessage.accumulate("title", mMessageTitle);
                mMessage.accumulate("content_type", "text");

                if (mMsgExtra != null)
                {
                    JSONObject extra = new JSONObject();
                    for(HashMap.Entry<String, String> m : mMsgExtra.entrySet())
                    {
                        extra.accumulate(m.getKey(), m.getValue());
                    }
                    mMessage.accumulate("extras", extra);
                }

                mObject = new JSONObject();
                mObject.accumulate("platform", mPlatform);
                mObject.put("audience", mAudience == null ? "all" : mAudience);
                mObject.put("message", mMessage);

            }
            catch (Exception e)
            {
                Log.e("tag", e.getMessage());
                return null;
            }
            return new JPushSender(mObject.toString());
        }
    }
}
