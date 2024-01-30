package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.example.campingequipmentrentalsystem.Adapter.BookingAdapter

class MyBookings : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Bookings"

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)

        val bookingList = findViewById<ListView>(R.id.booking_list)
        val dbHandler = DBHandler(this)

        val bookingHashMapList = dbHandler.getBookingDetails(userId)

        val adapter = BookingAdapter(this, bookingHashMapList)
        bookingList.adapter = adapter

        bookingList.setOnItemClickListener { _, _, position, _ ->
            val selectedBooking = bookingHashMapList[position]

            val intent = Intent(this, BookingSummary::class.java).apply {
                putExtra("id", selectedBooking.id)
                putExtra("name", selectedBooking.name)
                putExtra("price", selectedBooking.price)
                putExtra("status", selectedBooking.status)
                putExtra("description", selectedBooking.description)
                putExtra("imageUrl", selectedBooking.imageUrl)
                putExtra("pickupDate", selectedBooking.pickupDate)
                putExtra("returnDate", selectedBooking.returnDate)
                putExtra("totalPrice", selectedBooking.totalPrice)
                putExtra("equipment_id", selectedBooking.equipmentId)
            }
            startActivity(intent)
        }

        setListViewHeightBasedOnChildren(bookingList)

        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, bookingList)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.
        makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }
        val layoutParams = listView.layoutParams
        layoutParams.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = layoutParams
        listView.requestLayout()
    }
}
