package com.codev.recruitment.carlaberdin.lib

import com.codev.recruitment.carlaberdin.repository.ContactDatabase
import com.codev.recruitment.carlaberdin.repository.ContactRepository
import com.codev.recruitment.carlaberdin.repository.data.ContactDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class])
interface RoomComponent {
    fun contactDao(): ContactDao
    fun contactDatabase(): ContactDatabase
    fun contactRepository(): ContactRepository
    fun inject(contactsLibrary: ContactsLib)
}