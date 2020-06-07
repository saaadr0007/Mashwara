package com.saad.example.nearbyservices.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SyncRequest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.model.User;
import com.saad.example.nearbyservices.utils.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG ="fb" ;
    private FirebaseAuth mAuth;
    private TextView info;
    public String email,birthday,name,gender;
    int flag= -1;
    private ImageView profilePictureView;

    private static final String EMAIL = "email";
    private Button sigin;
    private TextView register, forgot;
    private EditText uname, pass;
    String username, password;
    com.facebook.login.widget.LoginButton loginButton;
    CallbackManager mcallbackManager;
    public String FB_ID;
    private String getPassword = "1234";
    //  CallbackManager mCallbackManager;
    //private String getEmail = "saad@gmail.com";
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;



    private void addinFirebase()
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        if(user!=null) {
            String uid = user.getUid();
            User user1 = new User(uid, username, user.getEmail());
            SaveSharedPreference.setUserName(this,user.getEmail() );
            databaseReference = database.getReference().child("Users").child(uid);
            databaseReference.setValue(user1);
        }
        //child(userEmail).child("movie").setValue(title);
    }
    private void addinFirebase_fbuser(String email,String username,String birthday,String gender,String fb_id)
    {

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        if(user!=null) {
            String uid = user.getUid();
            User user1 = new User(uid, email, username,birthday,gender,fb_id);
            SaveSharedPreference.setUserName(this,user.getEmail() );
            databaseReference = database.getReference().child("Users").child(uid);
            databaseReference.setValue(user1);
        }
        //child(userEmail).child("movie").setValue(title);
    }


    private int getAge(String dobString){

        Date date = null;
        if (dobString.equals(null))
        {
            Log.v("bday_null","bdaaay_null");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (sdf.equals(null))
        {
                Log.v("sdf_null","sdf_null");
        }

        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }



        return age;
    }
    public void validateCredentials()
    {

        username = uname.getText().toString();
        password = pass.getText().toString();
        //loginPresenter.onLogin(username,password);

        if (username.equals("")) {
            uname.setError("Empty Field!");

        } else if (password.equals("")) {
            pass.setError("Empty Field");
        } else {
            final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
            pd.setMessage("Logging in...");
            pd.show();

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        private String TAG = "user signing in";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                addinFirebase();
                                // Toast.makeText(th, "Success!", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit the Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        register = (TextView) findViewById(R.id.textView);
        profilePictureView=(ImageView) findViewById(R.id.imagefb);
        sigin = (Button) findViewById(R.id.button);
        uname = (EditText) findViewById(R.id.emaileditText);
        pass = (EditText) findViewById(R.id.passeditText);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });


        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();

            }

        });
        info =(TextView)findViewById(R.id.fbdata);
        // Initialize Facebook Login button
        mcallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.fb);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        loginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
                pd.setMessage("Logging in...");
                pd.show();
               // handleFacebookAccessToken(loginResult.getAccessToken());
              FB_ID=  loginResult.getAccessToken().getUserId();

//                info.setText(
//                        "User ID: "
//                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
//                );

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email = object.getString("email");
                                    birthday = object.getString("birthday"); // 01/31/1980 format
                                    name = object.getString("name");
                                    gender = object.getString("gender");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                info.append(
//                                        "User Name: " + name
//                                                + "\n" +
//                                                "birthday: "
//                                                + birthday
//                                                + "\n" +
//                                                "Gender: "
//                                                + gender
//                                                + "\n" +
//                                                "Email: "
//                                                + email
//                                );
                                addinFirebase_fbuser(email,name,birthday,gender,FB_ID);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);


                request.executeAsync();
                updateUI();
                 pd.dismiss();



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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI();
    }

    private void updateUI() {
        // Toast.makeText(this,currentUser.getEmail(),Toast.LENGTH_SHORT);
        Intent i=new Intent(LoginActivity.this,LandingActivity.class);
        startActivity(i);
    }


    public boolean verifyCredentials(String email,String pass)
    {
        if(email.equals("tayyaba123@gmail.com")&&pass.equals("123456"))
        {
            return true;
        }
        else
            return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mcallbackManager.onActivityResult(requestCode, resultCode, data);

      //  updateUI();

    }
}