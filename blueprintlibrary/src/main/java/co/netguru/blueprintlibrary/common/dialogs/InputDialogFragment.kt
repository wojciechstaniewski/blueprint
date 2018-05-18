package co.netguru.blueprintlibrary.common.dialogs

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants
import co.netguru.blueprintlibrary.common.events.ActionEvent

class InputDialogFragment : AppCompatDialogFragment() {

    val submitInputValueEvent = ActionEvent<String>()

    companion object {
        val tag = InputDialogFragment::class.java.simpleName!!

        fun newInstance(inputDialogMessages: InputDialogMessages): InputDialogFragment {
            val fragment = InputDialogFragment()
            val args = Bundle()
            args.putSerializable(Constants.DIALOG_PARAMS, inputDialogMessages)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dialogParams = arguments?.getSerializable(Constants.DIALOG_PARAMS) as InputDialogMessages
        val rootView = inflater.inflate(R.layout.input_dialog_fragment, container, false)
        rootView.findViewById<AppCompatTextView>(R.id.input_dialog_title).text =
                getString(dialogParams.titleId)
        val inputEditText: TextInputEditText = rootView.findViewById(R.id.input_dialog_edit_text)
        inputEditText.hint = getString(dialogParams.hintId)
        initPositiveButton(rootView.findViewById(R.id.input_dialog_positive_button), inputEditText,
                dialogParams.positiveButtonTextId)
        initNegativeButton(rootView.findViewById(R.id.input_dialog_negative_button),
                dialogParams.negativeButtonTextId)
        return rootView
    }

    private fun initPositiveButton(positiveButton: AppCompatTextView,
                                   inputEditText: TextInputEditText,
                                   positiveButtonTextId: Int) {
        positiveButton.text = activity?.getString(positiveButtonTextId)
        positiveButton.setOnClickListener({
            val inputValue = inputEditText.text.toString()
            if (inputValue.isNotBlank()) {
                submitInputValueEvent.onSuccess(inputValue)
                dialog.dismiss()
            }
        })
    }

    private fun initNegativeButton(negativeButton: AppCompatTextView, negativeButtonTextId: Int) {
        negativeButton.text = getString(negativeButtonTextId)
        negativeButton.setOnClickListener({ dialog.dismiss() })
    }

}