package com.hadesky.cacw.model;

import rx.Subscriber;

/**
 * Created by dzysg on 2016/9/1 0001.
 */
public abstract class  RxSubscriber<T> extends Subscriber<T>
{

    abstract void onError(String msg);

    @Override
    public void onCompleted()
    {

    }

    @Override
    public void onError(Throwable e)
    {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t)
    {

    }
}
