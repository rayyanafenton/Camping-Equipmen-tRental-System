package com.example.campingequipmentrentalsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton

class Profile : AppCompatActivity() {
    lateinit var dbHandler: DBHandler

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var phone_number: EditText
    private lateinit var address: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"

        dbHandler = DBHandler(this)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        phone_number = findViewById(R.id.phone_number)
        address = findViewById(R.id.address)
        password = findViewById(R.id.password)
        val editProfileBtn = findViewById<AppCompatButton>(R.id.edit_profile_btn)
        val logOutBtn = findViewById<AppCompatButton>(R.id.log_out_btn)

        loadUserDetails()

        editProfileBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("id", -1)
            updateUserDetails(
                userId,
                username.text.toString(),
                email.text.toString(),
                phone_number.text.toString(),
                address.text.toString(),
                password.text.toString()
            )
            val intent = Intent(this@Profile, Home::class.java)
            startActivity(intent)
        }

        logOutBtn.setOnClickListener {
            val intent = Intent(this@Profile, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserDetails() {
        val userDetails = retrieveUserDetails()
        username.setText(userDetails["Username"])
        email.setText(userDetails["Email"])
        phone_number.setText(userDetails["PhoneNumber"])
        address.setText(userDetails["Address"])
        password.setText(userDetails["Password"])
    }

    private fun updateUserDetails(userId: Int, username: String, email: String, phoneNumber: String, address: String, password: String) {
        dbHandler.updateUserDetails(userId.toString(), username, email, phoneNumber, address, password)
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Username", username)
        editor.putString("Email", email)
        editor.putString("PhoneNumber", phoneNumber)
        editor.putString("Address", address)
        editor.putString("Password", password)
        editor.apply()
    }

    private fun retrieveUserDetails(): HashMap<String, String> {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userDetails = HashMap<String, String>()
        userDetails["Username"] = sharedPreferences.getString("Username", "") ?: ""
        userDetails["Email"] = sharedPreferences.getString("Email", "") ?: ""
        userDetails["Password"] = sharedPreferences.getString("Password", "") ?: ""
        userDetails["PhoneNumber"] = sharedPreferences.getString("PhoneNumber", "") ?: ""
        userDetails["Address"] = sharedPreferences.getString("Address", "") ?: ""
        return userDetails
    }

}
