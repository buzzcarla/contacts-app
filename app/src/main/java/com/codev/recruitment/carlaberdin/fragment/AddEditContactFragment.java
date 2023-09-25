package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.FragmentAddEditContactBinding;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;

public class AddEditContactFragment extends Fragment {

    private FragmentAddEditContactBinding binding;

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
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_edit_contact, container, false);

        View view = binding.getRoot();

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        return view;
    }
}