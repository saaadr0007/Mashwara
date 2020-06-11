package com.saad.example.nearbyservices.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.PlaceListAdapter;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;

public class PlaceListActivity extends AppCompatActivity {

    public static final String TAG = PlaceListActivity.class.getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private PlaceListAdapter mPlaceListAdapter;

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
                        startActivity(new Intent(PlaceListActivity.this, LandingActivity.class));
                        break;


                    case R.id.location_favourite_icon:
                          startActivity(new Intent(PlaceListActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.mycircle:
                        startActivity(new Intent(PlaceListActivity.this, MainActivityReq.class));


                        //  startActivity(new Intent(FavouritePlaceListActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.pref_icon:
                        startActivity(new Intent(PlaceListActivity.this,PreferenceActivity.class));
                        break;

                }
                return false;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /**
         * get the intent and get the location Tag
         */
        String locationTag = getIntent().getStringExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT);
        String locationName = getIntent().getStringExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT);
        String currentLocation = getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        String locationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                currentLocation + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + locationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Log.d(TAG, locationQueryStringUrl);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
       // setTitle(getString(R.string.favs));
       // actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));


        //setSupportActionBar(actionBar);
        //String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
        String actionBarTitleText = " " + locationName + " " + getString(R.string.list_tag);
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNearByPlaceArrayList = getIntent()
                .getParcelableArrayListExtra(GoogleApiUrl.ALL_NEARBY_LOCATION_KEY);
        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

        if (mNearByPlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGridLayoutManager = new GridLayoutManager(this, 1);
            mPlaceListAdapter = new PlaceListAdapter(this, mNearByPlaceArrayList);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(mPlaceListAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
