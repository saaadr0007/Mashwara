package com.saad.example.nearbyservices.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Base64;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.example.retrofit.Pojo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;
import com.saad.example.nearbyservices.utils.SaveSharedPreference;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends AwesomeSplash implements
        ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {

    public static final String TAG = SplashScreenActivity.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 100;
    public static final int LOCATION_PERMISSION_CODE = 101;
    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mCurrentLocationRequest;
    private String mCurrentLocation = "";
    public static List<Pojo> getDescripion = new ArrayList<>();
    public static List<Pojo> getDescripionfurn = new ArrayList<>();
    private SharedPreferences mLocationSharedPreferences;
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("restaurant_description.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public String readFile(String fileName) throws IOException
    {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"));

        String content = "";
        String line;
        while ((line = reader.readLine()) != null)
        {
            content = content + line;
        }

        return content;

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//       // String myJson=inputStreamToString(this.getResources().openRawResource());
//
//    }
    public void readingJSONS(){

        String jsonFileContent,jsonFileContent1;
        jsonFileContent = null;
        jsonFileContent1=null;
        try {
            jsonFileContent = readFile("restaurant_description.json");
            jsonFileContent1 = readFile("furniture_description.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        JSONArray jsonArray1 = null;
        try {
            jsonArray = new JSONArray(jsonFileContent);
            jsonArray1 = new JSONArray(jsonFileContent1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i<jsonArray.length(); i++)
        {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String key = null;
            try {
                key = jsonObj.getString("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String desc = null;
            try {
                desc = jsonObj.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getDescripion.add(new Pojo(key,desc));
        }
        for (int i = 0; i<jsonArray1.length(); i++)
        {
            JSONObject jsonObj1 = null;
            try {
                jsonObj1 = jsonArray1.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String key = null;
            try {
                key = jsonObj1.getString("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String desc = null;
            try {
                desc = jsonObj1.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getDescripionfurn.add(new Pojo(key,desc));
        }
        Log.d("getobjj",getDescripion.get(0).getDescripion());
        Log.d("getobjj",getDescripionfurn.get(0).getDescripion());

    }

    public void checkLoginStatus()
    {
        if(SaveSharedPreference.getUserName(this).length() == 0)
        {
            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));

        }
        else
        {
            startActivity(new Intent(SplashScreenActivity.this,LandingActivity.class));

        }
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {

        readingJSONS();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sadma.example.nearbyservices",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        configSplash.setBackgroundColor(R.color.accent_purple); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.travell); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.ZoomInLeft); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
       // configSplash.setPathSplash(SyncStateContract.Constants.DROID_LOGO);
        configSplash.setOriginalHeight(800); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(800); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(2); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.pink); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.white); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("Mashwara");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//        configSplash.setTitleFont("assets/Roboto-Bold.ttf"); //provide string to your font located in assets/fonts/





    }

    @Override
    public void animationsFinished() {

        buildGoogleApiClient();

        //startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
//        new Handler().postDelayed(new Runnable() {
//
//            /**
//             * Showing Splash Screen With timer
//             * and it is used for to display company logo
//             */
//
//            @Override
//            public void run() {
//                //Start HomeScreenActivity
//                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
//
//                //Stop SplashScreenActivity
//                finish();
//            }
//        }, SPLASH_SCREEN_TIMER);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected())
//            mGoogleApiClient.connect();
//    }

//    @Override
//    protected void onStop() {
//        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())
//            mGoogleApiClient.disconnect();
//        super.onStop();
//    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected())
                 mGoogleApiClient.connect();

       // Log.d("buildGoogleApi",mGoogleApiClient.toString());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        /**
         * Send Location Request to Google Location Service after Connecting
         * to GoogleApiClient
         */

        Log.d(TAG, "onConnected");
        Log.d("onconnected","onConnected");

        mCurrentLocationRequest = LocationRequest.create();
        mCurrentLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mCurrentLocationRequest.setInterval(60000);

        /**
         * Check runtime permission for Android M and high level SDK
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(SplashScreenActivity.this)
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{
                                                    Manifest.permission.ACCESS_FINE_LOCATION},
                                            LOCATION_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_CODE);
            } else
                getGPSPermission();
        } else
            getGPSPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            
            return;
        }
        if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) != null) {
            mCurrentLocation = String.valueOf(
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude())
                    + "," + String.valueOf(
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mCurrentLocationRequest,
                    this);
        }

        mLocationSharedPreferences = getApplicationContext().getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0);

        //SharedPreference to store current location
        SharedPreferences.Editor locationEditor = mLocationSharedPreferences.edit();
        locationEditor.putString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, mCurrentLocation);
        locationEditor.apply();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        //get the current location of the user
        mCurrentLocation = String.valueOf(location.getLatitude()) + "," +
                String.valueOf(location.getLongitude());

        SharedPreferences.Editor locationEditor = mLocationSharedPreferences.edit();
        locationEditor.putString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, mCurrentLocation);
        locationEditor.apply();

        Log.d(TAG, "onLocationChange");
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog all permission are correct;
                new Handler().postDelayed(new Runnable() {

                    /**
                     * Showing Splash Screen With timer
                     * and it is used for to display company logo
                     */

                    @Override
                    public void run() {
                        //Start HomeScreenActivity
                        checkLoginStatus();

                        // startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));

                        //Stop SplashScreenActivity
                        finish();
                    }
                }, SPLASH_SCREEN_TIMER);
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(SplashScreenActivity.this, LOCATION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Log.d("actttres","aasa");
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mCurrentLocationRequest,
                        this);

                //Start HomeScreenActivity

              startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                //Stop SplashScreenActivity
                finish();
            } else {
                new AlertDialog.Builder(SplashScreenActivity.this)
                        .setTitle(R.string.gps_permission_title)
                        .setMessage(R.string.gps_permission_message)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Retry for GPS Permission
                                getGPSPermission();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Kill the application
                                finish();
                            }
                        }).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getGPSPermission();
        }
    }

    /**
     * GPS Permission Dialog
     */
    private void getGPSPermission() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mCurrentLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(SplashScreenActivity.this);
    }
}
