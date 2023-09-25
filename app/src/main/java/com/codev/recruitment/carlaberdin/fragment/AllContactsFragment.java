package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.adapter.ContactsViewAdapter;
import com.codev.recruitment.carlaberdin.databinding.FragmentAllContactsBinding;
import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;

import java.util.List;


public class AllContactsFragment extends Fragment {

    private FragmentAllContactsBinding binding;

    private ContactViewModel mContactVM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_all_contacts, container, false);
        View view = binding.getRoot();
        //here data must be an instance of the class MarsDataProvider
        return view;

//        binding = FragmentAllContactsBinding.inflate(inflater, container, false);
//        View view = binding.getRoot();
//        return view;

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_all_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        mContactVM.getAllContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (contacts != null && contacts.size() > 0) {
                    ContactsViewAdapter adapter = new ContactsViewAdapter(contacts, new ContactsViewAdapter.CustomClickListener() {
                        @Override
                        public void contactClicked(Contact contact) {
                            mContactVM.setCurrentlyViewing(contact);
                            navController.navigate(R.id.action_allContactsFragment_to_contactSummaryFragment);
                            // Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();
                        }
                    });
                    binding.setMyAdapter(adapter);
                } else {
                    binding.llNoContacts.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_allContactsFragment_to_addEditContactFragment);
            }
        });

        // mContactVM.addContact(new Contact("John", "Wayne", "+639874851203", "john.wayne@email.com", ""));


    }

    @Override
    public void onResume() {
        super.onResume();

        mContactVM.resetCurrentlyViewing();
    }
}