package com.codev.recruitment.carlaberdin.lib

import android.content.Context
import androidx.room.Room
import com.codev.recruitment.carlaberdin.repository.ContactDatabase
import com.codev.recruitment.carlaberdin.repository.ContactRepository
import com.codev.recruitment.carlaberdin.repository.data.ContactDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule (context: Context) {
    private val contactDatabase : ContactDatabase

    init {
        contactDatabase = Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            "contacts_table"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesContactDatabase(): ContactDatabase {
        return contactDatabase
    }

    @Singleton
    @Provides
    fun providesContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.contactDao()
    }

    @Singleton
    @Provides
    fun productRepository(contactDao: ContactDao): ContactRepository {
        return ContactRepository(contactDao)
    }

}