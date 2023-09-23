package com.codev.recruitment.carlaberdin.repository

import androidx.lifecycle.LiveData
import com.codev.recruitment.carlaberdin.repository.data.Contact
import com.codev.recruitment.carlaberdin.repository.data.ContactDao

class ContactRepository(private val contactDao: ContactDao) {
    suspend fun insertContact(contact: Contact) {
        contactDao.insert(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.update(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.delete(contact)
    }

    fun getContacts(): LiveData<List<Contact>> {
        return contactDao.getContacts()
    }
}