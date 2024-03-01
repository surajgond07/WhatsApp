package com.surajinfotech.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.surajinfotech.whatsapp.ChatDetailActivity;
import com.surajinfotech.whatsapp.Models.MessageModel;
import com.surajinfotech.whatsapp.Models.Users;
import com.surajinfotech.whatsapp.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder>{

    private ArrayList<Users> list;
    private Context context;
    private String currentUserId; // Add a variable to store the current user ID

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
        this.currentUserId = currentUserId; // Initialize the current user ID
    }

    public UsersAdapter(ArrayList<MessageModel> messageModels, ChatDetailActivity context) {
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = list.get(position);

        // Exclude the current user from the list
        if (users.getUserId().equals(currentUserId)) {
            holder.itemView.setVisibility(View.GONE); // Hide the item view if it's the current user
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // Set layout params to 0 to prevent it from taking up space
            return; // Skip further processing
        }

        String profilePicUrl = users.getProfilePic();

        // Load user profile picture using Picasso
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Picasso.get().load(profilePicUrl).placeholder(R.drawable.avatar).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.avatar);
        }

        holder.userName.setText(users.getUserName());

        // Send user to ChatDetailActivity on item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilePic());
                intent.putExtra("username", users.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView userName, lastMessage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
