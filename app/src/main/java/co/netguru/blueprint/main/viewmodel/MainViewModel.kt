package co.netguru.blueprint.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.util.Log
import co.netguru.blueprint.services.pet.business.PetManager
import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprint.services.pet.domain.Status
import co.netguru.blueprintlibrary.Repository
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.adapters.LayoutItemAdapter
import co.netguru.blueprintlibrary.common.events.ActionEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel : ViewModel() {


    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var petManager: PetManager

    val nextEvent = ActionEvent<Boolean>()

    val allPetsEvent = ActionEvent<List<Pet>>()

    val allPets: MutableLiveData<List<Pet>> = MutableLiveData()

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

    fun getAllPets() {
        compositeDisposable.add(Single.merge(
                petManager.findByStatus(Status.AVAILABLE).subscribeOn(Schedulers.io()),
                petManager.findByStatus(Status.PENDING).subscribeOn(Schedulers.io()),
                petManager.findByStatus(Status.SOLD).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                    allPetsEvent.onSuccess(result)
                }, { e ->
                    Log.e(MainViewModel::class.java.simpleName, e.message)
                    allPetsEvent.onError(e)
                }))
    }
}