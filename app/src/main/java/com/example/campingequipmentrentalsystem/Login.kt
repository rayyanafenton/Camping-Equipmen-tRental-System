package com.example.campingequipmentrentalsystem

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class Login : AppCompatActivity() {

    lateinit var dbHandler: DBHandler // Assuming DBHandler is your SQLiteOpenHelper class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        dbHandler = DBHandler(this)

        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val signUpLink: TextView = findViewById(R.id.signup_link)
        val errorMessage: TextView = findViewById(R.id.error_message)
        val btnLogin: AppCompatButton = findViewById(R.id.login_btn)

        btnLogin.setOnClickListener {
            val userEmail = email.text.toString()
            val userPassword = password.text.toString()

            if(userEmail == "admin" && userPassword == "admin"){
                val intent = Intent(this@Login, AdminPage::class.java)
                startActivity(intent)
            }
            else if (validateAndStoreUser(userEmail, userPassword)) {
                val intent = Intent(this@Login, Home::class.java)
                startActivity(intent)
            } else {
                errorMessage.text = "Invalid email or password."
            }
        }

        signUpLink.setOnClickListener {

            val intent = Intent(this@Login, SignUp::class.java)
            startActivity(intent)
        }

    }

    private fun validateAndStoreUser(email: String, password: String): Boolean {
        val cursor = dbHandler.getUserDetailsByEmail(email)
        if (cursor.moveToFirst() && cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_PASSWORD)) == password) {
            storeUserDetails(cursor)
            cursor.close()
            return true
        }
        cursor.close()
        return false
    }

    private fun storeUserDetails(cursor: Cursor) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("id", cursor.getInt(cursor.getColumnIndexOrThrow(DBHandler.KEY_ID_USER)))
        editor.putString("Username", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_USERNAME)))
        editor.putString("Email", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_EMAIL)))
        editor.putString("Password", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_PASSWORD)))
        editor.putString("PhoneNumber", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_PHONE)))
        editor.putString("Address", cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.KEY_ADDRESS)))

        editor.apply()
    }

}
