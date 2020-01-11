package com.ananda.qrinventory.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ananda.qrinventory.InventoryTag
import com.ananda.qrinventory.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val ref = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        btn_insert.setOnClickListener {
            val inventoryTag = InventoryTag(
                model = model_edit_text.text.toString(),
                monogram = monogram_edit_text.text.toString(),
                spec = spec_edit_text.text.toString(),
                lotNumber = lot_edit_text.text.toString(),
                quantity = quantity_edit_text.text.toString(),
                remarks = remarks_edit_text.text.toString()
            )
            ref.push().setValue(inventoryTag)
            Toast.makeText(activity, "Data sent to database", Toast.LENGTH_SHORT)
                .show()
            clearAll()
        }

        ArrayAdapter.createFromResource(
            activity!!.baseContext,
            R.array.battery_models,
            R.layout.dropdown_menu_popup_item
        )
            .also { adapter ->
                model_edit_text.setAdapter(adapter)
                model_edit_text.setOnClickListener {
                    model_edit_text.showDropDown()
                }
            }

        ArrayAdapter.createFromResource(
            activity!!.baseContext,
            R.array.battery_monograms,
            R.layout.dropdown_menu_popup_item
        )
            .also { adapter ->
                monogram_edit_text.setAdapter(adapter)
                monogram_edit_text.setOnClickListener {
                    monogram_edit_text.showDropDown()
                }
            }

        ArrayAdapter.createFromResource(
            activity!!.baseContext,
            R.array.battery_specs,
            R.layout.dropdown_menu_popup_item
        )
            .also { adapter ->
                spec_edit_text.setAdapter(adapter)
                spec_edit_text.setOnClickListener {
                    spec_edit_text.showDropDown()
                }
            }
    }

    private fun clearAll() {
        model_edit_text.setText("")
        monogram_edit_text.setText("")
        spec_edit_text.setText("")
        lot_edit_text.setText("")
        quantity_edit_text.setText("")
        remarks_edit_text.setText("")
    }


}
