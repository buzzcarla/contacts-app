package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.adapter.ContactsViewAdapter;
import com.codev.recruitment.carlaberdin.databinding.FragmentAllContactsBinding;
import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.repository.data.Contact;

import java.util.List;


public class AllContactsFragment extends Fragment {

    private FragmentAllContactsBinding binding;

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


        binding.recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));


        ContactsLib contactsLib = new ContactsLib(getActivity().getApplicationContext());
        contactsLib.getAllContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {

                ContactsViewAdapter adapter = new ContactsViewAdapter(contacts);
                binding.setMyAdapter(adapter);
            }
        });
    }
}