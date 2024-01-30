package com.example.campingequipmentrentalsystem.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.campingequipmentrentalsystem.BookingData
import com.example.campingequipmentrentalsystem.R

class BookingAdapter(context: Context, bookingDataList: List<BookingData>) :

    ArrayAdapter<BookingData>(context, 0, bookingDataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.booking_list_card, parent, false)
        }

        val currentBooking = getItem(position)

        val equipmentName = listItemView?.findViewById<TextView>(R.id.equipment_name)
        val equipmentPrice = listItemView?.findViewById<TextView>(R.id.equipment_price)
        val bookingStatus = listItemView?.findViewById<TextView>(R.id.booking_status)
        val equipmentImage = listItemView?.findViewById<ImageView>(R.id.equipment_image)

        equipmentName?.text = currentBooking?.name
        equipmentPrice?.text = "Price: ${currentBooking?.totalPrice}"
        bookingStatus?.text = "${currentBooking?.pickupDate} - ${currentBooking?.returnDate}"

        equipmentImage?.setImageResource(currentBooking?.imageUrl ?: R.drawable.fan)

        return listItemView!!
    }
}


