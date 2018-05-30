package co.netguru.blueprint.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.util.Log
import co.netguru.blueprint.services.pet.business.PetManager
import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.adapters.LayoutItemAdapter
import co.netguru.blueprintlibrary.common.events.ActionEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel : ViewModel() {

    @Inject
    lateinit var petManager: PetManager

    val nextEvent = ActionEvent<Boolean>()

    val petsEvent = ActionEvent<Unit>()

    val pets: MutableLiveData<List<Pet>> = MutableLiveData()

    internal var items: MutableList<LayoutItemAdapter> = arrayListOf()

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun handleStartClick() {
        nextEvent.onStartProgress()
    }

    fun waitUntilAnimationFinish() {
        val runnable = {
            nextEvent.onSuccess(true)
        }
        Handler().postDelayed(runnable, Constants.ONE_SECOND)
    }

    fun getPets(status: Status) {
        items.clear()
        compositeDisposable.add(
                petManager.findByStatus(status).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                            pets.postValue(result)
                        }, { e ->
                            Log.e(MainViewModel::class.java.simpleName, e.message)
                            petsEvent.onError(e)
                        }))
    }
}