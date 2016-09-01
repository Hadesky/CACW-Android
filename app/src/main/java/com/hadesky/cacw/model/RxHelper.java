package com.hadesky.cacw.model;

import com.hadesky.cacw.network.BaseResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class RxHelper
{

    public static <T> Observable.Transformer<T, T> io_main()
    {
        return new Observable.Transformer<T, T>()
        {
            @Override
            public Observable<T> call(Observable<T> tObservable)
            {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public static <T> Observable.Transformer<BaseResult<T>, T> handleResult() {

        return new Observable.Transformer<BaseResult<T>, T>() {

            @Override
            public Observable<T> call(Observable<BaseResult<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResult<T> result) {
                        if (result.getState_code()==0) {
                            return Observable.just(result.getData());
                        } else {
                            return Observable.error(new Throwable(result.getError_msg()));
                        }
                    }
                });
            }
        };
    }
}
