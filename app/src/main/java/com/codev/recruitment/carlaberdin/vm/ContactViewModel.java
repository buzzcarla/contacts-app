package com.codev.recruitment.carlaberdin.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.SingleLiveEvent;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactsLib mContactsLib;

    private MutableLiveData<Contact> mCurrentlyViewing;


    public ContactViewModel(@NonNull Application application) {
        super(application);

        mContactsLib = new ContactsLib(application);

        mCurrentlyViewing = new MutableLiveData<>();

    }

    public void addContact(Contact contact) {
        mContactsLib.insertContact(contact);
    }
    public void deleteContact(Contact contact) {
        mContactsLib.deleteContact(contact);
    }

    public void updateContact(Contact contact) {
        mContactsLib.updateContact(contact);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return mContactsLib.getAllContacts();
    }

    public MutableLiveData<Contact> getCurrentlyViewing() {
        return mCurrentlyViewing;
    }

    public void setCurrentlyViewing(Contact currentlyViewing) {
        this.mCurrentlyViewing.postValue(currentlyViewing);
    }

}
