package co.netguru.blueprint.main.viewmodel

import android.arch.lifecycle.ViewModel
import co.netguru.blueprint.R
import co.netguru.blueprint.services.pet.domain.Pet
import co.netguru.blueprintlibrary.common.adapters.LayoutItemAdapter
import co.netguru.blueprintlibrary.common.adapters.LayoutItemType

class PetViewModel(val pet: Pet) : ViewModel(), LayoutItemAdapter {

    override val type: LayoutItemType
        get() = LayoutItemType(R.layout.pet_item_layout)
}