package com.codev.recruitment.carlaberdin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.codev.recruitment.carlaberdin.lib.ContactsLib;
import com.codev.recruitment.carlaberdin.lib.DaggerRoomComponent;
import com.codev.recruitment.carlaberdin.repository.data.Contact;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}