package com.example.campingequipmentrentalsystem

data class BookingData(
    val id: String,
    val equipmentId: String,
    val userId: String,
    val name: String,
    val price: String,
    val status: String,
    val description: String,
    val imageUrl: Int,
    val bookingStatus: String,
    val pickupDate: String,
    val returnDate: String,
    val totalPrice: String,
)


