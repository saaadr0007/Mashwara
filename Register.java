package com.example.mashwara.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mashwara.Activities.login;
import com.example.mashwara.Models.User;
import com.example.mashwara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private TextView signinText;
    private EditText email, username, password, retype_pass1;
    private String username1, password1, getEmail, retype;
    private Button register;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private void addinFirebase()
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        User user1=new User(uid,user.getEmail(),user.getDisplayName());
        databaseReference=database.getReference().child("Users").child(uid);
        databaseReference.setValue(user1);
        //child(userEmail).child("movie").setValue(title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        //mAuth = FirebaseAuth.getInstance();
        register=(Button)findViewById(R.id.Signup_button);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.paassword);
        retype_pass1 = (EditText) findViewById(R.id.Re_type_pass);
        signinText = findViewById(R.id.alreadyAccount);

        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register.this,login.class);
                startActivity(i);
            }
        });

        //sign up
        retype = retype_pass1.getText().toString();
        password1 = password.getText().toString();

        register.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "Registering User";

            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    email.setError("Empty Field");
                }else if(!email.getText().toString().contains("@")||(!email.getText().toString().contains(".com"))) {
                    email.setError("Enter Valid Email");
                }
        else if (username.getText().toString().equals("")) {
            username.setError("Empty Field!");
        } else if (password.getText().toString().equals("")) {
            password.setError("Empty Field");
        } else if (retype_pass1.getText().toString().equals("")) {
            retype_pass1.setError("Empty Field");
        } else if (!retype_pass1.getText().toString().equals(password.getText().toString())) {

            Toast.makeText(getApplicationContext(), R.string.errormsg2, Toast.LENGTH_SHORT).show();

        } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                private String TAG = "user registering";

                                @Override
                                public void onComplete(@NonNull Task <AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                       addinFirebase();
                                        // Toast.makeText(th, "Success!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        updateUI();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                       // updateUI();
                                        Toast.makeText(getApplicationContext(), "Failure!", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }

        });
    }
    private void updateUI() {
        Intent i=new Intent(this,TrendingActivity.class);
        startActivity(i);
    }

}

