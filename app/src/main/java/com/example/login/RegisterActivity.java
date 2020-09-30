package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username,emailId,dob,password;
    Button register;
    DatabaseReference reference;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        dob=findViewById(R.id.dob);
        register = findViewById(R.id.button2);
        mFirebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        register.setOnClickListener(v -> {

            final String strusername = username.getText().toString().trim();
            final String stremail = emailId.getText().toString().trim();
            final String strpwd = password.getText().toString().trim();
            final String strdob = dob.getText().toString().trim();
            final boolean b = strusername.isEmpty() || stremail.isEmpty() || strpwd.isEmpty()|| strdob.isEmpty();
            if (b) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (strpwd.length() < 6) {

                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!b) {
                register(strusername,stremail,strpwd,strdob);
            }
        });
    }
    private void register(String username,String email,String pwd,String dob){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                String userid = firebaseUser.getUid();


                reference = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", userid);
                hashMap.put("email", email);
                hashMap.put("username", username);
                hashMap.put("dob", dob);
               reference.updateChildren(hashMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, DisplayActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                });
            }else  {
                progressDialog.dismiss();
                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                assert e != null;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
