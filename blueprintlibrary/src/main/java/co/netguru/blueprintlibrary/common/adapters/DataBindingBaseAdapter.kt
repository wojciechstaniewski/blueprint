package co.netguru.blueprintlibrary.common.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


abstract class DataBindingBaseAdapter(private val viewModelVariableId: Int,
                                      private val items: List<LayoutItemAdapter>)
    : RecyclerView.Adapter<DataBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBindingViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
                viewType, parent, false)

        val layoutItemsTypes = createLayoutTypesFromData()

        return DataBindingViewHolder(binding, viewModelVariableId, layoutItemsTypes)
    }

    private fun createLayoutTypesFromData(): ArrayList<LayoutItemType> {
        val layoutItemsTypes = arrayListOf<LayoutItemType>()

        items.forEach {
            val resId = it.type.layoutRes
            if (layoutItemsTypes.size > 0) {
                if (layoutItemsTypes.find { it.layoutRes == resId } == null) {
                    layoutItemsTypes.add(it.type)
                }
            } else {
                layoutItemsTypes.add(it.type)
            }
        }
        return layoutItemsTypes
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder,
                                  position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(bind(obj))
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    protected abstract fun getObjForPosition(position: Int): LayoutItemAdapter

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun bind(layoutItemAdapter: LayoutItemAdapter): LayoutItemAdapter

}