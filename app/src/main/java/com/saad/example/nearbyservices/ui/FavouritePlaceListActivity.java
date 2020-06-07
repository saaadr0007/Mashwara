package com.saad.example.nearbyservices.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class  FavouritePlaceListActivity extends AppCompatActivity
//        implements
//        LoaderManager.LoaderCallbacks<Cursor> {
{

    public static final int FAVOURITE_PLACE_DETAIL_LOADER = 0;
    /**
     * ArrayList of the PlaceDetail
     */
    private ArrayList<Place> mFavouritePlaceArrayList = new ArrayList<>();
    private PlaceListAdapter mPlaceListAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase1;

    private GridLayoutManager mGridLayoutManager;

    //Adapter for RecyclerView to bind the data from Database using CursorAdapter
    private FavouritePlaceCursorAdapter mFavouritePlaceCursorAdapter;
    private FirebaseUser firebaseUser=null;
    private String userid=null;
    private ArrayList<Place> addFavPlace=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Favourites");

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(getString(R.string.favourite_place_list_string));
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
//
//
//                                                                       mFavouritePlaceCursorAdapter = new FavouritePlaceCursorAdapter( getApplicationContext(),addFavPlace);
//                                                                       mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
//                                                                       mRecyclerView.setAdapter(mFavouritePlaceCursorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);
//        //setup an Adapter to create a list item for each row of the favouritePlace item in the cursor
//        mFavouritePlaceCursorAdapter = new FavouritePlaceCursorAdapter(this, null);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
//        mRecyclerView.setAdapter(mFavouritePlaceCursorAdapter);

        //first time loader is initialize
        //getLoaderManager().initLoader(FAVOURITE_PLACE_DETAIL_LOADER, null, this);

// public void getfav(S)
// {
//     String jsonArrayTag = "jsonArrayTag";
//     JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//             locationQueryStringUrl, null,
//             new com.android.volley.Response.Listener<JSONObject>() {
//                 @Override
//                 public void onResponse(JSONObject response) {
//                     try {
//                         rootJsonArray = response.getJSONArray("results");
//                         System.out.println(rootJsonArray.toString());
//                         Log.i("printjson",rootJsonArray.toString());
//                         // save(PlaceListOnMapActivity.this,rootJsonArray.toString());
//                         if (rootJsonArray.length() == 0)
//                             ((TextView) findViewById(R.id.place_list_placeholder_text_view))
//                                     .setText(getResources().getString(R.string.no_near_by_tag)
//                                             + " " + mLocationName + " " + getString(R.string.found));
//                         else {
//                             for (int i = 0; i < rootJsonArray.length(); i++) {
//                                 JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);
//                                 String currentPlaceId = singlePlaceJsonObject.getString("place_id");
//                                 getplaceidofnearby.add(currentPlaceId);
//                                 System.out.println(currentPlaceId);
//                                 Double currentPlaceLatitude = singlePlaceJsonObject
//                                         .getJSONObject("geometry").getJSONObject("location")
//                                         .getDouble("lat");
//                                 Double currentPlaceLongitude = singlePlaceJsonObject
//                                         .getJSONObject("geometry").getJSONObject("location")
//                                         .getDouble("lng");
//                                 String currentPlaceName = singlePlaceJsonObject.getString("name");
//                                 String currentPlaceOpeningHourStatus = singlePlaceJsonObject
//                                         .has("opening_hours") ? singlePlaceJsonObject
//                                         .getJSONObject("opening_hours")
//                                         .getString("open_now") : "Status Not Available";
//                                 Double currentPlaceRating = singlePlaceJsonObject.has("rating") ?
//                                         singlePlaceJsonObject.getDouble("rating") : 0;
//                                 String currentPlaceAddress = singlePlaceJsonObject.has("vicinity") ?
//                                         singlePlaceJsonObject.getString("vicinity") :
//                                         "Address Not Available";
//                                 //2  Photo [] photoarr=getphotos(singlePlaceJsonObject);
//                                 Place singlePlaceDetail = new Place(
//                                         currentPlaceId,
//                                         currentPlaceLatitude,
//                                         currentPlaceLongitude,
//                                         currentPlaceName,
//                                         currentPlaceOpeningHourStatus,
//                                         currentPlaceRating,
//                                         currentPlaceAddress);
//                                 mNearByPlaceArrayList.add(singlePlaceDetail);
//
//                             }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
////    @Override
////    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
////
////        //define projection for the favouritePlace Details (No of Column)
////        String[] projection = {
////                PlaceDetailEntry._ID,
////                PlaceDetailEntry.COLUMN_PLACE_ID,
////                PlaceDetailEntry.COLUMN_PLACE_LATITUDE,
////                PlaceDetailEntry.COLUMN_PLACE_LONGITUDE,
////                PlaceDetailEntry.COLUMN_PLACE_NAME,
////                PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS,
////                PlaceDetailEntry.COLUMN_PLACE_RATING,
////                PlaceDetailEntry.COLUMN_PLACE_ADDRESS,
////                PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER,
////                PlaceDetailEntry.COLUMN_PLACE_WEBSITE,
////                PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK
////        };
////
////        return new CursorLoader(this,
////                PlaceDetailEntry.CONTENT_URI,
////                projection,
////                null,
////                null,
////                null);
////    }
////
////    @Override
////    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
////        //Swap the Cursor for loading new data
////        ((FavouritePlaceCursorAdapter) mRecyclerView.getAdapter()).swapCursor(data);
////    }
////
////    @Override
////    public void onLoaderReset(android.content.Loader<Cursor> loader) {
////        ((FavouritePlaceCursorAdapter) mRecyclerView.getAdapter()).swapCursor(null);
////    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//}
