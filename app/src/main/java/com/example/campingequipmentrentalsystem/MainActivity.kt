package com.example.campingequipmentrentalsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val btnLogin: AppCompatButton = findViewById(R.id.login_btn)
        val btnSignUp: AppCompatButton = findViewById(R.id.signup_btn)

        btnLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intent)
        }

    }
}
