package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static java.security.AccessController.getContext;

public class DisplayActivity extends AppCompatActivity {
    TextView username,dob,email;
    Button logout;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        username=findViewById(R.id.username);
        dob=findViewById(R.id.dob);
        email=findViewById(R.id.email);
        logout=findViewById(R.id.logout);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        logout.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DisplayActivity.this,MainActivity.class));
        });
        userinfo();
    }
    private void userinfo(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }


                User user=dataSnapshot.getValue(User.class);
                if (user==null)
                    username.setText("anonymous");

                else if (user!=null){

                    username.setText(user.getUsername());
                    dob.setText(user.getDob());
                    email.setText(user.getEmail());

                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}
}
