package co.netguru.blueprintlibrary.common.events

interface ViewModelEvent<T> {

    fun onStartProgress()
    fun onSuccess(t: T)
    fun onError(t: Throwable)
}