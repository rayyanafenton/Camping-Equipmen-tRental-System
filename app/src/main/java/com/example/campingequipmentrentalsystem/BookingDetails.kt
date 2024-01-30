package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BookingDetails : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var id: String? = null
    private var equipmentName: String? = null
    private var equipmentPrice: String? = null
    private var equipmentStatus: String? = null
    private var equipmentDescription: String? = null
    private var imageUrl: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
        supportActionBar?.title = "Booking Details"

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = this.intent
        id = intent.getStringExtra("id")
        equipmentName = intent.getStringExtra("name")
        equipmentPrice = intent.getStringExtra("price")
        equipmentStatus = intent.getStringExtra("status")
        equipmentDescription = intent.getStringExtra("description")
        imageUrl = intent.getIntExtra("imageUrl", -1)

        val equipment_image: ImageView = findViewById(R.id.equipment_image)
        val equipment_name: TextView = findViewById(R.id.equipment_name)
        val equipment_price: TextView = findViewById(R.id.equipment_price)
        val pick_up_date: TextView = findViewById(R.id.pick_up_date)
        val return_date: TextView = findViewById(R.id.return_date)
        val dbHandler = DBHandler(this)

        val btnBooking: AppCompatButton = findViewById(R.id.rent_btn)

        equipment_name.text = equipmentName
        equipment_price.text = "$equipmentPrice /Day"
        if (imageUrl != null) {
            equipment_image.setImageResource(imageUrl!!)
        }

        btnBooking.setOnClickListener {
            val pickUpDateString = pick_up_date.text.toString()
            val returnDateString = return_date.text.toString()
            val totalDays = calculateDays(pickUpDateString, returnDateString)
            val totalCost = calculateTotalCost(equipmentPrice, totalDays)

            if (id != null) {
                dbHandler.insertBookingDetails(
                    userId,
                    id!!.toInt(),
                    "Ongoing",
                    pickUpDateString,
                    returnDateString,
                    totalCost.toString()
                )
            }

            id?.let { equipmentId ->
                dbHandler.updateEquipmentStatus(equipmentId, "Not Available")
            }

            startActivity(Intent(this@BookingDetails, Home::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDays(pickUpDate: String, returnDate: String): Long {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val startDate = LocalDate.parse(pickUpDate, formatter)
        val endDate = LocalDate.parse(returnDate, formatter)
        return ChronoUnit.DAYS.between(startDate, endDate)
    }

    private fun calculateTotalCost(pricePerDay: String?, totalDays: Long): Double {
        val cleanPrice = pricePerDay?.replace(Regex("[^\\d.]"), "") ?: return 0.0
        val dailyPrice = cleanPrice.toDoubleOrNull() ?: 0.0
        return dailyPrice * totalDays
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Create an Intent to start EquipmentDetails with the necessary data
        val backIntent = Intent(this, EquipmentDetails::class.java).apply {
            putExtra("id", id)
            putExtra("name", equipmentName)
            putExtra("price", equipmentPrice)
            putExtra("status", equipmentStatus)
            putExtra("description", equipmentDescription)
            putExtra("imageUrl", imageUrl ?: -1)
        }
        startActivity(backIntent)
        finish()
    }
}
