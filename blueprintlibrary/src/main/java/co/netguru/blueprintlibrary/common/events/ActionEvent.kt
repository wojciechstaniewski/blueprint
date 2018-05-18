package co.netguru.blueprintlibrary.common.events

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

open class ActionEvent<T> : ViewModelEvent<T> {

    fun getStartProgressEvent(): Flowable<Boolean> {
        return startProgressRelay.toFlowable(BackpressureStrategy.LATEST)
    }

    fun getSuccessStream(): Flowable<T> {
        return successRelay.toFlowable(BackpressureStrategy.LATEST)
    }

    fun getErrorStream(): Flowable<Throwable> {
        return errorRelay.toFlowable(BackpressureStrategy.LATEST)
    }

    var startProgressRelay: PublishRelay<Boolean> = PublishRelay.create()
    var successRelay: PublishRelay<T> = PublishRelay.create()
    var errorRelay: PublishRelay<Throwable> = PublishRelay.create()

    override fun onSuccess(t: T) {
        successRelay.accept(t)
    }

    override fun onError(t: Throwable) {
        errorRelay.accept(t)
    }

    override fun onStartProgress() {
        startProgressRelay.accept(true)
    }
}