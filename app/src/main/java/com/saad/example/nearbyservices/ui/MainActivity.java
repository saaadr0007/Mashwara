 package com.saad.example.nearbyservices.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.saad.example.nearbyservices.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

 public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtName, txtEmail;
    private String userData[]={"id","first_name","middle_name","last_name","email","birthday","relationship_status","religion","work","languages","about","address","education","hometown","gender","location","interested_in","link","friends","likes","inspirational_people","favorite_teams","favorite_athletes","sports","political","books","movies","ratings","music","events","brand_teams","games","payment_pricepoints"};
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton= findViewById (R.id.login_button);
        txtName = findViewById(R.id.profile_name);
        txtEmail = findViewById(R.id.profile_email);
        circleImageView = findViewById(R.id.profile_pic);

        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){


            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        } );
    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
     }

     AccessTokenTracker tokenTracker= new AccessTokenTracker() {
         @Override
         protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){   //user Log out

                txtName.setText("");
                txtEmail.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this,"User Logged Out",Toast.LENGTH_LONG).show();
            }
            else{
                loadUserProfile(currentAccessToken);
            }
         }
     };
    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String id= object.getString("id");
                    String image_url="https://graph.facebook.com/"+id+"/picture?type=normal";

                    for (int i = 0; i < userData.length; i++) {
                        if(object.has(userData[i])){
                            System.out.println(userData[i]+"   "+object.getString(userData[i]));
                            System.out.println("");
                            System.out.println("");
                            System.out.println("");
                        }
                    }

                    String first_name=object.getString("first_name");
                    String last_name= object.getString("last_name");
                    String email=object.getString("email");
                    String birthday= object.getString("birthday");
                    txtEmail.setText(email);
                    txtName.setText(first_name+" "+last_name+" "+birthday);
                    RequestOptions requestOptions =new RequestOptions();  //glide
                    requestOptions.dontAnimate();
                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters =new Bundle();
        parameters.putString("fields","id,name,birthday,sports,gender,location,email,interested_in,education,inspirational_people,link,relationship_status,religion,work,languages,first_name,last_name,about,address,favorite_teams,favorite_athletes,middle_name,hometown,payment_pricepoints,political,books,friends,likes,movies,ratings,music,events,brand_teams,games,posts");
        request.setParameters(parameters);
        request.executeAsync();

    }
    private void checkLoginStatus(){

        if(AccessToken.getCurrentAccessToken()!=null){
            loadUserProfile(AccessToken.getCurrentAccessToken());

        }
    }


 }
