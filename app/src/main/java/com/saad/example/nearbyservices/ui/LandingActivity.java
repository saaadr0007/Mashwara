package com.saad.example.nearbyservices.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
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
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Calendar;

public class LandingActivity extends AppCompatActivity {
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private DatabaseReference mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;

public void navigation() {
      mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.home:
                    startActivity(new Intent(LandingActivity.this, LandingActivity.class));
                    break;


                case R.id.location_favourite_icon:
                    startActivity(new Intent(LandingActivity.this, FavouritePlaceListActivity.class));
                    break;

//                case R.id.share_icon:
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout AroundMe Application");
//                    startActivity(Intent.createChooser(shareIntent, "Share App.."));
//                    break;

//                    case R.id.feedback_icon:
//                        Intent mailToIntent = new Intent(Intent.ACTION_SEND);
//                        mailToIntent.setData(Uri.parse("mailto:"));
//                        mailToIntent.setType("text/plain");
//                        mailToIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"saadrafique1000@gmail.com"});
//                        startActivity(Intent.createChooser(mailToIntent, "Send Mail.."));
//                        mDrawerLayout.closeDrawers();
//                        break;

                case R.id.pref_icon:
                    startActivity(new Intent(LandingActivity.this,PreferenceActivity.class));
                    break;

//                case R.id.about_icon:
//                    Dialog aboutDialog = new Dialog(LandingActivity.this, R.style.AboutDialog);
//                    aboutDialog.setTitle(getString(R.string.about));
//                    aboutDialog.setContentView(R.layout.about_dialog);
//                    aboutDialog.show();
//                    break;
            }
            return false;
        }
      };
}
public void searchFeature() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);

        searchView.setQueryHint(getString(R.string.search_hint));
    ImageView searchIcon=
            searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
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

        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        Log.i("firebbbname","asada");
        Log.i("firebbbbname",uid);
        DirectToPrefs(uid);
        constraintLayout=(ConstraintLayout) findViewById(R.id.background_img);
        SliderView sliderView=findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapterExample(this));
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        textView=findViewById(R.id.timefood);

        searchFeature();
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle i=intent.getExtras();
//                int getid=i.getInt("pic");
//                if(getid==1)
//                {
//                    textView.setText("BreakFast");
//                }else if(getid==2)
//                {
//                    textView.setText("Lunch");
//
//                }else if (getid==3)
//                {
//                    textView.setText("Dinner");
//
//                }
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.sadma.example.nearbyservices");
//        registerReceiver(broadcastReceiver,intentFilter);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            textView.setText("Good Morning");
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.morning) );
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            textView.setText("Good Afternoon");
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            constraintLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.afternoon) );

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
