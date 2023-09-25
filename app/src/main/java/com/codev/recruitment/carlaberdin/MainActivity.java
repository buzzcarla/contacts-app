package com.codev.recruitment.carlaberdin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.codev.recruitment.carlaberdin.utils.Util;
import com.codev.recruitment.carlaberdin.vm.ContactViewModel;


public class MainActivity extends AppCompatActivity {


    private ContactViewModel contactVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactVM = new ViewModelProvider(this).get(ContactViewModel.class);

        SharedPreferences sharedPref = getSharedPreferences(Util.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(Util.KEY_FIRST_APP_LAUNCH, true)) {
            showFirstLaunchSetup();
        }


    }

    private void showFirstLaunchSetup() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.first_launch_setup);
        builder1.setMessage(R.string.first_launch_setup_msg);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                R.string.yes,
                (dialog, id) -> {
                    // use encryption
                    handleChangeInSharedPreferences(true);
                });

        builder1.setNegativeButton(
                R.string.no,
                (dialog, id) -> {
                    handleChangeInSharedPreferences(false);
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void handleChangeInSharedPreferences(boolean encryptionON) {
        // update shared preferences
        SharedPreferences sharedPref = getSharedPreferences(Util.KEY_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Util.KEY_ENCRYPTION_ON, encryptionON);
        editor.putBoolean(Util.KEY_FIRST_APP_LAUNCH, false);
        editor.apply();

        // display message
        Toast.makeText(getApplicationContext(), encryptionON ? getString(R.string.encryption_on) : getString(R.string.encryption_off), Toast.LENGTH_LONG).show();

        contactVM.initContactsLib(getApplicationContext());
    }
}