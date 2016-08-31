package com.hadesky.cacw.network;

/**
 *
 * Created by dzysg on 2016/8/31 0031.
 */
public class BaseResult<T>
{
    private String error_msg;
    private int state_code;
    private T data;

    @Override
    public String toString()
    {
        return "BaseResult{" +
                "state_code=" + state_code +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }

    public int getState_code()
    {
        return state_code;
    }

    public void setState_code(int state_code)
    {
        this.state_code = state_code;
    }
}
