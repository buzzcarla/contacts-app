package com.codev.recruitment.carlaberdin.lib

import android.content.Context
import androidx.lifecycle.LiveData
import com.codev.recruitment.carlaberdin.repository.ContactRepository
import com.codev.recruitment.carlaberdin.repository.data.Contact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class ContactsLib(context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var repository: ContactRepository

    init {

        DaggerRoomComponent.builder()
            .roomModule(RoomModule(context))
            .build()
            .inject(this)

    }

    fun insertContact(contact : Contact) {
        coroutineScope.launch {
            repository.insertContact(contact)
        }
    }

    fun updateContact(contact : Contact) {
        coroutineScope.launch {
            repository.updateContact(contact)
        }
    }

    fun deleteContact(contact : Contact) {
        coroutineScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun getAllContacts() : LiveData<List<Contact>> {
        return repository.getContacts()
    }
}