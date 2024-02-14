package com.surajinfotech.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.google.firebase.auth.FirebaseAuth;
import com.surajinfotech.whatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;

   FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the custom toolbar as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
    }
    // bringing menu on main page


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_custom, menu);

        // Apply text color to action bar menu items
//        for (int i = 0; i< menu.size(); i++){
//            MenuItem menuItem = menu.getItem(i);
//            applyMenuItemTextColor(menuItem, R.color.actionBarMenuItemColor);
//        }
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

//    private void applyMenuItemTextColor(MenuItem menuItem, int colorResId){
//        SpannableString spannableString = new SpannableString(menuItem.getTitle());
//        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, colorResId)), 0, spannableString.length(), 0);
//        menuItem.setTitle(spannableString);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_settings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.menu_logout)
        {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            // Close the current activity to prevent the user from
            // returning to it using the back button after signing out.
            finish();
        }

        return true;
    }
}