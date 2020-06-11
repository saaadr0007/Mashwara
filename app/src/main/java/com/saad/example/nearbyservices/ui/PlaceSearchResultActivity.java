package com.saad.example.nearbyservices.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.PlaceListAdapter;
import com.saad.example.nearbyservices.model.Filter;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.utils.AppController;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;

public class PlaceSearchResultActivity extends AppCompatActivity {

    public static final String TAG = PlaceSearchResultActivity.class.getSimpleName();

    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search_result_acitivity);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        database = FirebaseDatabase.getInstance();


        mProgressBar.setVisibility(View.VISIBLE);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = getString(R.string.search_result_string);
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handleIntent(getIntent());
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String locationName = intent.getStringExtra(SearchManager.QUERY);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                databaseReference = database.getReference().child("Search History").child(uid);
                Filter f=new Filter(locationName);
                databaseReference.push().setValue(f);


            }

            String currentLocation = getSharedPreferences(
                    GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                    .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

            String locationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                    GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                    currentLocation + "&" + GoogleApiUrl.RANK_BY_TAG + "=" + GoogleApiUrl.DISTANCE_TAG +
                    "&" + GoogleApiUrl.KEYWORD_TAG + "=" + locationName.replace(" ", "%20") + "&" +
                    GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;
            Log.d(TAG, locationQueryStringUrl);

            getLocationDetails(locationQueryStringUrl);

        }
    }

    private void getLocationDetails(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mProgressBar.setVisibility(View.GONE);
                            JSONArray rootJsonArray = response.getJSONArray("results");
                            for (int i = 0; i < rootJsonArray.length(); i++) {
                                JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);

                                String currentPlaceId = singlePlaceJsonObject.getString("place_id");
                                Double currentPlaceLatitude = singlePlaceJsonObject
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lat");
                                Double currentPlaceLongitude = singlePlaceJsonObject
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lng");
                                String currentPlaceName = singlePlaceJsonObject.getString("name");
                                String currentPlaceOpeningHourStatus = singlePlaceJsonObject
                                        .has("opening_hours") ? singlePlaceJsonObject
                                        .getJSONObject("opening_hours")
                                        .getString("open_now") : "Status Not Available";
                                Double currentPlaceRating = singlePlaceJsonObject.has("rating") ?
                                        singlePlaceJsonObject.getDouble("rating") : 0;
                                String currentPlaceAddress = singlePlaceJsonObject.has("vicinity") ?
                                        singlePlaceJsonObject.getString("vicinity") :
                                        "Address Not Available";



                                Place singlePlaceDetail = new Place(
                                        currentPlaceId,
                                        currentPlaceLatitude,
                                        currentPlaceLongitude,
                                        currentPlaceName,
                                        currentPlaceOpeningHourStatus,
                                        currentPlaceRating,
                                        currentPlaceAddress);
                                mNearByPlaceArrayList.add(singlePlaceDetail);

                            }
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

                            if (mNearByPlaceArrayList.size() == 0) {
                                findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                findViewById(R.id.empty_view).setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                PlaceListAdapter placeListAdapter =
                                        new PlaceListAdapter(PlaceSearchResultActivity.this,
                                                mNearByPlaceArrayList);
                                recyclerView.setLayoutManager(
                                        new GridLayoutManager(PlaceSearchResultActivity.this, 1));
                                recyclerView.setAdapter(placeListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                    }
                });
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(placeJsonObjectRequest, jsonArrayTag);
    }
}
