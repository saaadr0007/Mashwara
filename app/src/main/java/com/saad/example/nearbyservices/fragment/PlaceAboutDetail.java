package com.saad.example.nearbyservices.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.data.PlaceDetailContract.PlaceDetailEntry;
import com.saad.example.nearbyservices.model.Favorites;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.ui.SplashScreenActivity;
import com.saad.example.nearbyservices.utils.GoogleApiUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceAboutDetail extends Fragment implements OnMapReadyCallback {


    private static final int PERMISSION_REQUEST_CODE = 100;
    /**
     * All references of the views
     */

    private GoogleMap mGoogleMap;
    private int[] flag = {0};
    private boolean mMapReady = false;
    private Place mCurrentPlace;
    private DatabaseReference mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private TextView mLocationAddressTextView;
    private boolean getans=false;

    private TextView mLocationPhoneTextView;
    private TextView mLocationWebsiteTextView;
    private TextView mLocationOpeningStatusTextView,description;
    public static ArrayList<String> favourite_list=new ArrayList<>();
    private ImageView mFavouriteImageIcon;
    private String getDesc=null;
    private String userid=null;


    private FirebaseUser firebaseUser=null;

    public PlaceAboutDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_place_about_detail, container, false);

        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Favourites");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid= firebaseUser.getUid();

        mLocationAddressTextView = (TextView) rootView.findViewById(R.id.location_address_text_view);
        description=(TextView)rootView.findViewById(R.id.description);
        mLocationPhoneTextView = (TextView) rootView.findViewById(R.id.location_phone_number_text_view);
        mLocationWebsiteTextView = (TextView) rootView.findViewById(R.id.location_website_text_view);
        mLocationOpeningStatusTextView = (TextView) rootView.findViewById(R.id.location_status_text_view);
        mFavouriteImageIcon = (ImageView) rootView.findViewById(R.id.location_favourite_icon);

        ((ImageView) rootView.findViewById(R.id.small_location_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_phone_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_website_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.color_primary));
        ((ImageView) rootView.findViewById(R.id.small_location_status_icon))
                .setColorFilter(ContextCompat.getColor(getActivity(), R.color.color_primary));

        mCurrentPlace = getArguments().getParcelable(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY);

        SearchforDesc(mCurrentPlace.getPlaceId());

        rootView.findViewById(R.id.location_phone_container).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Check runtime permission for Android M and high level SDK
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (shouldShowRequestPermissionRationale(
                                        Manifest.permission.CALL_PHONE)) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle(R.string.call_permission_title)
                                            .setMessage(R.string.call_permission_message)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            })
                                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            }).show();
                                } else
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                            PERMISSION_REQUEST_CODE);
                            } else
                                makeCallToPlace();
                        } else
                            makeCallToPlace();
                    }
                });
        rootView.findViewById(R.id.location_website_container).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentPlace.getPlaceWebsite().charAt(0) != 'h')
                            Toast.makeText(getActivity(), R.string.website_not_registered_string,
                                    Toast.LENGTH_SHORT).show();
                        else {
                            Intent websiteUrlIntent = new Intent(Intent.ACTION_VIEW);
                            websiteUrlIntent.setData(Uri.parse(mCurrentPlace.getPlaceWebsite()));
                            getActivity().startActivity(websiteUrlIntent);
                        }
                    }
                }
        );
        if (mCurrentPlace != null){
            mLocationAddressTextView.setText(mCurrentPlace.getPlaceAddress());
            mLocationPhoneTextView.setText(mCurrentPlace.getPlacePhoneNumber());
            mLocationWebsiteTextView.setText(mCurrentPlace.getPlaceWebsite());
            mLocationOpeningStatusTextView.setText(mCurrentPlace.getPlaceOpeningHourStatus());
            description.setText(getDesc);
        }


        rootView.findViewById(R.id.location_favourite_container).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mFavouriteImageIcon.getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(getActivity(),
                                        R.drawable.ic_favorite_border_white).getConstantState())) {

            Place storePlace=new Place( mCurrentPlace.getPlaceId(),mCurrentPlace.getPlaceLatitude()
                    ,mCurrentPlace.getPlaceLongitude(), mCurrentPlace.getPlaceName(),mCurrentPlace.getPlaceOpeningHourStatus(),
                    mCurrentPlace.getPlaceRating(), mCurrentPlace.getPlaceAddress(), mCurrentPlace.getPlacePhoneNumber(),
                    mCurrentPlace.getPlaceWebsite(), mCurrentPlace.getPlaceShareLink());

                            ContentValues currentPlaceDetail = new ContentValues();
                            //get and insert the data into database
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_ID,
                                    mCurrentPlace.getPlaceId());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_LATITUDE,
                                    mCurrentPlace.getPlaceLatitude());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_LONGITUDE,
                                    mCurrentPlace.getPlaceLongitude());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_NAME,
                                    mCurrentPlace.getPlaceName());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_OPENING_HOUR_STATUS,
                                    mCurrentPlace.getPlaceOpeningHourStatus());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_RATING,
                                    mCurrentPlace.getPlaceRating());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_ADDRESS,
                                    mCurrentPlace.getPlaceAddress());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_PHONE_NUMBER,
                                    mCurrentPlace.getPlacePhoneNumber());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_WEBSITE,
                                    mCurrentPlace.getPlaceWebsite());
                            currentPlaceDetail.put(PlaceDetailEntry.COLUMN_PLACE_SHARE_LINK,
                                    mCurrentPlace.getPlaceShareLink());

                            //Insert Data into Database
                            getContext().getContentResolver().insert(PlaceDetailEntry.CONTENT_URI,
                                    currentPlaceDetail);

                            mFirebaseDatabase1.child(userid).push().setValue(storePlace);
                            favourite_list.add(mCurrentPlace.getPlaceId());
                            Toast.makeText(getActivity(), R.string.place_added_to_favourite_string,
                                    Toast.LENGTH_SHORT).show();
                            mFavouriteImageIcon.setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(), R.drawable.ic_favorite_white));
                        } else {
                                Toast.makeText(getActivity(),"Removeddddddd",Toast.LENGTH_LONG).show();
//                            Uri placeDetailUrl = PlaceDetailEntry.CONTENT_URI;
//                            String selection = PlaceDetailEntry.COLUMN_PLACE_ID + "=?";
//                            String[] selectionArgs = new String[]{String.valueOf(mCurrentPlace
//                                    .getPlaceId())};
//
//                            //Delete query for delete the place from favourite list
//                            getContext().getContentResolver().delete(placeDetailUrl, selection,
//                                    selectionArgs);
                            //mFirebaseDatabase1 = mFirebaseDatabase1.child(userid);
                            //Query mQueryRef=mFirebaseDatabase1.child(userid);

                            mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("placeee",dataSnapshot.getKey());
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                        Place place = snapshot.getValue(Place.class);
                                        assert place != null;
                                        if (place.getPlaceId() != null)
                                            if(place.getPlaceId().equals(mCurrentPlace.getPlaceId()))
                                            {
                                                snapshot.getRef().removeValue();
                                                //break;
                                            }
                                    }
                                    //Log.i("placeee",place.getPlaceId());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });
                            mFavouriteImageIcon.setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(), R.drawable.ic_favorite_border_white));
                            Toast.makeText(getActivity(), R.string.place_removed_to_favourite_string,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("placeee", dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    assert place != null;
                    if (place.getPlaceId()!=null && mCurrentPlace.getPlaceId()!=null)
                        if (place.getPlaceId().equals(mCurrentPlace.getPlaceId()) && getActivity()!=null) {
                            ((ImageView) rootView.findViewById(R.id.location_favourite_icon)).
                            setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white));
                            Log.i("getanss","yeaddd");
                           break;

                        }else if(getActivity()!=null)
                        {
                            Log.i("inside111","inside111");
                            ((ImageView) rootView.findViewById(R.id.location_favourite_icon)).
                                    setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_white));
                        }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ccancel","onCancelled");
            }
        });





//        Log.i("getttans",String.valueOf(getans));
//        if (isPlaceStoreInDatabase(mCurrentPlace.getPlaceId())) {
//            Log.i("inside","inside");
//            ((ImageView) rootView.findViewById(R.id.location_favourite_icon)).
//                    setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_white));
//        } else {
//            Log.i("inside111","inside111");
//            ((ImageView) rootView.findViewById(R.id.location_favourite_icon)).
//                    setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_border_white));
//        }


        return rootView;
    }

    private void SearchforDesc(String placeId) {
       for (int i=0;i<SplashScreenActivity.getDescripion.size();i++)
       {
           if(placeId.equals(SplashScreenActivity.getDescripion.get(i).getKey()))
           {
                getDesc=SplashScreenActivity.getDescripion.get(i).getDescripion();
               break;
           }
       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makeCallToPlace();
        }
    }

    @SuppressLint("MissingPermission")
    private void makeCallToPlace() {
        if (mCurrentPlace.getPlacePhoneNumber().charAt(0) != '+')
            Toast.makeText(getActivity(), R.string.phone_number_not_registered_string,
                    Toast.LENGTH_SHORT).show();
        else {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + mCurrentPlace.getPlacePhoneNumber()));
            getContext().startActivity(phoneIntent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMapReady = true;
        mGoogleMap = googleMap;
        String currentLocationString = getContext().getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);
        String currentPlace[] = currentLocationString.split(",");

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(Double.valueOf(currentPlace[0])
                        , Double.valueOf(currentPlace[1])))
                .zoom(13)
                .bearing(0)
                .tilt(0)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mGoogleMap.addMarker(new MarkerOptions()
                .position((new LatLng(Double
                        .valueOf(currentPlace[0]), Double.valueOf(currentPlace[1]))))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position((new LatLng(
                        mCurrentPlace.getPlaceLatitude(), mCurrentPlace.getPlaceLongitude())))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_map)));

        PolylineOptions joinTwoPlace = new PolylineOptions();
        joinTwoPlace.geodesic(true).add(new LatLng(Double.valueOf(currentPlace[0])
                , Double.valueOf(currentPlace[1])))
                .add(new LatLng(mCurrentPlace.getPlaceLatitude(), mCurrentPlace.getPlaceLongitude()))
                .width(5)
                .color(ContextCompat.getColor(getActivity(), R.color.color_primary));

        mGoogleMap.addPolyline(joinTwoPlace);
    }

    private boolean isPlaceStoreInDatabase(final String placeId) {

        //        Uri for requesting data from database
//        Uri placeContentUri = PlaceDetailEntry.CONTENT_URI;
//
//        //ArrayList for store all placeId information
//        ArrayList<String> placeDetailId = new ArrayList<>();
//
//        //Cursor Object to get resultSet from Database
//        Cursor placeDataCursor = getContext().getContentResolver().query(placeContentUri,
//                null, null, null, null, null);
//
//        if (placeDataCursor.moveToFirst()) {
//            do {
//                String id = placeDataCursor.getString(placeDataCursor.getColumnIndex(
//                        PlaceDetailEntry.COLUMN_PLACE_ID));
//                placeDetailId.add(id);
//            } while (placeDataCursor.moveToNext());
//        } else
//            return false;
//
//        if (placeDetailId.size() != 0) {
//            for (int i = 0; i < placeDetailId.size(); i++) {
//                if (placeDetailId.get(i).equals(placeId))
//                    return true;
//            }
//        }
//        placeDataCursor.close();



        //mFirebaseDatabase1 = mFirebaseDatabase1.child(userid);
        //Query mQueryRef=mFirebaseDatabase1.child(userid);
        //mFirebaseDatabase2 = mFirebaseInstance.getReference("Favourites").child(userid);
        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("placeee", dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    assert place != null;
                 if (place.getPlaceId()!=null && placeId!=null)
                        if (place.getPlaceId().equals(placeId)) {
                            flag[0] =1;
                            Log.i("getanss","yeaddd");

                        }else
                            flag[0] =0;
                            //getans=false;

                }


            }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("ccancel","onCancelled");
        }
    });
        Log.i("getttans11",String.valueOf(flag[0]));


        return flag[0] == 1;
        //return getans;


    }

    private boolean call() {
        return true;
    }

}
