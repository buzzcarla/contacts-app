package com.codev.recruitment.carlaberdin.lib

import com.codev.recruitment.carlaberdin.lib.crypto.Crypto
import com.codev.recruitment.carlaberdin.lib.crypto.CryptoModule
import com.codev.recruitment.carlaberdin.repository.ContactDatabase
import com.codev.recruitment.carlaberdin.repository.ContactRepository
import com.codev.recruitment.carlaberdin.repository.data.ContactDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class, CryptoModule::class])
interface LibComponent {
    fun contactDao(): ContactDao
    fun contactDatabase(): ContactDatabase
    fun contactRepository(): ContactRepository
    fun cryptography() : Crypto
    fun inject(contactsLibrary: ContactsLib)

}