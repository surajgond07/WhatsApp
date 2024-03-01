package com.surajinfotech.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.surajinfotech.whatsapp.Adapters.ChatAdapter;
import com.surajinfotech.whatsapp.Models.MessageModel;
import com.surajinfotech.whatsapp.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.Date;

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
        // bring receiver id from firebase
        final String senderId = auth.getUid();
        // bring receiver id from adapter
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

        //
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this);
        // setting up adapter on Recycler View
        binding.chatRecyclerView.setAdapter(chatAdapter);

        //setting up layout manager 
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        // setting for setOnClickListener sending button

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        // getting data from Firebase
        database.getReference().child("chats")
                        .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    // getting data from Message Model
                                    MessageModel model = dataSnapshot1.getValue(MessageModel.class);
                                    messageModels.add(model);
                                }
                                chatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.send.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                // setup chat message

                String message = binding.etMessage.getText().toString();
                //getting date & timestamp
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                // setting empty after sending message

                binding.etMessage.setText("");
                // creating child on database & creating separate node on firebase
                // getReference to store anything on database
                // sender on success listener

                database.getReference().child("chats")
                        // creating sender + receiver
                        .child(senderRoom)
                        //creating node using push() method
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // receiver
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        });


            }
        });
    }
}