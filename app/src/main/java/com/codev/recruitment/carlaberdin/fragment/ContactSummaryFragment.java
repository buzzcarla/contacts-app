package com.codev.recruitment.carlaberdin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        //here data must be an instance of the class MarsDataProvider

        mContactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        binding.setContact(mContactVM.getCurrentlyViewing());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}