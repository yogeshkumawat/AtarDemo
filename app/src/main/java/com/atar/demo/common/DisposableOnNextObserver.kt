package com.atar.demo.common

import io.reactivex.observers.DisposableObserver

abstract class DisposableOnNextObserver<T> :
    DisposableObserver<T>() {
    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {}
}