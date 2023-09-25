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
import androidx.lifecycle.Observer;
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

import java.util.Objects;

public class AddEditContactFragment extends Fragment {

    private FragmentAddEditContactBinding binding;

    private ContactViewModel mContactVM;

    private NavController navController;

    private final int ALERT_SAVE = 0;
    private final int ALERT_DISCARD = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_edit_contact, container, false);

        View view = binding.getRoot();

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        binding.setViewModel(mContactVM);
        binding.setLifecycleOwner(requireActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

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
            mContactVM.setCapturedImage(Util.decodeBase64ToBitmap(mContactVM.getCurrentlyViewing().getValue().getImage()));
        }
        binding.imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String firstName = binding.edittextFirstName.getText().toString();
                String lastName = binding.edittextLastName.getText().toString();
                String phone = binding.edittextPhone.getText().toString();
                String email = binding.edittextEmail.getText().toString();
                if (isInputsValid(firstName, lastName, phone, email)) {
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
                                mContactVM.getImageInBase64(),
                                currentlyViewing.isFavorite());
                        mContactVM.updateContact(contact);
                        mContactVM.setCurrentlyViewing(contact);
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

                    navController.popBackStack();
                } else {
                    showAlert(ALERT_SAVE);
                }
            }
        });

        mContactVM.getCapturedImage().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.imgContact.setImageBitmap(Util.getRoundedCroppedBitmap(bitmap));
            }
        });
    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap image = (Bitmap) data.getExtras().get("data");

                        mContactVM.setCapturedImage(image);

                    }
                }
            });

    @Override
    public void onDestroyView() {
        mContactVM.getCapturedImage().removeObservers(getViewLifecycleOwner());

        super.onDestroyView();
    }

    private boolean isInputsValid(String firstName, String lastName, String phone, String email) {

        if (!firstName.isEmpty() || !lastName.isEmpty()) {
            return !phone.isEmpty() || !email.isEmpty();
        }
        return false;

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

    private void onBack() {
        if (isFieldEmpty()) {
            navController.popBackStack();
        } else {
            Contact currentlyViewing = mContactVM.getCurrentlyViewing().getValue();
            if (currentlyViewing != null) {
                // Edit, compare
                if (!diffCheck(currentlyViewing)) {
                    // no changes
                    navController.popBackStack();
                    return;
                }
            }
            showAlert(ALERT_DISCARD);
        }
    }

    private boolean diffCheck(Contact contact) {
        if (!contact.getFirstName().equals(binding.edittextFirstName.getText().toString()))
            return true;
        if (!contact.getLastName().equals(binding.edittextLastName.getText().toString()))
            return true;
        if (!contact.getPhoneNumber().equals(binding.edittextPhone.getText().toString()))
            return true;
        if (!Objects.equals(contact.getEmail(), binding.edittextEmail.getText().toString()))
            return true;
        if (mContactVM.getImageInBase64() != null)
            return true;

        return false;

    }

    private boolean isFieldEmpty() {
        if (binding.edittextFirstName.getText().toString().trim().length() > 0)
            return false;
        if (binding.edittextLastName.getText().toString().trim().length() > 0)
            return false;
        if (binding.edittextPhone.getText().toString().trim().length() > 0)
            return false;
        if (binding.edittextEmail.getText().toString().trim().length() > 0)
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
                        (dialog, id) -> {
                            binding.btnSave.callOnClick();
                        });

                builder1.setNegativeButton(
                        getString(R.string.discard),
                        (dialog, id) -> {
                            // go back
                            navController.popBackStack();
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
        }


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}