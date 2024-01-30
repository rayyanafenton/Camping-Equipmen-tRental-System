package com.example.campingequipmentrentalsystem.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.campingequipmentrentalsystem.Data.EquipmentData
import com.example.campingequipmentrentalsystem.R

class EquipmentAdapter(context: Context, equipmentDataList: List<EquipmentData>) :
    ArrayAdapter<EquipmentData>(context, 0, equipmentDataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context)
                .inflate(R.layout.booking_list_card, parent, false)
        }

        val currentEquipment = getItem(position)

        val equipmentImage = listItemView?.findViewById<ImageView>(R.id.equipment_image)
        val equipmentName = listItemView?.findViewById<TextView>(R.id.equipment_name)
        val equipmentPrice = listItemView?.findViewById<TextView>(R.id.equipment_price)
        val bookingStatus = listItemView?.findViewById<TextView>(R.id.booking_status)

        equipmentImage?.setImageResource(currentEquipment?.imageUrl ?: R.drawable.fan)
        equipmentName?.text = currentEquipment?.name
        equipmentPrice?.text = currentEquipment?.price
        bookingStatus?.text = currentEquipment?.status

        return listItemView!!
    }
}
