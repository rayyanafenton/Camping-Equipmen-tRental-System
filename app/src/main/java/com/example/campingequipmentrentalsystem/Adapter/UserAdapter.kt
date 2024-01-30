package com.example.campingequipmentrentalsystem.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.campingequipmentrentalsystem.R
import com.example.campingequipmentrentalsystem.Data.UserData

class UserAdapter(context: Context, users: List<UserData>) :
    ArrayAdapter<UserData>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false)
        }

        val currentUser = getItem(position)

        val usernameTextView = listItemView!!.findViewById<TextView>(R.id.tvUsername)
        usernameTextView.text = currentUser?.username

        return listItemView
    }
}
