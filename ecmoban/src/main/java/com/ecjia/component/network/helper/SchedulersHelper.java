package com.ecjia.component.network.helper;


import com.ecjia.component.network.base.BaseRes;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

/**
 * Created by Android on 2016/6/16.
 */

public class SchedulersHelper {
    static String str = "暂无数据";

    public static <T> FlowableTransformer<BaseRes<T>, T> handleResult() {
        return responseJsonObservable -> responseJsonObservable.flatMap(
                dataJson -> {
                    if (dataJson == null) {
                        return Flowable.error(new Throwable(str));
                    } else if (dataJson.getStatus().getSucceed() == 0) {
                        return Flowable.error(new Throwable(dataJson.getStatus().getError_desc()));
                    } else {
                        Flowable.just(dataJson.getData());
                        return createData(dataJson.getData());
                    }
                });
    }
    public static <T> FlowableTransformer<BaseRes<T>, BaseRes<T>> handleResultJsonRet() {
        return responseJsonObservable -> responseJsonObservable.flatMap(
                dataJson -> {
                    if (dataJson == null) {
                        return Flowable.error(new Throwable(str));
                    } else if (dataJson.getStatus().getSucceed() == 0) {
                        return Flowable.error(new Throwable(dataJson.getStatus().getError_desc()));
                    } else {
                        return createData(dataJson);
                    }
                });
    }


    private static <T> Flowable<T> createData(T data) {
        return Flowable.create(subscriber -> {
            subscriber.onNext(data);
            subscriber.onComplete();
        }, BackpressureStrategy.ERROR);
    }
}
