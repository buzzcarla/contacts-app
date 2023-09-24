package com.codev.recruitment.carlaberdin.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.FragmentContactSummaryBinding;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;

public class ContactSummaryFragment extends Fragment {

    private FragmentContactSummaryBinding binding;

    private ContactViewModel mContactVM;

    private NavController mNavController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_contact_summary, container, false);

        View view = binding.getRoot();

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        binding.setViewModel(mContactVM);
        binding.setLifecycleOwner(requireActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.popBackStack();
            }
        });
        binding.btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show alert dialog
                showDeleteAlert();
            }
        });
        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = mContactVM.getCurrentlyViewing().getValue();

                contact.setFavorite(!contact.isFavorite());
                mContactVM.updateContact(contact);
                mContactVM.setCurrentlyViewing(contact);
            }
        });
    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(getString(R.string.delete_alert));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getString(R.string.delete),
                (dialog, id) -> {
                    mContactVM.deleteContact(mContactVM.getCurrentlyViewing().getValue());
                    mNavController.popBackStack();
                });

        builder1.setNegativeButton(
                getString(android.R.string.cancel),
                (dialog, id) -> {
                    // do nothing
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}