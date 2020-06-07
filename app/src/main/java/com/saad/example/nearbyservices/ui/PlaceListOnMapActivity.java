package com.saad.example.nearbyservices.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.model.Attribution;
import com.saad.example.nearbyservices.model.Photo;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.model.Preference;
import com.saad.example.nearbyservices.utils.AppController;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;


public class PlaceListOnMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    public static final String TAG = PlaceListOnMapActivity.class.getSimpleName();

    private StringBuilder stringBuilder;
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();
    private ArrayList<String> addFavPlace=new ArrayList<>();
   static private ArrayList<String> addprefsPlace=new ArrayList<>();
    private String getstring=" ";
    private GoogleMap mGoogleMap;
    private boolean mMapReady = false;
    private DrawerLayout mDrawerLayout;


    private ActionBarDrawerToggle mToggle;



    private NavigationView mNavigationView;

    ArrayList<String> getcommonplace=new ArrayList<>();
    ArrayList<String> getnocommon=new ArrayList<>();

    private String mLocationTag;
    private String mLocationName;
    private String mLocationQueryStringUrl;
    private ArrayList<String> getplaceidofnearby;
    private ArrayList<Place> getcommon=new ArrayList<>();
    private MapFragment mMapFragment;
    private FirebaseDatabase mFirebaseInstance;
    private static JSONArray rootJsonArray;
    ArrayList<String> getcommonplaces;
    private DatabaseReference mFirebaseDatabase1,getmFirebaseDatabase1;
    private String mCurrentPlace;
    private FirebaseUser firebaseUser=null;
    private String userid=null;
    private View personalizelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_on_map);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
//       setSupportActionBar(actionBar);
//        setTitle(R.string.app_name);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        getplaceidofnearby=new ArrayList<>();
        personalizelist=(View)findViewById(R.id.place_list_view1);
        mFirebaseInstance= FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Favourites");
        getmFirebaseDatabase1=mFirebaseInstance.getReference("Preferences");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        stringBuilder=new StringBuilder();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        //set the drawerListener
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        assert firebaseUser != null;
        userid= firebaseUser.getUid();



        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.location_favourite_icon:
                        startActivity(new Intent(PlaceListOnMapActivity.this, FavouritePlaceListActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.home:
                        startActivity(new Intent(PlaceListOnMapActivity.this, LandingActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;



                    case R.id.pref_icon:
                        startActivity(new Intent(PlaceListOnMapActivity.this,PreferenceActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;

//                    case R.id.about_icon:
//                        Dialog aboutDialog = new Dialog(HomeScreenActivity.this, R.style.AboutDialog);
//                        aboutDialog.setTitle(getString(R.string.about));
//                        aboutDialog.setContentView(R.layout.about_dialog);
//                        aboutDialog.show();
//                        mDrawerLayout.closeDrawers();
//                        break;
                }
                return false;
            }
        });
        getmFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Preference prefs = snapshot.getValue(Preference.class);
                    addprefsPlace.add(prefs.getPref_name());
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("placeee", dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    addFavPlace.add(place.getPlaceId());
                }

               // Log.i("joooin1", stringBuilder.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        personalizelist.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(final View v) {
                                                                   String s1 = "";
                                                                   for (int i = 0; i < addprefsPlace.size(); i++) {
                                                                       s1 = addprefsPlace.get(i);
                                                                       stringBuilder.append(s1);
                                                                       stringBuilder.append(" ");
                                                                   }
                                                                   String s = "";
                                                                   for (int i = 0; i < addFavPlace.size(); i++) {
                                                                       s = addFavPlace.get(i);
                                                                       stringBuilder.append(s);
                                                                       stringBuilder.append(" ");
                                                                   }
                                                                   Log.i("joooin2", stringBuilder.toString());
                                                                   Log.i("loc_tag", mLocationTag);
                                                                   getpersonalize(v, stringBuilder.toString(), mLocationTag);

                                                               }
                                                           });



            //        Toast.makeText(this,addFavPlace.get(0),Toast.LENGTH_SHORT).show();

//
                /**
         * Get the reference of the map fragment
         */
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        /**
         * get the intent and get the location Tag
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

    public void concatenateString(ArrayList<Place> places)
    {
//        String s =places.join

        ArrayList<String> listing = new ArrayList<String>();
        String item;
        for(int i=0;i<places.size();i++)
        {
            item = places.get(i).getPlaceId();
            listing.add(item);
            //getPath is a method in the customtype class which will return value in string format

        }
        final CharSequence[] fol_list = listing.toArray(new CharSequence[listing.size()]);

        String joinedString = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            joinedString = String.join(" ", fol_list);
        }
        Log.i("joiinn",joinedString);
//        for (int i=0;i<places.size();i++)
//        {
//
//        }
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


//    public Photo[] getphotos(JSONObject jPlace) throws JSONException {
//        Place place = new Place();
//        Photo[] photos = place.mPhotos;
//
//        if (!jPlace.isNull("photos")) {
//            JSONArray photosarray = jPlace.getJSONArray("photos");
//            photos = new Photo[photosarray.length()];
//            for (int i = 0; i < photosarray.length(); i++) {
//                photos[i] = new Photo();
//               photos[i].mWidth = ((JSONObject) photosarray.get(i)).getInt("width");
//              photos[i].mHeight = ((JSONObject) photosarray.get(i)).getInt("height");
//              photos[i].mPhotoReference = ((JSONObject) photosarray.get(i)).getString("photo_reference");
//                JSONArray attributions = ((JSONObject) photosarray.get(i)).getJSONArray("html_attributions");
//               photos[i].mAttributions = new Attribution[attributions.length()];
//                for (int j = 0; j < attributions.length(); j++) {
//                   photos[i].mAttributions[j] = new Attribution();
//                    photos[i].mAttributions[j].mHtmlAttribution = attributions.getString(j);
//                }
//            }
//            ImageDownloadTask[] imageDownloadTask = new ImageDownloadTask[photos.length];
//            int width=100;
//            int height=100;
//            String url = "https://maps.googleapis.com/maps/api/place/photo?";
//            String key = "key=AIzaSyDzkJaXGLibesCe4zA4oV0dUsFcXyMag2w";
//            String sensor = "sensor=true";
//            String maxWidth = "maxwidth=" + width;
//            String maxHeight = "maxheight=" + height;
//            url = url + "&" + key + "&" + sensor + "&" + maxWidth + "&" + maxHeight;
//
//            // URL for downloading the photo from Google Services
//            for (int i = 0; i < photos.length; i++) {
//                // Creating a task to download i-th photo
//                imageDownloadTask[i] = new ImageDownloadTask();
//                String photoReference = "photoreference=" + photos[i].mPhotoReference;
//                // URL for downloading the photo from Google Services
//                url = url + "&" + photoReference;
//                // Downloading i-th photo from the above url
//                imageDownloadTask[i].execute(url);
//            }
//        }
//    return photos;
//    }
    @SuppressLint("LongLogTag")
    private Bitmap downloadImage(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                // Starting image download
                bitmap = downloadImage(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Creating an instance of ImageView to display the downloaded image
            ImageView iView = new ImageView(getBaseContext());

            // Setting the downloaded image in ImageView
            iView.setImageBitmap(result);

            // Adding the ImageView to ViewFlipper
            //mFlipper.addView(iView);

            // Showing download completion message
            Toast.makeText(getBaseContext(), "Image downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void getLocationOnGoogleMap(String locationQueryStringUrl) {
        //Tag to cancel request
        String jsonArrayTag = "jsonArrayTag";
        JsonObjectRequest placeJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                locationQueryStringUrl, null,
                new com.android.volley.Response.Listener<JSONObject>() {
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
                                    getplaceidofnearby.add(currentPlaceId);
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
                                  //2  Photo [] photoarr=getphotos(singlePlaceJsonObject);
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
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    public void getpersonalize(View view,String s,String locationTag) {

        if(locationTag.equals("restaurant")) {
            getcommonplaces = new ArrayList<>();
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            if (addFavPlace.isEmpty() && addprefsPlace.isEmpty()) {
                //  Snackbar.make(PlaceListOnMapActivity.this, "This is main activity", Snackbar.LENGTH_LONG);
                Toast.makeText(this, "Please Add Favorites or Preferences ", Toast.LENGTH_LONG).show();
            } else {
                Log.i("favoriteee", s);
                Call<List<Retrophoto>> call = service.getpersonalized(s);
                Log.i("Caalll", call.request().url().toString());
                call.enqueue(new Callback<List<Retrophoto>>() {
                    @Override
                    public void onResponse(Call<List<Retrophoto>> call, Response<List<Retrophoto>> response) {
                        Log.i("plaaceid", String.valueOf(getplaceidofnearby));

                        if (response.body() != null) {
                            getcommonplaces = searchCcmmon(response.body(), getplaceidofnearby);
                            System.out.println(getcommonplaces);
                            Intent placeDetailTransferIntent = new Intent(PlaceListOnMapActivity.this, PlaceListActivity.class);
                            placeDetailTransferIntent.putParcelableArrayListExtra(
                                    GoogleApiUrl.ALL_NEARBY_LOCATION_KEY, getcommon);
                            placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, mLocationTag);
                            placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT, mLocationName);

                            startActivity(placeDetailTransferIntent);

                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_in);

                        } else System.out.println("Responsse null");
                    }

                    @Override
                    public void onFailure(Call<List<Retrophoto>> call, Throwable t) {
                        Log.d("niayakuch", "yeah");

                        Toast.makeText(PlaceListOnMapActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if(locationTag.equals("furniture_store"))
        {
            getcommonplaces = new ArrayList<>();
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            if (addFavPlace.isEmpty() && addprefsPlace.isEmpty()) {
                //  Snackbar.make(PlaceListOnMapActivity.this, "This is main activity", Snackbar.LENGTH_LONG);
                Toast.makeText(this, "No Favorites or Preferences Added", Toast.LENGTH_LONG).show();
            } else {
                Log.i("favoriteeefurn", s);
                Call<List<Retrophoto>> call = service.getpersonalizedfurn(s);
                Log.i("Caalllfun", call.request().url().toString());
                call.enqueue(new Callback<List<Retrophoto>>() {
                    @Override
                    public void onResponse(Call<List<Retrophoto>> call, Response<List<Retrophoto>> response) {
                        Log.i("plaaceid", String.valueOf(getplaceidofnearby));

                        if (response.body() != null) {
                            getcommonplaces = searchCcmmon(response.body(), getplaceidofnearby);
                            System.out.println(getcommonplaces);
                            addFavPlace.clear();
                            addprefsPlace.clear();
                            stringBuilder=new StringBuilder();
                            Intent placeDetailTransferIntent = new Intent(PlaceListOnMapActivity.this, PlaceListActivity.class);
                            placeDetailTransferIntent.putParcelableArrayListExtra(
                                    GoogleApiUrl.ALL_NEARBY_LOCATION_KEY, getcommon);
                            placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_TYPE_EXTRA_TEXT, mLocationTag);
                            placeDetailTransferIntent.putExtra(GoogleApiUrl.LOCATION_NAME_EXTRA_TEXT, mLocationName);

                            startActivity(placeDetailTransferIntent);

                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_in);

                        } else System.out.println("Responsse null");
                    }

                    @Override
                    public void onFailure(Call<List<Retrophoto>> call, Throwable t) {
                        Log.d("niayakuch", "yeah");

                        Toast.makeText(PlaceListOnMapActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

    }
    private ArrayList<String> searchCcmmon(List<Retrophoto> body, ArrayList<String> getplaceid) {
        int cond=(body.size()-25);
        Log.i("cond",String.valueOf(cond));
        for(int i=0;i<cond;i++)
        {
            for (int j=0;j<getplaceid.size();j++)
            {
                if(body.get(i).getPlace_id().equals(getplaceid.get(j)))
                {
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





