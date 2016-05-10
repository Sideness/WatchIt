package com.ktung.watchit.util;

import rx.Observer;
import timber.log.Timber;

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, e.getMessage());
    }
}
