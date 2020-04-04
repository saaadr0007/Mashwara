package com.saad.example.nearbyservices.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.retrofit.CustomAdapter;
import com.example.retrofit.GetDataService;
import com.example.retrofit.RetrofitClientInstance;
import com.example.retrofit.Retrophoto;
import com.example.retrofit.ShowInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.snackbar.Snackbar;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.fragment.PlaceAboutDetail;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.utils.AppController;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;

import retrofit2.Call;
import retrofit2.Callback;

public class PlaceListOnMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    public static final String TAG = PlaceListOnMapActivity.class.getSimpleName();

    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();

    private GoogleMap mGoogleMap;
    private boolean mMapReady = false;
    private String mLocationTag;
    private String mLocationName;
    private String mLocationQueryStringUrl;
    private ArrayList<String> getplaceid;
    private ArrayList<Place> getcommon=new ArrayList<>();
    private MapFragment mMapFragment;
    private static JSONArray rootJsonArray;
    ArrayList<String> getcommonplaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_on_map);
        getplaceid=new ArrayList<>();

        /**
         * Get the reference of the map fragment
         */
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        /**
         * get the intent and get the location Tag
         */
        mLocationTag = getIntent().getStringExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT);
        mLocationName = getIntent().getStringExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT);

        String currentLocation = getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        mLocationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                currentLocation + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=" + mLocationTag +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
                " " + mLocationName + " " + getString(R.string.list_tag);
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.place_list_placeholder_text_view))
                .setText(getResources().getString(R.string.near_by_tag) + " " + mLocationName +
                        " " + getString(R.string.list_tag));

        findViewById(R.id.place_list_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeDetailTransferIntent = new Intent(PlaceListOnMapActivity.this, PlaceListActivity.class);
                placeDetailTransferIntent.putParcelableArrayListExtra(
                        GoogleApiUrl.ALL_NEARBY_LOCATION_KEY, mNearByPlaceArrayList);
                placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, mLocationTag);
                placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT, mLocationName);
                startActivity(placeDetailTransferIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_in);
            }
        });
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
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMapReady = true;

        /**
         * Helper Method to put marker on Google map
         */
        getLocationOnGoogleMap(mLocationQueryStringUrl);
    }
    public void save(Context context, String jsonString) throws IOException {
        File rootFolder = context.getExternalFilesDir(null);
        File jsonFile = new File(rootFolder, "file.json");
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(jsonString);
        writer.close();

    }


    private void getLocationOnGoogleMap(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            rootJsonArray = response.getJSONArray("results");
                          System.out.println(rootJsonArray.toString());
                          Log.i("printjson",rootJsonArray.toString());
                           // save(PlaceListOnMapActivity.this,rootJsonArray.toString());
                            if (rootJsonArray.length() == 0)
                                ((TextView) findViewById(R.id.place_list_placeholder_text_view))
                                        .setText(getResources().getString(R.string.no_near_by_tag)
                                                + " " + mLocationName + " " + getString(R.string.found));
                            else {
                                for (int i = 0; i < rootJsonArray.length(); i++) {
                                    JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(i);
                                    String currentPlaceId = singlePlaceJsonObject.getString("place_id");
                                    getplaceid.add(currentPlaceId);
                                    System.out.println(currentPlaceId);
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
                                if (mMapReady) {
                                    //Set the camera position
                                    String currentLocationString = getSharedPreferences(
                                            GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                                            .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);
                                    String currentPlace[] = currentLocationString.split(",");
                                    LatLng currentLocation = new LatLng(Double.valueOf(currentPlace[0])
                                            , Double.valueOf(currentPlace[1]));
                                    CameraPosition cameraPosition = CameraPosition.builder()
                                            .target(currentLocation)
                                            .zoom(15)
                                            .bearing(0)
                                            .tilt(0)
                                            .build();
                                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                            1500, null);
                                    /**
                                     *  Set the marker on Google Map
                                     */
                                    for (int i = 0; i < mNearByPlaceArrayList.size(); i++) {
                                        Double currentLatitude = mNearByPlaceArrayList.get(i).getPlaceLatitude();
                                        Double currentLongitude = mNearByPlaceArrayList.get(i).getPlaceLongitude();
                                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                                        mGoogleMap.addMarker(new MarkerOptions()
                                                .position(currentLatLng)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_map)));
                                    }

                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(currentLocation)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)));
                                }
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

    @Override
    protected void onResume() {
        super.onResume();
        getcommon.clear();

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    public void getpersonalize(View view) {
        getcommonplaces = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        if (PlaceAboutDetail.favourite_list.isEmpty()) {
            //  Snackbar.make(PlaceListOnMapActivity.this, "This is main activity", Snackbar.LENGTH_LONG);
            Toast.makeText(this, "No Favorites added", Toast.LENGTH_LONG).show();


        } else {
            System.out.println(PlaceAboutDetail.favourite_list.get(0));
            Call<List<Retrophoto>> call = service.getpersonalized(PlaceAboutDetail.favourite_list.get(0));
            call.enqueue(new Callback<List<Retrophoto>>() {
                @Override
                public void onResponse(Call<List<Retrophoto>> call, retrofit2.Response<List<Retrophoto>> response) {
                    Log.d("ayakuch", "yeah");

                    getcommonplaces = searchCcmmon(response.body(), getplaceid);
                    System.out.println(getcommonplaces);

                    Intent placeDetailTransferIntent = new Intent(PlaceListOnMapActivity.this, PlaceListActivity.class);
                    placeDetailTransferIntent.putParcelableArrayListExtra(
                            GoogleApiUrl.ALL_NEARBY_LOCATION_KEY, getcommon);
                    placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, mLocationTag);
                    placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT, mLocationName);

                    startActivity(placeDetailTransferIntent);

                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_in);

                }

                @Override
                public void onFailure(Call<List<Retrophoto>> call, Throwable t) {
                    Log.d("niayakuch", "yeah");

                    Toast.makeText(PlaceListOnMapActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private ArrayList<String> searchCcmmon(List<Retrophoto> body, ArrayList<String> getplaceid) {
        ArrayList<String> getcommonplace=new ArrayList<>();
        ArrayList<String> getnocommon=new ArrayList<>();

        for(int i=0;i<body.size();i++)
        {
            for (int j=0;j<getplaceid.size();j++)
            {
                if(body.get(i).getPlace_id().equals(getplaceid.get(j)))
                {
                    //getcommonplace.add(getplaceid.get(j));
                    try {
                        JSONObject singlePlaceJsonObject = (JSONObject) rootJsonArray.get(j);
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

                        getcommon.add(singlePlaceDetail);

                        String placename = singlePlaceJsonObject.getString("name");
                        getcommonplace.add(placename);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                else
                {
                    getnocommon.add(getplaceid.get(j));
                }
            }
        }
        System.out.println("YUU"+getnocommon);
        return getcommonplace;
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/

    }





