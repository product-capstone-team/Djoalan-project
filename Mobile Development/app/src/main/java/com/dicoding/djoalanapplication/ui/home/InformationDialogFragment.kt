package com.dicoding.djoalanapplication.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.dicoding.djoalanapplication.R

class InformationDialogFragment: DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_info, null))
                .create()


        } ?: throw IllegalStateException("Activity Cannot be null")
    }

    companion object {
        const val TAG = "InformationDialog"
    }

}