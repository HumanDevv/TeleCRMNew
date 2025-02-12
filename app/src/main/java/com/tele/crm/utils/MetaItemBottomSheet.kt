package com.tele.crm.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tele.crm.R
import com.tele.crm.utils.dialog.MetaItemAdapter

class MetaItemBottomSheet<T>(
    private val context: Context,
    private val itemList: List<T>,
    private val onItemSelected: (T) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), com.google.android.material.R.style.Theme_MaterialComponents_Light_BottomSheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_meta_item, container, false) // Use the same layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvMetaItems)
        val ivClose: ImageView = view.findViewById(R.id.iv_close)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the adapter to handle any type of list
        recyclerView.adapter = MetaItemAdapter(itemList, onItemSelected,this)

        ivClose.setOnClickListener {
            dismiss()  // Close the dialog when the close button is clicked
        }
    }
}
