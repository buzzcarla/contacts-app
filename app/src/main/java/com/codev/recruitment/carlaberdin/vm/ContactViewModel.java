package com.codev.recruitment.carlaberdin.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.SingleLiveEvent;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactsLib mContactsLib;

    private Contact mCurrentlyViewing;


    public ContactViewModel(@NonNull Application application) {
        super(application);

        mContactsLib = new ContactsLib(application);

    }

    public LiveData<List<Contact>> getAllContacts() {
        return mContactsLib.getAllContacts();
    }

    public Contact getCurrentlyViewing() {
        return mCurrentlyViewing;
    }

    public void setCurrentlyViewing(Contact currentlyViewing) {
        this.mCurrentlyViewing = currentlyViewing;
    }
}
