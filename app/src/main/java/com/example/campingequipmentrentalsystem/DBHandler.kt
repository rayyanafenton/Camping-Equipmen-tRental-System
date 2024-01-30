package com.example.campingequipmentrentalsystem

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.campingequipmentrentalsystem.Data.EquipmentData
import com.example.campingequipmentrentalsystem.Data.UserData


class DBHandler
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createTableUser(db)
        createTableEquipment(db)
        createTableBooking(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_EQUIPMENT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_BOOKING")
        onCreate(db)
    }

    private fun createTableUser(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME_USER (" +
                "$KEY_ID_USER INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_USERNAME TEXT," +
                "$KEY_EMAIL TEXT," +
                "$KEY_PHONE TEXT," +
                "$KEY_ADDRESS TEXT," +
                "$KEY_PASSWORD TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    private fun createTableEquipment(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME_EQUIPMENT (" +
                "$KEY_ID_EQUIPMENT INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_EQUIPMENT_NAME TEXT," +
                "$KEY_EQUIPMENT_PRICE TEXT," +
                "$KEY_EQUIPMENT_STATUS TEXT," +
                "$KEY_EQUIPMENT_DESCRIPTION TEXT," +
                "$KEY_EQUIPMENT_IMAGE TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    private fun createTableBooking(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME_BOOKING (" +
                "$KEY_ID_BOOKING INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_ID_USER INTEGER," +
                "$KEY_ID_EQUIPMENT INTEGER," +
                "$KEY_BOOKING_STATUS TEXT," +
                "$KEY_BOOKING_PICK_UP TEXT," +
                "$KEY_BOOKING_RETURN TEXT," +
                "$KEY_BOOKING_TOTAL TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    fun insertUserDetails(username: String?, email: String?, phone_number: String?, address: String?, password: String?) {
        val db = writableDatabase
        val cValues = ContentValues()
        cValues.put(KEY_USERNAME, username)
        cValues.put(KEY_EMAIL, email)
        cValues.put(KEY_PHONE, phone_number)
        cValues.put(KEY_ADDRESS, address)
        cValues.put(KEY_PASSWORD, password)
        try {
            val result = db.insert(TABLE_NAME_USER, null, cValues)
        } catch (e: Exception) {
        } finally {
            db.close()
        }
    }

    fun getUserDetailsByEmail(email: String): Cursor {
        val db = writableDatabase
        return db.rawQuery(
            "SELECT * FROM $TABLE_NAME_USER WHERE $KEY_EMAIL = ?",
            arrayOf(email)
        )
    }

    fun updateUserDetails(
        id: String,
        username: String?,
        email: String?,
        phone_number: String?,
        address: String?,
        password: String?
    ) {
        val db = writableDatabase
        val cValues = ContentValues()
        cValues.put(KEY_USERNAME, username)
        cValues.put(KEY_EMAIL, email)
        cValues.put(KEY_PHONE, phone_number)
        cValues.put(KEY_ADDRESS, address)
        cValues.put(KEY_PASSWORD, password)

        val rowsAffected = db.update(TABLE_NAME_USER, cValues, "$KEY_ID_USER = ?", arrayOf(id))
        Log.d("DBHandler", "Updated $rowsAffected row(s)")

        db.close()
    }

    fun getAllUsers(): List<UserData> {
        val userList = ArrayList<UserData>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_USER", null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID_USER)
                val usernameIndex = cursor.getColumnIndex(KEY_USERNAME)
                val emailIndex = cursor.getColumnIndex(KEY_EMAIL)
                val phoneIndex = cursor.getColumnIndex(KEY_PHONE)
                val addressIndex = cursor.getColumnIndex(KEY_ADDRESS)
                val passwordIndex = cursor.getColumnIndex(KEY_PASSWORD)

                if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && phoneIndex != -1 && addressIndex != -1 && passwordIndex != -1) {
                    val user = UserData(
                        id = cursor.getInt(idIndex),
                        username = cursor.getString(usernameIndex),
                        email = cursor.getString(emailIndex),
                        phoneNumber = cursor.getString(phoneIndex),
                        address = cursor.getString(addressIndex),
                        password = cursor.getString(passwordIndex)
                    )
                    userList.add(user)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun updateEquipmentStatus(equipmentId: String, newStatus: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_EQUIPMENT_STATUS, newStatus)

        db.update(TABLE_NAME_EQUIPMENT, contentValues, "$KEY_ID_EQUIPMENT = ?", arrayOf(equipmentId))
        db.close()
    }

    fun insertEquipmentDetails(equipment: EquipmentData) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_EQUIPMENT_NAME, equipment.name)
        contentValues.put(KEY_EQUIPMENT_DESCRIPTION, equipment.description)
        contentValues.put(KEY_EQUIPMENT_STATUS, equipment.status)
        contentValues.put(KEY_EQUIPMENT_PRICE, equipment.price)
        contentValues.put(KEY_EQUIPMENT_IMAGE, equipment.imageUrl)

        db.insert(TABLE_NAME_EQUIPMENT, null, contentValues)
        db.close()
    }

    fun getEquipmentDetails(): ArrayList<EquipmentData> {
        val equipmentList = ArrayList<EquipmentData>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_EQUIPMENT", null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID_EQUIPMENT)
                val nameIndex = cursor.getColumnIndex(KEY_EQUIPMENT_NAME)
                val descriptionIndex = cursor.getColumnIndex(KEY_EQUIPMENT_DESCRIPTION)
                val statusIndex = cursor.getColumnIndex(KEY_EQUIPMENT_STATUS)
                val priceIndex = cursor.getColumnIndex(KEY_EQUIPMENT_PRICE)
                val imageIndex = cursor.getColumnIndex(KEY_EQUIPMENT_IMAGE)

                if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 || statusIndex == -1 || priceIndex == -1 || imageIndex == -1) {
                    Log.e("DBHandler", "One or more column names are invalid.")
                    break
                }

                val equipment = EquipmentData(
                    cursor.getString(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(descriptionIndex),
                    cursor.getString(statusIndex),
                    cursor.getString(priceIndex),
                    cursor.getInt(imageIndex)
                )
                equipmentList.add(equipment)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return equipmentList
    }

    fun insertBookingDetails(userId: Int, equipmentId: Int, bookingStatus: String, pickUpDate: String, returnDate: String, totalPrice: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID_USER, userId)
        contentValues.put(KEY_ID_EQUIPMENT, equipmentId)
        contentValues.put(KEY_BOOKING_STATUS, bookingStatus)
        contentValues.put(KEY_BOOKING_PICK_UP, pickUpDate)
        contentValues.put(KEY_BOOKING_RETURN, returnDate)
        contentValues.put(KEY_BOOKING_TOTAL, totalPrice)

        db.insert(TABLE_NAME_BOOKING, null, contentValues)
        db.close()
    }

    fun updateBookingStatus(bookingId: String, newStatus: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_BOOKING_STATUS, newStatus)

        db.update(TABLE_NAME_BOOKING, contentValues, "$KEY_ID_BOOKING = ?", arrayOf(bookingId))
        db.close()
    }

    fun getEquipmentById(equipmentId: String): EquipmentData? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME_EQUIPMENT,
            null,
            "$KEY_ID_EQUIPMENT = ?",
            arrayOf(equipmentId),
            null, null, null
        )

        var equipment: EquipmentData? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_EQUIPMENT))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EQUIPMENT_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EQUIPMENT_DESCRIPTION))
            val status = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EQUIPMENT_STATUS))
            val price = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EQUIPMENT_PRICE))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EQUIPMENT_IMAGE))

            equipment = EquipmentData(id, name, description, status, price, image)
        }
        cursor.close()
        db.close()
        return equipment
    }

    fun deleteBooking(bookingId: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME_BOOKING, "$KEY_ID_BOOKING = ?", arrayOf(bookingId))
        db.close()
    }

    fun getBookingDetails(userId: Int): ArrayList<BookingData> {
        val db = readableDatabase
        val bookingList = ArrayList<BookingData>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_BOOKING WHERE $KEY_ID_USER = ?", arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            val bookingId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_BOOKING))
            val equipmentId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_EQUIPMENT))
            val bookingStatus = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_STATUS))
            val pickUpDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_PICK_UP))
            val returnDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_RETURN))
            val totalPrice = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_TOTAL))

            val equipment = getEquipmentById(equipmentId)
            equipment?.let {
                val bookingData = BookingData(
                    id = bookingId,
                    equipmentId = it.id,
                    userId = userId.toString(),
                    name = it.name,
                    price = it.price,
                    status = it.status,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    bookingStatus = bookingStatus,
                    pickupDate = pickUpDate,
                    returnDate = returnDate,
                    totalPrice = totalPrice,
                )
                bookingList.add(bookingData)
            }
        }
        cursor.close()
        db.close()
        return bookingList
    }

    fun getAllBookingDetails(): ArrayList<BookingData> {
        val db = readableDatabase
        val bookingList = ArrayList<BookingData>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_BOOKING ", null)

        while (cursor.moveToNext()) {
            val bookingId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_BOOKING))
            val equipmentId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_EQUIPMENT))
            val bookingStatus = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_STATUS))
            val pickUpDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_PICK_UP))
            val returnDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_RETURN))
            val totalPrice = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BOOKING_TOTAL))

            val equipment = getEquipmentById(equipmentId)
            equipment?.let {
                val bookingData = BookingData(
                    id = bookingId,
                    equipmentId = it.id,
                    "",
                    name = it.name,
                    price = it.price,
                    status = it.status,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    bookingStatus = bookingStatus,
                    pickupDate = pickUpDate,
                    returnDate = returnDate,
                    totalPrice = totalPrice,
                )
                bookingList.add(bookingData)
            }
        }
        cursor.close()
        db.close()
        return bookingList
    }


    companion object {
         const val DB_VERSION = 1
         const val DB_NAME = "CampingEquipment"
         const val TABLE_NAME_USER = "user_details"
         const val TABLE_NAME_EQUIPMENT = "equipment_details"
         const val TABLE_NAME_BOOKING = "booking_details"

         const val KEY_ID_USER = "user_id"
         const val KEY_USERNAME = "username"
         const val KEY_EMAIL = "email"
         const val KEY_PHONE = "phone_number"
         const val KEY_ADDRESS = "address"
         const val KEY_PASSWORD = "password"

         const val KEY_ID_EQUIPMENT = "equipment_id"
         const val KEY_EQUIPMENT_NAME = "equipment_name"
         const val KEY_EQUIPMENT_PRICE = "equipment_price"
         const val KEY_EQUIPMENT_STATUS = "equipment_status"
         const val KEY_EQUIPMENT_DESCRIPTION = "equipment_description"
         const val KEY_EQUIPMENT_IMAGE = "image_URL"

         const val KEY_ID_BOOKING = "booking_id"
         const val KEY_BOOKING_STATUS = "equipment_status"
         const val KEY_BOOKING_PICK_UP = "pick_up_date"
         const val KEY_BOOKING_RETURN = "return_date"
         const val KEY_BOOKING_TOTAL = "total_price"
    }
}
