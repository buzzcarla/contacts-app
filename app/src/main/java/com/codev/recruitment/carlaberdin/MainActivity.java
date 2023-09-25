package com.codev.recruitment.carlaberdin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.codev.recruitment.carlaberdin.vm.ContactViewModel;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactViewModel contactVM = new ViewModelProvider(this).get(ContactViewModel.class);
    }
}