package com.saad.example.nearbyservices.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.MyService;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.SliderAdapterExample;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;
import com.saad.example.nearbyservices.utils.PlaceDetailProvider;
import com.saad.example.nearbyservices.utils.SaveSharedPreference;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandingActivity extends AppCompatActivity {
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private DatabaseReference mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;

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
                  //  startActivity(new Intent(LandingActivity.this, LandingActivity.class));
                    break;


                case R.id.location_favourite_icon:
                    startActivity(new Intent(LandingActivity.this, FavouritePlaceListActivity.class));
                    break;

                case R.id.mycircle:
                    startActivity(new Intent(LandingActivity.this, MainActivityReq.class));


                    //startActivity(new Intent(LandingActivity.this, FavouritePlaceListActivity.class));
                    break;


                case R.id.pref_icon:
                    startActivity(new Intent(LandingActivity.this,PreferenceActivity.class));
                    break;

            }
            return false;
        }
      };
}

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Sign Out the Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveSharedPreference.clearUserName(LandingActivity.this);
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
public void searchFeature() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);

        searchView.setQueryHint(getString(R.string.search_hint));
        ImageView searchIcon= searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
    searchIcon.setColorFilter(Color.WHITE);
    EditText editText = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
    editText.setHintTextColor(getResources().getColor(R.color.white));
    editText.setTextColor(getResources().getColor(R.color.white));



        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, PlaceSearchResultActivity.class)));
    }

    TextView textView;
    ImageView imageView;
    ConstraintLayout constraintLayout;
    View popupView ;
    String uid=null;
    private FirebaseAuth mAuth;

    // create the popup window

    PopupWindow popupWindow=null ;
    private CircleImageView fab_button;
    FrameLayout layout_MainMenu;
    View popupWindowView = null;
    //PopupWindow popupWindow = null;

    public void onButtonShowPopupWindowClick() {

        // inflate the layout of the popup window
        LayoutInflater popupLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            popupWindowView = popupLayoutInflater.inflate(R.layout.popup, null);
        } catch (InflateException e) {
            System.out.println(e.getMessage());
        }
        if (popupWindowView != null) {
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        if (popupWindow != null)
            popupWindow.showAtLocation(popupWindowView, Gravity.NO_GRAVITY, 100, 100);


        // dismiss the popup window when touched

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();

                layout_MainMenu.setAlpha( 0); // restore
                startActivity(new Intent(LandingActivity.this, PreferenceActivity.class));
                return true;
            }
        });
    }

    public void DirectToPrefs(final String uid)
    {
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Preferences");
      //  DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
      //  Log.d("firebbname",uid);
      //  rootRef.child("Favourites").child(uid);

        //Log.d("gettt",rootRef.child("Favourites").child(uid).getKey());
        mFirebaseDatabase1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    layout_MainMenu.setAlpha( 0);
                    onButtonShowPopupWindowClick();
                }
                else
                    Log.d("coount",String.valueOf(snapshot.getChildrenCount()));
                Log.d("snappp",snapshot.child(uid).getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mAuth = FirebaseAuth.getInstance();
        layout_MainMenu = (FrameLayout) findViewById( R.id.mainmenu);
        fab_button=(CircleImageView) findViewById(R.id.fab);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        Log.i("firebbbname","asada");
        Log.i("firebbbbname",uid);
        //DirectToPrefs(uid);

        getuserdetails(uid);
        constraintLayout=(ConstraintLayout) findViewById(R.id.background_img);
        SliderView sliderView=findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapterExample(this));
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        textView=findViewById(R.id.timefood);
        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this,usr_prof.class));
            }
        });

        searchFeature();
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            textView.setText("Good Morning");
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.morning) );
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            textView.setText("Good Afternoon");
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.afternoon_1) );
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            textView.setText("Good Evening");
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.night) );

        }else if(timeOfDay >= 21 && timeOfDay < 24){
            textView.setText("Good Night");
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.night) );
        }
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        textView.startAnimation(shake);
    }

    private void getuserdetails(String uid) {
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Users");
        mFirebaseDatabase1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("fb_id")) {
                   String fbid= (String) snapshot.child("fb_id").getValue();
                    Log.v("getfbbb", fbid);
                    show_profilepic(fbid,fab_button);

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void show_profilepic(String FB_ID, CircleImageView profilePictureView)
    {

        Picasso.with(getApplicationContext()) // Context
                .load("https://graph.facebook.com/" + FB_ID + "/picture?type=large") // URL or file
                .into(profilePictureView); // An ImageView object to show the loaded image
        // info.setVisibility(View.VISIBLE);
    }

    public void getmore(View view) {
        startActivity(new Intent(LandingActivity.this,HomeScreenActivity.class));
    }

    public void getrestaurants(View view) {
        String  locationTag = "restaurant";

        Intent placeTagIntent = new Intent(this, PlaceListOnMapActivity.class);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,PlaceDetailProvider.popularPlaceTagName[28]);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
        this.startActivity(placeTagIntent);
    }

    public void getfurniture(View view) {
        String  locationTag = "furniture_store";

        Intent placeTagIntent = new Intent(this, PlaceListOnMapActivity.class);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,PlaceDetailProvider.popularPlaceTagName[13]);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
        this.startActivity(placeTagIntent);
    }

    public void getshopping(View view) {
        String  locationTag = "clothing_store";

        Intent placeTagIntent = new Intent(this, PlaceListOnMapActivity.class);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,PlaceDetailProvider.popularPlaceTagName[30]);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
        this.startActivity(placeTagIntent);
    }

    public void getpumps(View view) {
        String  locationTag = "gas_station";

        Intent placeTagIntent = new Intent(this, PlaceListOnMapActivity.class);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,PlaceDetailProvider.popularPlaceTagName[14]);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
        this.startActivity(placeTagIntent);
    }

    public void getCafe(View view) {
        String  locationTag = "restaurant";

        Intent placeTagIntent = new Intent(this, PlaceListOnMapActivity.class);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT,PlaceDetailProvider.popularPlaceTagName[28]);
        placeTagIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, locationTag);
        this.startActivity(placeTagIntent);
    }
}
