package com.saad.example.nearbyservices.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.FavouritePlaceCursorAdapter;
import com.saad.example.nearbyservices.adapter.PlaceListAdapter;
import com.saad.example.nearbyservices.data.PlaceDetailContract.PlaceDetailEntry;
import com.saad.example.nearbyservices.model.Place;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class  FavouritePlaceListActivity extends AppCompatActivity
//        implements
//        LoaderManager.LoaderCallbacks<Cursor> {
{

    public static final int FAVOURITE_PLACE_DETAIL_LOADER = 0;
    /**
     * ArrayList of the PlaceDetail
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private ArrayList<Place> mFavouritePlaceArrayList = new ArrayList<>();
    private PlaceListAdapter mPlaceListAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseInstance;
    private TextView empty_view;
    private DatabaseReference mFirebaseDatabase1;

    private GridLayoutManager mGridLayoutManager;

    //Adapter for RecyclerView to bind the data from Database using CursorAdapter
    private FavouritePlaceCursorAdapter mFavouritePlaceCursorAdapter;
    private FirebaseUser firebaseUser=null;
    private String userid=null;
    private ArrayList<Place> addFavPlace=new ArrayList<>();

    public void navigation() {
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(FavouritePlaceListActivity.this, LandingActivity.class));
                        break;


                    case R.id.location_favourite_icon:
                      //  startActivity(new Intent(FavouritePlaceListActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.mycircle:
                        startActivity(new Intent(FavouritePlaceListActivity.this, MainActivityReq.class));


                      //  startActivity(new Intent(FavouritePlaceListActivity.this, FavouritePlaceListActivity.class));
                        break;

                    case R.id.pref_icon:
                        startActivity(new Intent(FavouritePlaceListActivity.this,PreferenceActivity.class));
                        break;

                }
                return false;
            }
        };
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Favourites");
        empty_view=(TextView) findViewById(R.id.empty_view);
        navigation();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(getString(R.string.favs));
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        userid = firebaseUser.getUid();
        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    addFavPlace.add(place);
                }
               // Toast.makeText(getApplicationContext(),"inside datachange",Toast.LENGTH_LONG).show();
                if (!addFavPlace.isEmpty()) {
                    mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
                    //setup an Adapter to create a list item for each row of the favouritePlace item in the cursor
                    mRecyclerView.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),"inside o",Toast.LENGTH_LONG).show();

                    mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    mPlaceListAdapter = new PlaceListAdapter(getApplicationContext(), addFavPlace);
                    mRecyclerView.setLayoutManager(mGridLayoutManager);
                    mRecyclerView.setAdapter(mPlaceListAdapter);
                }
                else
                    empty_view.setVisibility(View.VISIBLE);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
