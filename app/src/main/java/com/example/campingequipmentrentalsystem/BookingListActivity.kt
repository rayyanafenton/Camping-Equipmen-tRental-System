package com.example.campingequipmentrentalsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.campingequipmentrentalsystem.Adapter.BookingAdapter

class BookingListActivity : AppCompatActivity() {

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_list)
        supportActionBar?.title = "Booking List"

        dbHandler = DBHandler(this)
        val bookingsListView: ListView = findViewById(R.id.bookingsListView)

        val bookingList = dbHandler.getAllBookingDetails()

        val adapter = BookingAdapter(this, bookingList)
        bookingsListView.adapter = adapter
    }
}
