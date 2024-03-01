package com.surajinfotech.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.surajinfotech.whatsapp.Adapters.UsersAdapter;
import com.surajinfotech.whatsapp.Models.MessageModel;
import com.surajinfotech.whatsapp.Models.Users;
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
        binding.backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        //
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final UsersAdapter chatAdapter = new UsersAdapter(messageModels, this);
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
                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            // getting data from Message Model
                            MessageModel model = dataSnapshot1.getValue(MessageModel.class);
                            if (model != null && !model.getUserId().equals(currentUserId)) {
                                messageModels.add(model);
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ChatDetailActivity", "Failed to read value.", error.toException());
                        Toast.makeText(ChatDetailActivity.this, "Failed to read data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.send.setOnClickListener(v -> {

            // setup chat message

            String message = binding.etMessage.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(ChatDetailActivity.this, "Please enter a message.", Toast.LENGTH_SHORT).show();
                return;
            }
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
                    .setValue(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // receiver
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .push()
                                    .setValue(model)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("ChatDetailActivity", "Message sent successfully.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ChatDetailActivity", "Failed to send message.", e);
                                            Toast.makeText(ChatDetailActivity.this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ChatDetailActivity", "Failed to send message.", e);
                            Toast.makeText(ChatDetailActivity.this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Initialize and set up the UsersAdapter to display users
        final ArrayList<Users> usersList = new ArrayList<>();
        final UsersAdapter usersAdapter = new UsersAdapter(usersList, this);
        binding.chatRecyclerView.setAdapter(usersAdapter);

        // Setting up layout manager for usersRecyclerView
        LinearLayoutManager usersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.chatRecyclerView.setLayoutManager(usersLayoutManager);

        // Fetching list of users except the current user
        database.getReference().child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Users user = dataSnapshot.getValue(Users.class);
//                            if (user != null && !user.getUserId().equals(senderId)) {
//                                usersList.add(user);
//                            }
                            if (user != null && senderId != null && user.getUserId() != null && senderId.equals(user.getUserId())) {
                                // Add user to the list
                                usersList.add(user);
                            }


                        }
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ChatDetailActivity", "Failed to read value.", error.toException());
                        Toast.makeText(ChatDetailActivity.this, "Failed to read data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
