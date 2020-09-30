package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    FirebaseAuth mFirebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        progressDialog = new ProgressDialog(LoginActivity.this);

        btnSignIn.setOnClickListener(v -> {
            String email = emailId.getText().toString().trim();
            String pwd = password.getText().toString().trim();
            if(email.isEmpty() || pwd.isEmpty()){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
            else {
                progressDialog.show();
                progressDialog.setMessage("Please wait...");
                mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, task -> {
                    if(!task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Intent intToHome = new Intent(LoginActivity.this,DisplayActivity.class);
                                intToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intToHome);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

        });



    }

}
