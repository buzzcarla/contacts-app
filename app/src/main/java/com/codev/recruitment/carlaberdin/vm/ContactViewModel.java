package com.codev.recruitment.carlaberdin.vm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.lib.crypto.EncryptionSettings;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.SingleLiveEvent;
import com.codev.recruitment.carlaberdin.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Contact View Model for business logic and observables
 */
public class ContactViewModel extends AndroidViewModel {
    private static final String TAG = "ContactViewModel";
    private ContactsLib mContactsLib;

    private MutableLiveData<Contact> mCurrentlyViewing;
    private SingleLiveEvent<Bitmap> mImageCaptured;

    private String mImageInBase64; // String base64 of the captured Bitmap, to save in database
    private final ExecutorService mExecutorService;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        mExecutorService = Executors.newSingleThreadExecutor();

        mCurrentlyViewing = new MutableLiveData<>();
        mImageCaptured = new SingleLiveEvent<>();

        // Instance of Contacts Library
        initContactsLib(application.getApplicationContext());
    }

    public void initContactsLib(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(Util.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        mContactsLib = new ContactsLib(
                context,
                sharedPref.getBoolean(Util.KEY_ENCRYPTION_ON, false) ?
                        new EncryptionSettings(                           // For encryption, Keys and Init Vectors are hardcoded for demo purposes
                                true,                                     // To show that the library is initialized with the encryption settings
                                "ABCDE-123456-ABCDEF-123456-ABCDE",       // if encryption ON - contact data in each column is encrypted on insert/update and decrypted on fetchAll
                                "ssa234fdssa234fd") :                     // In real-world apps, keys and IVs are hidden depending on developer's implementation
                        new EncryptionSettings(       // OFF
                                false,
                                "",
                                "")
        );
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

    /**
     * Function used to decrypt the data from the database (if encryption ON)
     * Not implemented in the Contacts Lib because it is a LiveData, decryption is easier in the app side
     */
    public List<Contact> intercept(List<Contact> allContacts) {
        return mContactsLib.intercept(allContacts);
    }

    // Reset variables used in keeping track of/displaying the selected Contact
    public void resetCurrentlyViewing() {
        mCurrentlyViewing = new MutableLiveData<>();
        mImageCaptured = new SingleLiveEvent<>();
        mImageInBase64 = null;
    }

    public void setCurrentlyViewing(Contact currentlyViewing) {
        this.mCurrentlyViewing.postValue(currentlyViewing);
    }

    // Creates a "clone" Contact object
    public Contact clone(Contact contact) {
        return new Contact(contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getImage(),
                contact.isFavorite());
    }

    public SingleLiveEvent<Bitmap> getCapturedImage() {
        return mImageCaptured;
    }

    public String getImageInBase64() {
        return mImageInBase64;
    }

    public void convertImageToBase64(Bitmap bitmap) throws IOException {
        byte[] imgArr = Util.getBytes(bitmap, 50); // set low quality for fast transaction
        mImageInBase64 = Util.encodeImageToBase64(imgArr);
    }

    public void setImage(Bitmap rounded) {
        mImageCaptured.postValue(rounded);
    }

    public void setCapturedImageFromCamera(Bitmap fullSize) {
        mExecutorService.execute(() -> {
            Bitmap rounded = Util.getRoundedCroppedBitmap(fullSize);
            try {
                convertImageToBase64(rounded);
            } catch (IOException e) {
                Log.d(TAG, "conversion to Base64 failed");
            }
            mImageCaptured.postValue(rounded);
        });
    }

}
