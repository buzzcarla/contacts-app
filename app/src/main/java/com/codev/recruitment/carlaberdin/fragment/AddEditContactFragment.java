package com.codev.recruitment.carlaberdin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.FragmentAddEditContactBinding;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.Util;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;


/**
 * Fragment for adding/editing Contact.
 */
public class AddEditContactFragment extends Fragment {

    private FragmentAddEditContactBinding mBinding;
    private ContactViewModel mContactVM;
    private NavController mNavController;

    // code for the Alert dialog
    private final int ALERT_SAVE = 0;
    private final int ALERT_DISCARD = 1;
    private final int ALERT_INVALID_EMAIL = 2;

    private final int VALIDATION_OK = 1;
    private final int VALIDATION_FAIL_EMAIL = 2;
    private final int VALIDATION_FAIL_PHONE = 3;
    private final int VALIDATION_FAIL_EMPTY_FIELDS = 4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_edit_contact, container, false);

        View view = mBinding.getRoot();

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        mBinding.setViewModel(mContactVM);
        mBinding.setLifecycleOwner(requireActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        // Handle phone back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /*enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if (mContactVM.getCurrentlyViewing().getValue() != null && mContactVM.getCurrentlyViewing().getValue().getImage() != null) {
            // Edit contact, show image if not null
            // image not included in data binding. images are saved as Base64 String in the database and needs to be decoded to Bitmap before displaying
            mContactVM.setImage(Util.decodeBase64ToBitmap(mContactVM.getCurrentlyViewing().getValue().getImage()));
        }
        mBinding.imgContact.setOnClickListener(view1 -> {
            // open camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityResultLauncher.launch(cameraIntent);
        });

        mBinding.btnCancel.setOnClickListener(view12 -> mNavController.popBackStack());

        mBinding.btnBack.setOnClickListener(view13 -> onBack());

        mBinding.btnSave.setOnClickListener(view14 -> {

            String firstName = mBinding.edittextFirstName.getText().toString();
            String lastName = mBinding.edittextLastName.getText().toString();
            String phone = mBinding.edittextPhone.getText().toString();
            String email = mBinding.edittextEmail.getText().toString();

            // check if inputs are valid
            int validation = isInputsValid(firstName, lastName, phone, email);
            if (VALIDATION_OK == validation) {
                Contact contact;
                Contact currentlyViewing = mContactVM.getCurrentlyViewing().getValue();
                if (currentlyViewing != null) {
                    // Edit
                    contact = generateContactObject(
                            currentlyViewing.getId(),
                            firstName,
                            lastName,
                            phone,
                            email,
                            mContactVM.getImageInBase64() != null ? mContactVM.getImageInBase64() : currentlyViewing.getImage(), // only replace if there's a new image captured
                            currentlyViewing.isFavorite());
                    // set currently viewing as clone of the new Contact object,  currently viewing is used in data binding
                    // if not cloned, in case of encryption ON, encrypted data will be momentarily displayed in ContactSummary screen upon update
                    mContactVM.setCurrentlyViewing(mContactVM.clone(contact));

                    mContactVM.updateContact(contact);
                } else {
                    // New Contact
                    contact = generateContactObject(
                            0,
                            firstName,
                            lastName,
                            phone,
                            email,
                            mContactVM.getImageInBase64(),
                            false);

                    mContactVM.addContact(contact);
                }

                mNavController.popBackStack();
            } else {
                // invalid input
                switch (validation) {
                    case VALIDATION_FAIL_EMPTY_FIELDS:
                        showAlert(ALERT_SAVE);
                        break;
                    case VALIDATION_FAIL_EMAIL:
                        showAlert(ALERT_INVALID_EMAIL);
                        break;

                }
            }
        });

        // if there's a newly captured image, display
        mContactVM.getCapturedImage().observe(getViewLifecycleOwner(), bitmap -> mBinding.imgContact.setImageBitmap(Util.getRoundedCroppedBitmap(bitmap)));
    }

    // Handle result of Camera Intent
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            // Get the intent extra (image) as Bitmap
                            Bitmap image = (Bitmap) data.getExtras().get("data");
                            // Set value in VM to notify observers
                            mContactVM.setCapturedImageFromCamera(image);
                        }

                    }
                }
            });

    @Override
    public void onDestroy() {
        // remove observer
        mContactVM.getCapturedImage().removeObservers(this);

        super.onDestroy();
    }

    // Check if there is any valid information to save and if email is valid
    private int isInputsValid(String firstName, String lastName, String phone, String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!firstName.isEmpty() || !lastName.isEmpty()) {
            if (!email.isEmpty()) {
                if (!email.matches(emailPattern)) {
                    // invalid email
                    return VALIDATION_FAIL_EMAIL;
                } else {
                    return VALIDATION_OK;
                }
            }
            if (!phone.isEmpty()) {
                return VALIDATION_OK;
            }
        }
        return VALIDATION_FAIL_EMPTY_FIELDS;
    }

    private Contact generateContactObject(int id, String firstName, String lastName, String phone, String email, String imgBase64, boolean isFavorite) {
        return new Contact(
                id,
                firstName,
                lastName,
                phone,
                email,
                imgBase64,
                isFavorite);
    }


    // Handle back press to check if should popBackStack or show unsaved changes alert first
    private void onBack() {
        if (isFieldEmpty()) { // no inputted data
            mNavController.popBackStack();
        } else {
            Contact currentlyViewing = mContactVM.getCurrentlyViewing().getValue();
            if (currentlyViewing != null) {
                // Edit, Compare
                if (!diffCheck(currentlyViewing)) {
                    // has no changes, no need to show alert
                    mNavController.popBackStack();
                    return;
                }
            }
            showAlert(ALERT_DISCARD);
        }
    }


    // Checks if data has been changed in Edit Contact page
    private boolean diffCheck(Contact contact) {
        if (!contact.getFirstName().equals(mBinding.edittextFirstName.getText().toString()))
            return true;
        if (!contact.getLastName().equals(mBinding.edittextLastName.getText().toString()))
            return true;
        if (!contact.getPhoneNumber().equals(mBinding.edittextPhone.getText().toString()))
            return true;
        if (contact.getEmail() != null && !contact.getEmail().equals(mBinding.edittextPhone.getText().toString()))
            return true;
        return mContactVM.getImageInBase64() != null;

    }

    // Checks if there is inputted data
    private boolean isFieldEmpty() {
        if (mBinding.edittextFirstName.getText().toString().trim().length() > 0)
            return false;
        if (mBinding.edittextLastName.getText().toString().trim().length() > 0)
            return false;
        if (mBinding.edittextPhone.getText().toString().trim().length() > 0)
            return false;
        if (mBinding.edittextEmail.getText().toString().trim().length() > 0)
            return false;
        return mContactVM.getImageInBase64() == null;
    }

    private void showAlert(int code) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        builder1.setCancelable(true);

        switch (code) {
            case ALERT_DISCARD:
                builder1.setMessage(R.string.discard_alert);
                builder1.setNeutralButton(
                        getString(android.R.string.cancel),
                        (dialog, id) -> {
                            // do nothing
                        });

                builder1.setPositiveButton(
                        getString(R.string.save),
                        (dialog, id) -> mBinding.btnSave.callOnClick());

                builder1.setNegativeButton(
                        getString(R.string.discard),
                        (dialog, id) -> {
                            // go back
                            mNavController.popBackStack();
                        });
                break;
            case ALERT_SAVE:
                builder1.setMessage(R.string.save_alert);
                builder1.setPositiveButton(
                        getString(android.R.string.ok),
                        (dialog, id) -> {
                            // do nothing
                        });


                break;
            case ALERT_INVALID_EMAIL:
                builder1.setMessage(R.string.email_alert);
                builder1.setPositiveButton(
                        getString(android.R.string.ok),
                        (dialog, id) -> {
                            // do nothing
                        });


                break;
        }


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}