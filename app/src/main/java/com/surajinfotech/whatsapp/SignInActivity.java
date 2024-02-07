package com.surajinfotech.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.surajinfotech.whatsapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

  ActivitySignInBinding binding;

  FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivitySignInBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       // Firebase Authentication
        auth = FirebaseAuth.getInstance();

       binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               auth.signInWithEmailAndPassword(
                       binding.etEmail.getText().toString(),
                       binding.etPassword.getText().toString()).
                       addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (task.isSuccessful())
                       {
                           Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                           startActivity(intent);
                       }
                       else
                       {
                           Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }

                   }
               });

           }
       });
       if (auth.getCurrentUser() != null)
       {
           Intent intent = new Intent(SignInActivity.this, MainActivity.class);
           startActivity(intent);
       }
    }
}