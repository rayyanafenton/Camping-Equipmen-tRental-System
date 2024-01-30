package com.example.campingequipmentrentalsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class BookingSummary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_summary)
        supportActionBar?.title = "Booking Summary"
        val dbHandler = DBHandler(this)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val status = intent.getStringExtra("status")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getIntExtra("imageUrl", -1)
        val pickupDate = intent.getStringExtra("pickupDate")
        val returnDate = intent.getStringExtra("returnDate")
        val totalPrice = intent.getStringExtra("totalPrice")
        val equipmentId = intent.getStringExtra("equipment_id")

        val equipmentImage: ImageView = findViewById(R.id.equipment_image)
        val equipmentName: TextView = findViewById(R.id.equipment_name)
        val equipmentPrice: TextView = findViewById(R.id.equipment_price)
        val pickUpDateView: TextView = findViewById(R.id.pick_up_date)
        val returnDateView: TextView = findViewById(R.id.return_date)
        val totalPriceView: TextView = findViewById(R.id.total_price)

        equipmentName.text = name
        equipmentPrice.text = "Price: $price"
        pickUpDateView.text = "Pick up date: $pickupDate"
        returnDateView.text = "Return date: $returnDate"
        totalPriceView.text = "Total price: $totalPrice"

        if (imageUrl != -1) {
            equipmentImage.setImageResource(imageUrl)
        }

        val cancelBtn: AppCompatButton = findViewById(R.id.cancel_btn)
        val returnBtn: AppCompatButton = findViewById(R.id.return_btn)

        cancelBtn.setOnClickListener {
                if (id != null) {
                    dbHandler.updateBookingStatus(id, "Unsuccessful")
                    dbHandler.deleteBooking(id)
                    if (equipmentId != null) {
                        dbHandler.updateEquipmentStatus(equipmentId, "Available")
                    }
                }
            val intent = Intent(this@BookingSummary, MyBookings::class.java)
            startActivity(intent)
        }

        returnBtn.setOnClickListener {
            if (id != null) {
                dbHandler.updateBookingStatus(id, "Completed")
                dbHandler.deleteBooking(id)
                if (equipmentId != null) {
                    dbHandler.updateEquipmentStatus(equipmentId, "Available")
                }
            }
            val intent = Intent(this@BookingSummary, MyBookings::class.java)
            startActivity(intent)
        }
    }

}
