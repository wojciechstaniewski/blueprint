package co.netguru.blueprintlibrary.common.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class DataBindingViewHolder constructor(private val binding: ViewDataBinding,
                                        private val viewModelVariableId: Int,
                                        private val layoutItemTypes: List<LayoutItemType>)
    : RecyclerView.ViewHolder(binding.root) {


    fun bind(obj: LayoutItemAdapter) {
        layoutItemTypes.find {
            binding.setVariable(viewModelVariableId, obj)
        }
        binding.executePendingBindings()
    }
}
