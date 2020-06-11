package com.saad.example.nearbyservices.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus,mprofileFriendCount;
    private Button mProfileSendReqButton,mProfileDeclineReqButton;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    ProgressDialog mProgressDialog;
    private String mCurrent_state;

    DatabaseReference mfriendReqReference;
    DatabaseReference mDatabaseReference;
    DatabaseReference mFriendDatabase;
    DatabaseReference mNotificationReference;
    DatabaseReference mRootReference;
    DatabaseReference getmDatabaseReference;

    FirebaseUser mFirebaseUser;
    String user_id;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void navigation() {
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
                        break;


                    case R.id.location_favourite_icon:
                        startActivity(new Intent(ProfileActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.mycircle:
                        startActivity(new Intent(ProfileActivity.this, MainActivityReq.class));


                        //  startActivity(new Intent(FavouritePlaceListActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.pref_icon:
                        startActivity(new Intent(ProfileActivity.this,PreferenceActivity.class));
                        break;

                }
                return false;
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(getString(R.string.users));
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //---GETTING ID OF USER WHOSE PROFILE IS OPENED----
        user_id = getIntent().getStringExtra("user_id");

        //---AFTER CLICKING NOTIFICATION , IF USER_ID IS NOT FETCHED
        if(user_id==null){
            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

//        Log.e("user_id is",user_id);

        mProfileImage = (ImageView)findViewById(R.id.profileUserImage);
        mProfileName = (TextView)findViewById(R.id.profileUserName);
      //  mProfileStatus=(TextView)findViewById(R.id.profileUserStatus);
        mprofileFriendCount=(TextView)findViewById(R.id.profileUserFriends);
        mProfileSendReqButton=(Button)findViewById(R.id.profileSendReqButton);
        mProfileDeclineReqButton=(Button)findViewById(R.id.profileDeclineReqButton);

        //----IT WILL BECOME VISIBLE ONLY WHEN WE GET THE FRIEND REQUEST FROM THAT PERSON-----
        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
        mProfileDeclineReqButton.setEnabled(false);

        mfriendReqReference=FirebaseDatabase.getInstance().getReference().child("friend_request");
        mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendDatabase=FirebaseDatabase.getInstance().getReference().child("friends");
        mNotificationReference=FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootReference=FirebaseDatabase.getInstance().getReference();
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        //----fOR SETTING ONLINE---
        getmDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mFirebaseUser.getUid());

        mProgressDialog=new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Fetching Details");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mCurrent_state = "not_friends"; // 4 types--- "not_friends" , "req_sent"  , "req_received" & "friends"

        //----ADDING NAME , STATUS AND IMAGE OF USER----
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name=dataSnapshot.child("username").getValue().toString();
               // String display_image=dataSnapshot.child("image").getValue().toString();
                if (dataSnapshot.child("fb_id").getValue()!=null) {
                    String display_image = dataSnapshot.child("fb_id").getValue(String.class);
                    Picasso.with(getApplicationContext()) // Context
                            .load("https://graph.facebook.com/" + display_image + "/picture?type=large") // URL or file
                            .into(mProfileImage); // An ImageView object to show the loaded image
                }
                mProfileName.setText(display_name);
              //  Picasso.with(ProfileActivity.this).load(display_image).placeholder(R.drawable.man).into(mProfileImage);

                //----ADDING TOTAL  NO OF FRIENDS---
                mFriendDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long len = dataSnapshot.getChildrenCount();
                        mprofileFriendCount.setText("TOTAL FRIENDS : "+len);

                        //----SEEING THE FRIEND STATE OF THE USER---
                        //----ADDING THE TWO BUTTON-----
                        mfriendReqReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //----CHECKING IF FRIEND REQUEST IS SEND OR RECEIVED----
                                if(dataSnapshot.hasChild(user_id)){

                                    String request_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                                    if(request_type.equals("sent")){

                                        mCurrent_state="req_sent";
                                        mProfileSendReqButton.setText("Cancel Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqButton.setEnabled(false);
                                    }

                                    else if(request_type.equals("received")){
                                        mCurrent_state="req_received";
                                        mProfileSendReqButton.setText("Accept Friend Request");
                                        mProfileDeclineReqButton.setVisibility(View.VISIBLE);
                                        mProfileDeclineReqButton.setEnabled(true);
                                    }

                                    mProgressDialog.dismiss();
                                }

                                //---USER IS FRIEND----
                                else{

                                    mFriendDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                                            mProfileDeclineReqButton.setEnabled(false);

                                            if(dataSnapshot.hasChild(user_id)){
                                                mCurrent_state="friends";
                                                mProfileSendReqButton.setText("Unfriend This Person");
                                            }
                                            mProgressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            mProgressDialog.dismiss();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(ProfileActivity.this, "Error fetching Friend request data", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });

        //-------SEND REQUEST BUTTON IS PRESSED----
        mProfileSendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUserId= mFirebaseUser.getUid();

                if(mUserId.equals(user_id)){
                    Toast.makeText(ProfileActivity.this, "Cannot send request to your own", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Log.e("m_current_state is : ",mCurrent_state);
                mProfileSendReqButton.setEnabled(false);


                //-------NOT FRIEND STATE--------
                if(mCurrent_state.equals("not_friends")){

                    DatabaseReference newNotificationReference = mRootReference.child("notifications").child(user_id).push();

                    String newNotificationId = newNotificationReference.getKey();

                    HashMap<String,String> notificationData=new HashMap<String, String>();
                    notificationData.put("from",mFirebaseUser.getUid());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+mFirebaseUser.getUid()+ "/"+user_id + "/request_type","sent");
                    requestMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid()+"/request_type","received");
                    requestMap.put("notifications/"+user_id+"/"+newNotificationId,notificationData);

                    //----FRIEND REQUEST IS SEND----
                    mRootReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null){

                                Toast.makeText(ProfileActivity.this, "Friend Request sent successfully", Toast.LENGTH_SHORT).show();

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state= "req_sent";
                                mProfileSendReqButton.setText("Cancel Friend Request");

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Some error in sending friend Request", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                //-------CANCEL--FRIEND--REQUEST-----

                if(mCurrent_state.equals("req_sent")){

                    Map valueMap=new HashMap();
                    valueMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----FRIEND REQUEST IS CANCELLED----
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){

                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Friend Request Cancelled Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Cannot cancel friend request...", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });




                }

                //-------ACCEPT FRIEND REQUEST------

                if(mCurrent_state.equals("req_received")){
                    //-----GETTING DATE-----
                    Date current_date=new Date(System.currentTimeMillis());

                    //Log.e("----Date---",current_date.toString());
                    String date[]=current_date.toString().split(" ");
                    final String todays_date=(date[1] + " " + date[2] + "," + date[date.length-1]+" "+date[3]);

                    Map friendMap=new HashMap();
                    friendMap.put("friends/"+mFirebaseUser.getUid()+"/"+user_id+"/date",todays_date);
                    friendMap.put("friends/"+user_id+"/"+mFirebaseUser.getUid()+"/date",todays_date);

                    friendMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    friendMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //-------BECAME FRIENDS------
                    mRootReference.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError==null){

                                mProfileSendReqButton.setEnabled(true);
                                mCurrent_state = "friends";
                                mProfileSendReqButton.setText("Unfriend this person");
                                mProfileDeclineReqButton.setEnabled(false);
                                mProfileDeclineReqButton.setVisibility(View.INVISIBLE);

                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Error is " +databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


                //----UNFRIEND---THIS---PERSON----
                if(mCurrent_state.equals("friends")){

                    Map valueMap=new HashMap();
                    valueMap.put("friends/"+mFirebaseUser.getUid()+"/"+user_id,null);
                    valueMap.put("friends/"+user_id+"/"+mFirebaseUser.getUid(),null);

                    //----UNFRIENDED THE PERSON---
                    mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                mCurrent_state = "not_friends";
                                mProfileSendReqButton.setText("Send Friend Request");
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Successfully Unfriended...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mProfileSendReqButton.setEnabled(true);
                                Toast.makeText(ProfileActivity.this, "Cannot Unfriend..Contact Kshitiz..", Toast.LENGTH_SHORT).show();

                            }
                        }

                    });


                }

            }
        });


        //-----DECLING THE FRIEND REQUEST-----
        mProfileDeclineReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map valueMap=new HashMap();
                valueMap.put("friend_request/"+mFirebaseUser.getUid()+"/"+user_id,null);
                valueMap.put("friend_request/"+user_id+"/"+mFirebaseUser.getUid(),null);

                mRootReference.updateChildren(valueMap, new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){

                            mCurrent_state = "not_friends";
                            mProfileSendReqButton.setText("Send Friend Request");
                            mProfileSendReqButton.setEnabled(true);
                            Toast.makeText(ProfileActivity.this, "Friend Request Declined Successfully...", Toast.LENGTH_SHORT).show();

                            mProfileDeclineReqButton.setEnabled(false);
                            mProfileDeclineReqButton.setVisibility(View.INVISIBLE);
                        }
                        else{

                            mProfileSendReqButton.setEnabled(true);
                            Toast.makeText(ProfileActivity.this, "Cannot decline friend request...", Toast.LENGTH_SHORT).show();

                        }
                    }

                });


            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //getmDatabaseReference.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
       // getmDatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);

    }
}
