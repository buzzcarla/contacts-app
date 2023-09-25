package com.codev.recruitment.carlaberdin.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.codev.recruitment.carlaberdin.R;
import com.codev.recruitment.carlaberdin.databinding.ContactItemBinding;
import com.codev.recruitment.carlaberdin.repository.data.Contact;
import com.codev.recruitment.carlaberdin.utils.Util;

import java.util.List;

/**
 * Custom Recycler View Adapter to display Contacts in the database
 */
public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ViewHolder> {
    private List<Contact> mAllContacts;
    private final CustomClickListener mContactItemClickListener;

    public ContactsViewAdapter(CustomClickListener customClickListener) {
        mContactItemClickListener = customClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewAdapter.ViewHolder holder, int position) {
        Contact contact = mAllContacts.get(position);
        holder.bind(contact, mContactItemClickListener);
        if (contact.getImage() != null) {
            // image not included in data binding. images are saved as Base64 String in the database and needs to be decoded to Bitmap before displaying
            holder.contactItemBinding.imgContact.setImageBitmap(Util.decodeBase64ToBitmap(contact.getImage()));
        }
    }

    @Override
    public int getItemCount() {
        return mAllContacts != null ? mAllContacts.size() : 0;
    }

    public void setData(List<Contact> contacts) {
        mAllContacts = contacts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ContactItemBinding contactItemBinding;

        public ViewHolder(ContactItemBinding userItemBinding) {
            super(userItemBinding.getRoot());
            this.contactItemBinding = userItemBinding;
        }

        public void bind(Object obj, CustomClickListener clickListener) {
            contactItemBinding.setVariable(BR.contact, obj);
            contactItemBinding.setItemClickListener(clickListener);
            contactItemBinding.executePendingBindings();
        }
    }

    // listener for item tap in the recycler view
    public interface CustomClickListener {
        void contactClicked(Contact contact);
    }
}
