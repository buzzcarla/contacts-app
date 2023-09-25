package com.codev.recruitment.carlaberdin.vm;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.SingleLiveEvent;
import com.codev.recruitment.carlaberdin.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactViewModel extends AndroidViewModel {
    private static final String TAG = "ContactViewModel";
    private ContactsLib mContactsLib;

    private MutableLiveData<Contact> mCurrentlyViewing;

    private SingleLiveEvent<Bitmap> imageCaptured;
    private String imageInBase64;

    private ExecutorService mExecutorService;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        mExecutorService = Executors.newSingleThreadExecutor();

        mContactsLib = new ContactsLib(application);

        mCurrentlyViewing = new MutableLiveData<>();

        imageCaptured = new SingleLiveEvent<>();
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

    public void resetCurrentlyViewing() {
        mCurrentlyViewing = new MutableLiveData<>();
        imageCaptured = new SingleLiveEvent<>();
        imageInBase64 = null;
    }

    public void setCurrentlyViewing(Contact currentlyViewing) {
        this.mCurrentlyViewing.postValue(currentlyViewing);
    }

    public SingleLiveEvent<Bitmap> getCapturedImage() {
        return imageCaptured;
    }

    public String getImageInBase64() {
        return imageInBase64;
    }

    public void convertImageToBase64(Bitmap bitmap) throws IOException {
        byte[] imgArr = Util.getBytes(bitmap, 50);
        imageInBase64 = Util.encodeImageToBase64(imgArr);
    }

    public void setImage(Bitmap rounded) {
        imageCaptured.postValue(rounded);
    }

    public void setCapturedImageFromCamera(Bitmap fullSize) {
        mExecutorService.execute(() -> {
            Bitmap rounded = Util.getRoundedCroppedBitmap(fullSize);
            try {
                convertImageToBase64(rounded);
            } catch (IOException e) {
                Log.d(TAG, "conversion to Base64 failed");
            }
            imageCaptured.postValue(rounded);
        });
    }

}
