package com.surajinfotech.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.surajinfotech.whatsapp.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;

    // receive user data in Chat Detail Activity
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        // data receiving on string from Firebase Auth
        // creating receiver id

        String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("userId");

        // bring data from Firebase auth
        String userName = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("profilePic");

        // setting data
        binding.chatUserName.setText(userName);
        // getting image using Picasso
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);

        // to move back
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}