package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.FragmentContactSummaryBinding;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.Util;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;

/**
 * Fragment to display details of the selected Contact
 */
public class ContactSummaryFragment extends Fragment {

    private FragmentContactSummaryBinding mBinding;

    private ContactViewModel mContactVM;

    private NavController mNavController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_contact_summary, container, false);

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

        // show image if not null
        // image not included in data binding. images are saved as Base64 String in the database and needs to be decoded to Bitmap before displaying
        if (mContactVM.getCurrentlyViewing().getValue() != null && mContactVM.getCurrentlyViewing().getValue().getImage() != null) {
            mContactVM.setImage(Util.decodeBase64ToBitmap(mContactVM.getCurrentlyViewing().getValue().getImage()));
        }

        mBinding.btnBack.setOnClickListener(view1 -> mNavController.popBackStack());

        mBinding.btnDeleteContact.setOnClickListener(view12 -> {
            // show alert dialog
            showDeleteAlert();
        });

        mBinding.btnFavorite.setOnClickListener(view13 -> {
            Contact contact = mContactVM.getCurrentlyViewing().getValue();

            contact.setFavorite(!contact.isFavorite());
            mContactVM.setCurrentlyViewing(mContactVM.clone(contact));
            mContactVM.updateContact(contact);
        });

        mBinding.btnEdit.setOnClickListener(view14 -> mNavController.navigate(R.id.action_contactSummaryFragment_to_addEditContactFragment));

        mContactVM.getCapturedImage().observe(getViewLifecycleOwner(), bitmap -> mBinding.imgContact.setImageBitmap(bitmap));
    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
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

    @Override
    public void onResume() {
        super.onResume();
    }
}