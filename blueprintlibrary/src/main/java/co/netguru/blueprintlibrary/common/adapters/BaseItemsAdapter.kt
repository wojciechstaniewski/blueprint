package co.netguru.blueprintlibrary.common.adapters

class BaseItemsAdapter(val data: List<LayoutItemAdapter>,
                       private val bindData: (layoutItemAdapter: LayoutItemAdapter) -> LayoutItemAdapter,
                       viewModelVariableId: Int, layoutItemTypes: List<LayoutItemType>) :
        DataBindingBaseAdapter(viewModelVariableId,layoutItemTypes) {
    override fun bind(layoutItemAdapter: LayoutItemAdapter): LayoutItemAdapter {
        return bindData(layoutItemAdapter)
    }

    override fun getObjForPosition(position: Int): LayoutItemAdapter = data[position]

    override fun getItemCount(): Int = data.size

    override fun getLayoutIdForPosition(position: Int): Int = data[position].type.layoutRes

}