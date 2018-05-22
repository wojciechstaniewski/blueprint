package co.netguru.blueprintlibrary.common.utils

import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.dialogs.BottomSheetItem
import co.netguru.blueprintlibrary.common.dialogs.GeneralDialogFragment
import co.netguru.blueprintlibrary.common.dialogs.InputDialogFragment
import co.netguru.blueprintlibrary.common.dialogs.InputDialogMessages
import java.io.Serializable

class DialogUtils {

    fun showDialog(dialogParams: Serializable,
                   fragmentManager: FragmentManager): GeneralDialogFragment {
        val dialog = GeneralDialogFragment.newInstance(dialogParams)
        showDialogFragment(fragmentManager, dialog, GeneralDialogFragment.tag)
        return dialog
    }

    fun createBottomSheetDialog(inflater: LayoutInflater, container: ViewGroup?,
                                items: List<BottomSheetItem>): BottomSheetDialog {
        val context = container!!.context
        val dialog = BottomSheetDialog(context)
        val bottomSheetMenu = inflater.inflate(R.layout.bottom_sheet_menu, container, false)
        val itemsContainer = bottomSheetMenu.findViewById<LinearLayout>(R.id.bottom_sheet_menu_items_container)
        for (item in items) {
            val itemView = createBottomSheetItem(inflater, container, item.iconResId, item.titleResId)
            itemView.setOnClickListener {
                dialog.dismiss()
                item.clickListener.onClick(it)
            }
            itemsContainer.addView(itemView)
        }
        dialog.setContentView(bottomSheetMenu)
        return dialog
    }

    private fun createBottomSheetItem(inflater: LayoutInflater, container: ViewGroup, imageResId: Int, titleResId: Int): View {
        val item: View = inflater.inflate(R.layout.bottom_sheet_menu_item, container, false)
        item.findViewById<AppCompatImageView>(R.id.bottom_sheet_menu_option_icon)
                .setImageResource(imageResId)
        item.findViewById<AppCompatTextView>(R.id.bottom_sheet_menu_option_title)
                .text = container.context.getString(titleResId)
        return item
    }

    fun showInputDialogFragment(dialogParams: InputDialogMessages,
                                fragmentManager: FragmentManager): InputDialogFragment {
        val inputDialogFragment = InputDialogFragment.newInstance(dialogParams)
        inputDialogFragment.isCancelable = false
        showDialogFragment(fragmentManager, inputDialogFragment, InputDialogFragment.tag)
        return inputDialogFragment
    }

    private fun showDialogFragment(fragmentManager: FragmentManager,
                                   dialog: AppCompatDialogFragment, tag: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentManager.findFragmentByTag(tag) != null) {
            dialog.show(fragmentTransaction, tag)
        } else {
            fragmentTransaction.add(dialog, tag)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

}