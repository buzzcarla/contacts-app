package com.codev.recruitment.carlaberdin.lib

import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import com.codev.recruitment.carlaberdin.lib.crypto.Crypto
import com.codev.recruitment.carlaberdin.lib.crypto.CryptoModule
import com.codev.recruitment.carlaberdin.lib.crypto.EncryptionSettings
import com.codev.recruitment.carlaberdin.repository.ContactRepository
import com.codev.recruitment.carlaberdin.repository.data.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Library for all communication with the database, including the encryption before inserting/updating into the database
 * and decryption of data
 */
class ContactsLib(context: Context, encryptionSettings : EncryptionSettings) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var encryptionSettings : EncryptionSettings

    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var cryptography: Crypto

    init {
        this.encryptionSettings = encryptionSettings

        DaggerLibComponent.builder()
            .roomModule(RoomModule(context))
            .cryptoModule(CryptoModule(encryptionSettings))
            .build()
            .inject(this)
    }

    fun insertContact(contact : Contact) {
        coroutineScope.launch {
            repository.insertContact(intercept(contact))
        }
    }

    fun updateContact(contact : Contact) {
        coroutineScope.launch {
            repository.updateContact(intercept(contact))
        }
    }

    fun deleteContact(contact : Contact) {
        coroutineScope.launch {
            repository.deleteContact(intercept(contact))
        }
    }

    fun getAllContacts() : LiveData<List<Contact>> {
        return repository.getContacts()
    }


    /**
     * For decrypting the list of Contacts from the database
     */
    fun intercept(allContacts: List<Contact>): List<Contact> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && encryptionSettings.isEncryptionOn) {
            val decryptedList = mutableListOf<Contact>()

            for (item in allContacts) {
                decryptedList.add(decrypt(item))
            }

            // Sort the list alphabetically, if encryptON data once decrypted is not in alphabetical order
            decryptedList.sortWith(Comparator.comparing(Contact::firstName))
            return decryptedList
        }
        return allContacts // off, return as is
    }

    /**
     * For encrypting fields in Contacts object
     */
    private fun intercept(contact: Contact): Contact {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && encryptionSettings.isEncryptionOn) {
            contact.firstName = cryptography.encryptAES265(contact.firstName.toByteArray(Charsets.UTF_8))
            contact.lastName = cryptography.encryptAES265(contact.lastName.toByteArray(Charsets.UTF_8))
            contact.phoneNumber = cryptography.encryptAES265(contact.phoneNumber.toByteArray(Charsets.UTF_8))
            contact.email = cryptography.encryptAES265(contact.email?.toByteArray(Charsets.UTF_8))

            if (contact.image != null && !contact.image.equals("")) {
                contact.image = cryptography.encryptAES265(contact.image?.toByteArray(Charsets.UTF_8))
            }

            return contact
        }
        return contact // off, return as is
    }

    private fun decrypt(contact: Contact) : Contact {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && encryptionSettings.isEncryptionOn) {

            contact.firstName = cryptography.decryptAES265(contact.firstName)
            contact.lastName = cryptography.decryptAES265(contact.lastName)
            contact.phoneNumber = cryptography.decryptAES265(contact.phoneNumber)
            contact.email = cryptography.decryptAES265(contact.email)

            if (contact.image != null && !contact.image.equals("")) {
                contact.image = cryptography.decryptAES265(contact.image)
            }
        }
        return contact
    }
}