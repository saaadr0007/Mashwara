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

import com.example.mashwara.Models.User;
import com.example.mashwara.Presenter.IloginPresenter;
import com.example.mashwara.Presenter.LoginPresenter;
import com.example.mashwara.R;
import com.example.mashwara.View.IloginView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import static android.widget.Toast.makeText;

public class login extends AppCompatActivity implements IloginView{
    private static final String TAG ="fb" ;
    private LoginPresenter loginPresenter;
    private FirebaseAuth mAuth;

    private static final String EMAIL = "email";
    private Button sigin;
    private TextView register, forgot;
    private EditText uname, pass;
    String username, password;
    com.facebook.login.widget.LoginButton loginButton;
    CallbackManager mcallbackManager;
    private String getPassword = "1234";
  //  CallbackManager mCallbackManager;
    //private String getEmail = "saad@gmail.com";
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;


    private void addinFirebase()
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        User user1=new User(uid,username,user.getEmail());
        databaseReference=database.getReference().child("Users").child(uid);
        databaseReference.setValue(user1);
        //child(userEmail).child("movie").setValue(title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        loginPresenter=new LoginPresenter((IloginView) this);


        mAuth = FirebaseAuth.getInstance();
        forgot = findViewById(R.id.checkBox);
        register = (TextView) findViewById(R.id.textView);
        sigin = (Button) findViewById(R.id.button);
        uname = (EditText) findViewById(R.id.emaileditText);
        pass = (EditText) findViewById(R.id.passeditText);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Forgot.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

//        sigin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                username=uname.getText().toString();
//                password=pass.getText().toString();
//
//                if (username.equals(""))
//                {
//                    uname.setError("Empty Field!");
//
//                }
//                else if(password.equals(""))
//                {
//                    pass.setError("Empty Field");
//                }
//                else{
//                    if(username.equals(getEmail) && password.equals(getPassword) ) {
//                       // Intent intent = new Intent(getApplicationContext(), TabLayout.class);
//                        Intent intent=new Intent(getApplicationContext(),ContentActivity.class);
//                        startActivity(intent);
//                    }
//                    else
//                    {
//                        makeText(getApplicationContext(),R.string.errormsg,Toast.LENGTH_SHORT).show();
//                    }
//            }
//            }
//        });
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = uname.getText().toString();
                password = pass.getText().toString();
                loginPresenter.onLogin(username,password);

                if (username.equals("")) {
                    uname.setError("Empty Field!");

                } else if (password.equals("")) {
                    pass.setError("Empty Field");
                } else {

                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                private String TAG = "user signing in";

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        addinFirebase();
                                        // Toast.makeText(th, "Success!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                                        updateUI();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Failure!", Toast.LENGTH_SHORT).show();
                                        //updateUI();
                                    }

                                    // ...
                                }
                            });
                }


            }

        });



        // Initialize Facebook Login button
        mcallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

// ...
private void handleFacebookAccessToken(AccessToken token) {
    Log.d(TAG, "handleFacebookAccessToken:" + token);

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        //FirebaseUser user = mAuth.getCurrentUser();
                        addinFirebase();
                        updateUI();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                       // updateUI();
                    }

                    // ...
                }
            });
}


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI();
    }

    private void updateUI() {
       // Toast.makeText(this,currentUser.getEmail(),Toast.LENGTH_SHORT);
        Intent i=new Intent(this,TrendingActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       mcallbackManager.onActivityResult(requestCode, resultCode, data);
//        addinFirebase();
//        updateUI();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onLoginResult(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}


