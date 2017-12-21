package com.ecjia.component.network.base;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-07.
 */

public abstract class RSubscriberAbstract<T> extends ResourceSubscriber <BaseRes<T>> {


    @Override
    public void onNext(BaseRes<T> data) {
        _onNext(data.getData(),data.getPaginated());
    }

    @Override
    public void onError(Throwable t) {
        _onError(t);
    }

    @Override
    public void onComplete() {

    }

    public abstract void _onNext(T t, BaseRes.Paginated page);//onNext()

    public abstract void _onError(Throwable t);//onNext()
}
