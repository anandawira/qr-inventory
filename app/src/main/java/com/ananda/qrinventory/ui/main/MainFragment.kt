package com.ananda.qrinventory.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ananda.qrinventory.AuditActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val result = 0
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), result)
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
            ref.push()
                .setValue(inventoryTag)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Data sent to database", Toast.LENGTH_SHORT)
                        .show()
                }
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

        show_scanner_button.setOnClickListener {
            if (scanner_view.isVisible) {
                scanner_view.visibility = View.GONE
                scanner_view.stopCamera()
            } else {
                startQRScanner()
            }
        }

        fab.setOnClickListener {
            val intent = Intent(activity, AuditActivity::class.java)
            startActivity(intent)
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

    private fun startQRScanner() {
        scanner_view.startCamera()
        scanner_view.visibility = View.VISIBLE
        scanner_view.resumeCameraPreview {
            val result = it.text.split("-")
            if (result.size == 4) {
                model_edit_text.setText(result[0])
                monogram_edit_text.setText(result[1])
                spec_edit_text.setText(result[2])
            } else {
                Toast.makeText(activity, "Incorrect qr code", Toast.LENGTH_SHORT)
                    .show()
            }

            scanner_view.visibility = View.GONE
            scanner_view.stopCamera()
        }
    }


}
