package co.netguru.blueprintlibrary.common.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class DataBindingViewHolder constructor(private val binding: ViewDataBinding, private val variableId: Int) : RecyclerView.ViewHolder(binding.root) {


    fun bind(obj: LayoutItemAdapter) {
        LayoutItemType.values().find {
            binding.setVariable(variableId, obj)
        }


        //when (obj.type) {


//            LayoutItemType.PORTFOLIO_ITEM -> binding.setVariable(BR.portfolioItemViewModel, obj)
//            LayoutItemType.PORTFOLIO_GENERAL_INFO_SECTION -> binding.setVariable(BR.portfolioGeneralInfoViewModel, obj)
//            LayoutItemType.PORTFOLIO_HEADER_SECTION -> binding.setVariable(BR.portfolioHeaderSectionViewModel, obj)
//            LayoutItemType.PORTFOLIO_FOOTER_SECTION -> binding.setVariable(BR.portfolioFooterSectionViewModel, obj)
//            LayoutItemType.CHAT_CARD -> binding.setVariable(BR.chatCardItemViewModel, obj)
//            LayoutItemType.CHAT_CAROUSEL_CARD -> binding.setVariable(BR.chatCardItemViewModel, obj)
//            LayoutItemType.CARD -> binding.setVariable(BR.cardItemViewModel, obj)
//            LayoutItemType.CAROUSEL_CARDS -> binding.setVariable(BR.carouselViewModel, obj)
//            LayoutItemType.EVENT_CARD -> binding.setVariable(BR.jobInterviewCardViewModel, obj)
//            LayoutItemType.CHAT_BUBBLE -> binding.setVariable(BR.chatBubbleViewModel, obj)
//            LayoutItemType.CARD_DETAILS_SECTION -> binding.setVariable(BR.cardsDetailsSection, obj)
//            LayoutItemType.APPLY_JOB_BUTTON -> binding.setVariable(BR.applyJobButtonViewModel, obj)
//            LayoutItemType.RECRUITMENT_PROCESS -> binding.setVariable(BR.recruitmentProcessViewModel, obj)
//            LayoutItemType.LOGIN_DATA_SECTION -> binding.setVariable(BR.loginDataViewModel, obj)
        //}
        binding.executePendingBindings()
    }
}
