package com.saad.example.nearbyservices.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.model.Preference;
import com.saad.example.nearbyservices.model.User;
import com.saad.example.nearbyservices.utils.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.saad.example.nearbyservices.ui.MyData.nameArray;

public class usr_prof extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private DatabaseReference mFirebaseDatabase1,getmFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private String userid=null;
    private Button mycircle;
    private LinearLayout linlay1;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    public String email,birthday,name,gender,fb_id;
    private FirebaseUser firebaseUser=null;
    private CircleImageView dp_fb;
    private EditText email_profile,age_profile,birthday_profile;
    private TextView usrname;

    @Override
    public boolean onNavigateUp() {
       onBackPressed();return true;
    }

    public void navigation() {
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(usr_prof.this, LandingActivity.class));
                        break;

                    case R.id.location_favourite_icon:
                        startActivity(new Intent(usr_prof.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.pref_icon:
                        startActivity(new Intent(usr_prof.this,PreferenceActivity.class));
                        break;
                    case R.id.mycircle:
                        startActivity(new Intent(usr_prof.this,MainActivityReq.class));
                        break;

                }
                return false;
            }
        };
    }

    public void init()
    {
        usrname=(TextView)findViewById(R.id.usrname);
        email_profile=(EditText)findViewById(R.id.email_profile);
        age_profile=(EditText)findViewById(R.id.age_profile);
        birthday_profile=(EditText)findViewById(R.id.birthday_profile);
        dp_fb=(CircleImageView)findViewById(R.id.dp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prof_);

       // mycircle=(Button)findViewById(R.id.mycircle);
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Users");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        linlay1=(LinearLayout) findViewById(R.id.linlay);
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        assert firebaseUser != null;
        userid= firebaseUser.getUid();

//        mycircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(usr_prof.this,MainActivityReq.class));
//            }
//        });


        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                 //   User user = snapshot.getValue(User.class);
                   // assert user != null;
                    init();
                    String fb_id1=null;
                    String email1 = dataSnapshot.child("email").getValue(String.class);
                    String name1 = dataSnapshot.child("username").getValue(String.class);
                    usrname.setText(name1);
                    email_profile.setText(email1);



                    if (dataSnapshot.child("birthday").getValue()!=null) {
                 String birthday1 = dataSnapshot.child("birthday").getValue(String.class);
                 if (dataSnapshot.child("fb_id").getValue()!=null)
                     fb_id1 = dataSnapshot.child("fb_id").getValue(String.class);

                 Log.v("bbbdaay",birthday1 + fb_id1 );
                 birthday_profile.setText(birthday1);
                if (fb_id1!=null) {
                    Picasso.with(getApplicationContext()) // Context
                            .load("https://graph.facebook.com/" + fb_id1 + "/picture?type=large") // URL or file
                            .into(dp_fb); // An ImageView object to show the loaded image
                }
                 String str[] = birthday1.split("/");
                 int month = Integer.parseInt(str[0]);
                 int day = Integer.parseInt(str[1]);
                 int year = Integer.parseInt(str[2]);

                 Date now = new Date();
                 int nowMonth = now.getMonth() + 1;
                 int nowYear = now.getYear() + 1900;
                 int result = nowYear - year;

                 if (month > nowMonth) {
                     result--;
                 } else if (month == nowMonth) {
                     int nowDay = now.getDate();

                     if (day > nowDay) {
                         result--;
                     }
                 }
                 age_profile.setText(String.valueOf(result));
             }else
             {
                     Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Add in detail to complete your profile", Snackbar.LENGTH_SHORT);
                     snackbar.show();


                    // addinFirebase_fbuser(name1,email1,);


                 }
             }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });




    }

    private void addinFirebase_fbuser(String name,String email,String birthday,String age)
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Users");
        if(user!=null) {
            String uid = user.getUid();
            User user1 = new User( name,email,birthday,age);
          //  SaveSharedPreference.setUserName(this,user.getEmail() );
            databaseReference = database.getReference().child("Users").child(uid);
            databaseReference.setValue(user1);
        }

    }
    public void show_profilepic(String FB_ID, CircleImageView profilePictureView)
    {

        Picasso.with(getApplicationContext()) // Context
                .load("https://graph.facebook.com/" + FB_ID + "/picture?type=large") // URL or file
                .into(profilePictureView); // An ImageView object to show the loaded image
        // info.setVisibility(View.VISIBLE);
    }

    public void savechanges(View view) {

        String name1 = usrname.getText().toString();
        String email1 = email_profile.getText().toString();
        if (name1 == null || email1 != null || birthday_profile.getText().toString() != null || age_profile.getText().toString() != null) {
            addinFirebase_fbuser(name1, email1, birthday_profile.getText().toString(), age_profile.getText().toString());

            Toast.makeText(this, "Changes Saved Successfully!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Please Add your Complete info!!!", Toast.LENGTH_SHORT).show();

    }
}
