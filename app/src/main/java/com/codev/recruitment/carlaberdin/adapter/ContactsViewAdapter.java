package com.codev.recruitment.carlaberdin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.ContactItemBinding;
import com.codev.recruitment.carlaberdin.repository.data.Contact;

import java.util.List;
import java.util.Objects;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ViewHolder> {

    List<Contact> allContacts;

    public ContactsViewAdapter(List<Contact> allContacts) {
        this.allContacts = allContacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_item, parent, false);

        return new ViewHolder(binding);
//        return new ViewHolder(ContactItemBinding.inflate(LayoutInflater.from(parent.getContext()),
//                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewAdapter.ViewHolder holder, int position) {
        Contact contact = allContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return allContacts != null ? allContacts.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ContactItemBinding contactItemBinding;

        public ViewHolder(ContactItemBinding userItemBinding) {
            super(userItemBinding.getRoot());
            this.contactItemBinding = userItemBinding;
        }

        public void bind(Object obj) {
            contactItemBinding.setVariable(BR.contact, obj);
            contactItemBinding.executePendingBindings();
        }
    }

    public interface CustomClickListener {
        void contactClicked(Contact contact);
    }
}
