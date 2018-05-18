package co.netguru.blueprint.main.viewmodel

import android.arch.lifecycle.ViewModel
import android.os.Parcelable
import co.netguru.blueprint.main.navigation.MainNavigationHelper
import co.netguru.blueprintlibrary.Repository
import co.netguru.blueprintlibrary.common.events.ActionEvent
import javax.inject.Inject


class MainViewModel : ViewModel() {


    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var mainNavigationHelper: MainNavigationHelper

    val nextEvent = ActionEvent<Boolean>()

    val adminAndCandidateEvent = ActionEvent<Parcelable>()

    override fun onCleared() {
        super.onCleared()
    }


}