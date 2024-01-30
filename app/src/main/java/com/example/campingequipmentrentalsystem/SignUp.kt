package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignUp : AppCompatActivity() {

    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        dbHandler = DBHandler(this)

        val username: EditText = findViewById(R.id.username)
        val email: EditText = findViewById(R.id.email)
        val phone_number: EditText = findViewById(R.id.phone_number)
        val address: EditText = findViewById(R.id.address)
        val password: EditText = findViewById(R.id.password)

        val loginLink: TextView = findViewById(R.id.login_link)
        val errorMessage: TextView = findViewById(R.id.error_message)
        val btnSignUp: AppCompatButton = findViewById(R.id.signup_btn)

        btnSignUp.setOnClickListener {
            val userEmail = email.text.toString()
            val userExists = checkIfUserExists(userEmail)

            if (userExists) {
                errorMessage.text = "Email already registered. Please login."
            } else {
                dbHandler.insertUserDetails(
                    username.text.toString(),
                    userEmail,
                    phone_number.text.toString(),
                    address.text.toString(),
                    password.text.toString()
                )
                errorMessage.text = "Registration successful."
                storeUserDetails(userEmail)
                val intent = Intent(this@SignUp, Home::class.java)
                startActivity(intent)
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this@SignUp, Login::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfUserExists(email: String): Boolean {
        val cursor = dbHandler.getUserDetailsByEmail(email)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    private fun storeUserDetails(email: String) {
        val cursor = dbHandler.getUserDetailsByEmail(email)
        if (cursor.moveToFirst()) {
            val sharedPreferences = getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("id", cursor.getInt(cursor.getColumnIndexOrThrow(DBHandler.KEY_ID_USER)))
            editor.putString("Username", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_USERNAME)))
            editor.putString("Email", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_EMAIL)))
            editor.putString("Password", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_PASSWORD)))
            editor.putString("PhoneNumber", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_PHONE)))
            editor.putString("Address", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_ADDRESS)))

            editor.apply()
        }
        cursor.close() // Close the cursor
    }
}
