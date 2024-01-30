package com.example.campingequipmentrentalsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)
        supportActionBar?.title = "Admin Page"

        val btnViewUsers: Button = findViewById(R.id.btnViewUsers)
        val btnViewEquipment: Button = findViewById(R.id.btnViewEquipment)
        val btnViewBookings: Button = findViewById(R.id.btnViewBookings)

        btnViewUsers.setOnClickListener {
            val intent = Intent(this@AdminPage, UserListActivity::class.java)
            startActivity(intent)
        }

        btnViewEquipment.setOnClickListener {
            val intent = Intent(this@AdminPage, EquipmentListActivity::class.java)
            startActivity(intent)
        }

        btnViewBookings.setOnClickListener {
            val intent = Intent(this@AdminPage, BookingListActivity::class.java)
            startActivity(intent)
        }
    }
}
