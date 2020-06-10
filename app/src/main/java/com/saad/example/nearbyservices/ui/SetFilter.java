package com.saad.example.nearbyservices.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.model.Filter;
import com.saad.example.nearbyservices.model.Preference;

import java.util.ArrayList;
import java.util.Set;

import in.ishankhanna.materialcheckboxview.MaterialCheckBox;

public class SetFilter extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseDatabase mFirebaseInstance;
    String uid=null;
    public ArrayList<String> filters=new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filter);
        database = FirebaseDatabase.getInstance();
        mFirebaseInstance=FirebaseDatabase.getInstance();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor= pref.edit();

        MaterialCheckBox ageCheckBox = (MaterialCheckBox) findViewById(R.id.age_checkbox);
        MaterialCheckBox friendsCheckBox = (MaterialCheckBox) findViewById(R.id.friends_checkbox);
        MaterialCheckBox favplaces_checkbox = (MaterialCheckBox) findViewById(R.id.favplaces_checkbox);
        MaterialCheckBox pref_checkbox = (MaterialCheckBox) findViewById(R.id.pref_checkbox);
        MaterialCheckBox search_checkbox = (MaterialCheckBox) findViewById(R.id.search_checkbox);
//
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//             uid = user.getUid();
//        }
//        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Log.i("placeee", dataSnapshot.getKey());
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Filter filter = snapshot.getValue(Filter.class);
//                    assert preference != null;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        ageCheckBox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked) {
                    filters.add("age");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        if (filters.get(i).equals("age")) {
                            filters.remove(i);
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        search_checkbox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked) {
                    filters.add("search");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        if (filters.get(i).equals("search")) {
                            filters.remove(i);
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        friendsCheckBox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked) {
                    filters.add("friends");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        if (filters.get(i).equals("friends")) {
                            filters.remove(i);
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        favplaces_checkbox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked) {
                    filters.add("favourites");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        if (filters.get(i).equals("favourites")) {
                            filters.remove(i);
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        pref_checkbox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked) {
                    filters.add("prefs");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        if (filters.get(i).equals("prefs")) {
                            filters.remove(i);
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void savefilters(View view) {
        Log.v("sizzzelist",String.valueOf(filters.size()));
        if (!filters.isEmpty()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                databaseReference = database.getReference().child("FiltersSet").child(uid);
                for (int i = 0; i < filters.size(); i++){
                    Filter f=new Filter(filters.get(i));
                databaseReference.push().setValue(f);
            }}
            String[] filterarr = new String[filters.size()];
            filterarr = filters.toArray(filterarr);
            //SharedPreferences pref = getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < filters.size(); i++) {
                sb.append(filterarr[i]).append(",");
            }
            editor.putString("filter", sb.toString());
            editor.commit();
            Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();

        }
startActivity(new Intent(SetFilter.this,MainActivityReq.class));

    }
}
