package com.ecjia.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 原来的两个BaseFragment中都有ECJiaMainActivity
 * ECJiaMainActivity并不需要使用，所以重新新建了一个BaseFragment
 * Created by YichenZ on 2017/2/9 10:18.
 */

public class NewBaseFragment extends Fragment {
        private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

        @NonNull
        @CheckResult
        public final Observable<FragmentEvent> lifecycle() {
            return lifecycleSubject.hide();
        }

        public final <T> LifecycleTransformer<T> liToDestroy() {
            return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
        }

        @NonNull
        @CheckResult
        public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
            return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
        }

        @NonNull
        @CheckResult
        public final <T> LifecycleTransformer<T> bindToLifecycle() {
            return RxLifecycleAndroid.bindFragment(lifecycleSubject);
        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
                lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
                lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
                lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
                lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
                lifecycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
                lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
                lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
                lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
                lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }
}
