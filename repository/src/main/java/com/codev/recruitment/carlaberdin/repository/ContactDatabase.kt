package com.codev.recruitment.carlaberdin.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codev.recruitment.carlaberdin.repository.data.Contact
import com.codev.recruitment.carlaberdin.repository.data.ContactDao

@Database(entities = [Contact::class], version = 2)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao() : ContactDao
}