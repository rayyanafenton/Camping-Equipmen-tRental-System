package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class EquipmentDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_details)
        supportActionBar?.title = "Equipment Details"

        val intent = this.intent
        val id = intent.getStringExtra("id")
        val equipmentName = intent.getStringExtra("name")
        val equipmentPrice = intent.getStringExtra("price")
        val equipmentStatus = intent.getStringExtra("status")
        val equipmentDescription = intent.getStringExtra("description")
        val imageUrl = intent.getIntExtra("imageUrl", -1)

        val equipment_image: ImageView = findViewById(R.id.equipment_image)
        val equipment_name = findViewById<TextView>(R.id.equipment_name)
        val equipment_price = findViewById<TextView>(R.id.equipment_price)
        val equipment_description = findViewById<TextView>(R.id.equipment_description)

        val btnRent: AppCompatButton = findViewById(R.id.rent_btn)

        equipment_name.text = equipmentName
        equipment_price.text = equipmentPrice + "/Day"
        equipment_description.text = equipmentDescription

        if (imageUrl != -1) {
            equipment_image.setImageResource(imageUrl)
        }

        if (equipmentStatus.equals("Not Available", ignoreCase = true)) {
            btnRent.isEnabled = false
            btnRent.setBackgroundResource(R.drawable.grey_bg)
        }

        btnRent.setOnClickListener {
            val intent = Intent(this@EquipmentDetails, BookingDetails::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", equipmentName)
            intent.putExtra("description", equipmentDescription)
            intent.putExtra("status", equipmentStatus)
            intent.putExtra("price", equipmentPrice)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
        }
    }
}