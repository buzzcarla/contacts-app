package com.codev.recruitment.carlaberdin.repository.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String?,
    val image: String?,
    val isFavorite: Boolean) {

    constructor(firstName: String, lastName: String, phoneNumber: String) : this(0, firstName, lastName, phoneNumber, null, null, false) {

    }

    constructor(firstName: String, lastName: String, phoneNumber: String, email: String, image: String) : this(0, firstName, lastName, phoneNumber, email, image, false) {

    }
}
