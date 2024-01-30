package com.example.campingequipmentrentalsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.campingequipmentrentalsystem.Adapter.UserAdapter

class UserListActivity : AppCompatActivity() {

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.title = "User List"

        dbHandler = DBHandler(this)
        val usersListView: ListView = findViewById(R.id.userListView)

        val users = dbHandler.getAllUsers()

        val adapter = UserAdapter(this, users)
        usersListView.adapter = adapter
    }
}

