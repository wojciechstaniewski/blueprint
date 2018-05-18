package co.netguru.blueprintlibrary.common.view

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.fragmentargs.FragmentArgs
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : ViewModel, S : ViewDataBinding> constructor(private val layoutResId: Int) : ErrorHandlingFragment() {

    lateinit var baseBinding: S

    lateinit var baseViewModel: T

    var compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        setVariables()
        baseBinding.executePendingBindings()
        return baseBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    abstract fun setVariables()
}