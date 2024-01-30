package com.example.campingequipmentrentalsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.campingequipmentrentalsystem.Adapter.EquipmentAdapter

class EquipmentListActivity : AppCompatActivity() {

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_list)
        supportActionBar?.title = "Equipment List"

        dbHandler = DBHandler(this)
        val equipmentListView: ListView = findViewById(R.id.equipmentListView)

        val equipmentList = dbHandler.getEquipmentDetails()

        val adapter = EquipmentAdapter(this, equipmentList)
        equipmentListView.adapter = adapter
    }
}

