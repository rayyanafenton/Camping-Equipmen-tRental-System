package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campingequipmentrentalsystem.Adapter.EquipmentAdapter
import com.example.campingequipmentrentalsystem.Data.EquipmentData

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        val welcome = findViewById<TextView>(R.id.welcome_text)
        val bookingIcon: ImageView = findViewById(R.id.booking)
        val profileIcon: ImageView = findViewById(R.id.user_profile)
        val equipmentList = findViewById<ListView>(R.id.equipment_list)

        val userDetails = retrieveUserDetails()
        val username = userDetails["Username"] ?: ""
        welcome.text = "Welcome, $username"

        bookingIcon.setOnClickListener {
            val intent = Intent(this, MyBookings::class.java)
            startActivity(intent)
        }

        profileIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        val dbHandler = DBHandler(this)
        var equipmentDataList = dbHandler.getEquipmentDetails()
        if (equipmentDataList.isEmpty()) {
            populateInitialData(dbHandler)
            equipmentDataList = dbHandler.getEquipmentDetails()
        }

        val adapter = EquipmentAdapter(this, equipmentDataList)
        equipmentList.adapter = adapter
        equipmentList.isClickable = true
        setListViewHeightBasedOnChildren(equipmentList)

        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, equipmentList)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        equipmentList.setOnItemClickListener { _, _, position, _ ->
            val selectedEquipment = equipmentDataList[position]
            val intent = Intent(this@Home, EquipmentDetails::class.java).apply {
                putExtra("id", selectedEquipment.id)
                putExtra("name", selectedEquipment.name)
                putExtra("description", selectedEquipment.description)
                putExtra("status", selectedEquipment.status)
                putExtra("price", selectedEquipment.price)
                putExtra("imageUrl", selectedEquipment.imageUrl)
            }
            startActivity(intent)
        }

    }

    private fun retrieveUserDetails(): HashMap<String, String> {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return hashMapOf(
            "id" to sharedPreferences.getInt("id", -1).toString(),
            "Username" to (sharedPreferences.getString("Username", "") ?: ""),
            "Email" to (sharedPreferences.getString("Email", "") ?: ""),
            "Password" to (sharedPreferences.getString("Password", "") ?: ""),
            "PhoneNumber" to (sharedPreferences.getString("PhoneNumber", "") ?: ""),
            "Address" to (sharedPreferences.getString("Address", "") ?: "")
        )
    }

    private fun populateInitialData(dbHandler: DBHandler) {
        val initialData = listOf(
            EquipmentData("1", "Ball Tent", "A playful and vibrant Ball Tent designed with a height of 200cm, providing kids the joy of jumping inside the tent!! The triangular transparent PVC roof window allows natural sunlight to illuminate the interior.", "Available", "RM75.00", R.drawable.ball_tent),
            EquipmentData("2", "Camping Carpet", "Enhance your camping experience with our Camping Carpet. This versatile accessory adds comfort to your camping site, making it a perfect choice for outdoor relaxation.", "Available", "RM50.00", R.drawable.camping_carpet),
            EquipmentData("3", "Chair", "A basic camping chair designed for comfort and convenience. Take a seat and relax by the campfire or use it for various outdoor activities. The lightweight design makes it easy to carry on your adventures.", "Available", "RM20.00", R.drawable.chair), // Added "Available" status
            EquipmentData("4", "Dome Shelter", "Experience spacious camping with our Dome Shelter. This shelter offers a comfortable and weather-resistant space for your outdoor adventures. Its innovative design provides ample room for various activities.", "Available", "RM120.00", R.drawable.dome_shelter),
            EquipmentData("5", "Fan", "Beat the heat during your camping trips with our portable Camp Fan. Compact and efficient, this fan ensures a cool and comfortable environment in your tent or campsite.", "Available", "RM30.00", R.drawable.fan),
            EquipmentData("6", "Kermit", "The Kermit camping tent is your go-to choice for lightweight and compact shelter. Perfect for solo camping or backpacking adventures, it provides a cozy space to rest after a day of exploration.", "Available", "RM90.00", R.drawable.kermit),
            EquipmentData("7", "Mosquito Hammock", "Ensure a peaceful night's sleep with our Mosquito Hammock. This hammock is designed to keep pesky mosquitoes at bay, allowing you to relax and enjoy the serenity of the outdoors.", "Available", "RM40.00", R.drawable.mosquito_hammock),
            EquipmentData("8", "MXPro Tent", "Elevate your camping experience with the MXPro Tent. This professional-grade tent offers durability and functionality, ensuring a comfortable and secure shelter during your outdoor adventures.", "Available", "RM150.00", R.drawable.mxpro),
            EquipmentData("9", "Roll Egg Table", "Add convenience to your campsite with the Roll Egg Table. This folding table is lightweight and easy to carry, providing a practical surface for meals, games, or other outdoor activities.", "Available", "RM25.00", R.drawable.roll_egg_table),
            EquipmentData("10", "Tilam", "Enjoy a good night's sleep with our comfortable Tilam (mattress). Designed for camping, this mattress provides the support you need for a restful night under the stars.", "Available", "RM45.00", R.drawable.tilam),
            EquipmentData("11", "Triangle Hanging", "Elevate your camping ambiance with the Triangle Hanging decoration. Hang it in your tent or campsite to add a touch of style and create a cozy atmosphere during your outdoor retreat.", "Available", "RM15.00", R.drawable.triangle_hanging),
            EquipmentData("12", "Two Bedrooms Tent", "Experience luxury camping with our Two Bedrooms Tent. Spacious and well-designed, this tent provides separate sleeping areas, ensuring comfort and privacy during your camping adventure.", "Available", "RM200.00", R.drawable.two_bedrooms_tent)
        )
        initialData.forEach { dbHandler.insertEquipmentDetails(it) }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
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