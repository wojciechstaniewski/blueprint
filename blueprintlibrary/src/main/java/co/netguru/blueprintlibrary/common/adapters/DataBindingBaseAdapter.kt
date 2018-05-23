package co.netguru.blueprintlibrary.common.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


abstract class DataBindingBaseAdapter(private val viewModelVariableId: Int,
                                      private val layoutItemTypes: List<LayoutItemType>)
    : RecyclerView.Adapter<DataBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DataBindingViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
                viewType, parent, false)
        return DataBindingViewHolder(binding, viewModelVariableId, layoutItemTypes)
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