package com.hadesky.cacw.model;

import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.network.BaseResult;
import com.hadesky.cacw.model.network.SessionException;

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
                        //如果成功,反回正常数据
                        if (result.getState_code()==0) {
                            return Observable.just(result.getData());
                        }
                        //如果session失效
                        else if(result.getState_code()==3)
                        {
                            MyApp.getSessionManager().saveSeesion(null);
                            return Observable.error(new SessionException(result.getError_msg()));
                        }
                        else {
                            return Observable.error(new RuntimeException(result.getError_msg()));
                        }
                    }
                });
            }
        };
    }
}
