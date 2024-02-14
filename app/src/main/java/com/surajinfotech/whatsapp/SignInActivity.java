package com.surajinfotech.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
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
//        private static final int REQ_ONE_TAP = 2;
         final int REQ_ONE_TAP = 2;
        auth = FirebaseAuth.getInstance();

       // configure Google SignIn
        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();





        // OnClick Listener Setup
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

       // Signup Activity
       binding.tvClickSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
               startActivity(intent);
           }
       });
       if (auth.getCurrentUser() != null)
       {
           Intent intent = new Intent(SignInActivity.this, MainActivity.class);
           startActivity(intent);
       }
    }

   // Google SignIn Activity 2nd Step

}