package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.adapter.ContactsViewAdapter;
import com.codev.recruitment.carlaberdin.databinding.FragmentAllContactsBinding;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;

/**
 * Fragment to display All contacts in the database in a recyclerView.
 */
public class AllContactsFragment extends Fragment {

    private FragmentAllContactsBinding mBinding;

    private ContactViewModel mContactVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_all_contacts, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        // create new recycler view adapter
        ContactsViewAdapter adapter = new ContactsViewAdapter(
                contact -> {
                    mContactVM.setCurrentlyViewing(contact);
                    navController.navigate(R.id.action_allContactsFragment_to_contactSummaryFragment);
                });
        mContactVM.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            // observe changes in all contacts, not included in data binding.
            // data needs to be intercepted if encryption is ON, so decrypted/readable data will be displayed
            if (contacts != null && contacts.size() > 0) {
                adapter.setData(mContactVM.intercept(contacts));
                mBinding.setMyAdapter(adapter);
            } else {
                // no contact list, show "No contacts" layout
                mBinding.llNoContacts.setVisibility(View.VISIBLE);
            }
        });

        mBinding.btnAddNew.setOnClickListener(view1 -> navController.navigate(R.id.action_allContactsFragment_to_addEditContactFragment));
    }

    @Override
    public void onResume() {
        super.onResume();

        // reset variables that are used to keep track of/display the last viewed Contact
        mContactVM.resetCurrentlyViewing();
    }

    @Override
    public void onDestroy() {
        mContactVM.getAllContacts().removeObservers(this);

        super.onDestroy();
    }
}