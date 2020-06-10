package com.saad.example.nearbyservices.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.CustomAdapter;
import com.saad.example.nearbyservices.adapter.CustomAdapterFurniture;
import com.saad.example.nearbyservices.clicklistener;
import com.saad.example.nearbyservices.listener_furn;
import com.saad.example.nearbyservices.model.Place;
import com.saad.example.nearbyservices.model.Preference;

import java.util.ArrayList;
import java.util.List;

import static com.saad.example.nearbyservices.ui.MyData.nameArray;
import static com.saad.example.nearbyservices.ui.MyData.nameArray1;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> preferencelist = new ArrayList<>();

    private RecyclerView horizontal_recycler_view, horizontal_recycler_view_furn;
    private ArrayList<Arraylist> horizontalList, horizontalListfurn ;
    private CustomAdapter horizontalAdapter;
    private CustomAdapterFurniture horizontalAdapterfurn;
    private View includeedu, includerest;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private TextView junk, chinese, mexican, desi, conti, italian;
    private ImageView imgheart;
    private Animation rotate;
    private DatabaseReference mFirebaseDatabase1,getmFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private String userid=null;
    private FirebaseUser firebaseUser=null;
    private boolean removepref=false;
    private int pos;
    private  Preference preferen=null;
    private ImageView getImgheart;
    public void navigation() {
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(CategoryActivity.this, LandingActivity.class));
                        break;


                    case R.id.location_favourite_icon:
                        startActivity(new Intent(CategoryActivity.this, FavouritePlaceListActivity.class));
                        break;

//                    case R.id.share_icon:
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.setType("text/plain");
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout AroundMe Application");
//                        startActivity(Intent.createChooser(shareIntent, "Share App.."));
//                        break;

//                    case R.id.feedback_icon:
//                        Intent mailToIntent = new Intent(Intent.ACTION_SEND);
//                        mailToIntent.setData(Uri.parse("mailto:"));
//                        mailToIntent.setType("text/plain");
//                        mailToIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"saadrafique1000@gmail.com"});
//                        startActivity(Intent.createChooser(mailToIntent, "Send Mail.."));
//                        mDrawerLayout.closeDrawers();
//                        break;

                    case R.id.pref_icon:
                        startActivity(new Intent(CategoryActivity.this, PreferenceActivity.class));
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


    public void initialisespinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Restaurant");
        categories.add("Furniture Stores");
//        categories.add("Banks");
//        categories.add("Education");
//        categories.add("ATM");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initialisespinner();
        navigation();
        includeedu = (View) findViewById(R.id.include2);
        includerest = (View) findViewById(R.id.include1);
        imgheart = (ImageView) findViewById(R.id.unheartimg);
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("Preferences");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid= firebaseUser.getUid();

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle(getString(R.string.preference_place_list_string));
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontalList = new ArrayList<Arraylist>();
        horizontalListfurn = new ArrayList<Arraylist>();



        for (int i = 0; i < nameArray.length; i++) {
            horizontalList.add(new Arraylist(
                    nameArray[i],
                    MyData.drawableArray[i]
            ));
        }


        horizontalAdapter = new CustomAdapter(horizontalList, new clicklistener() {
            @Override
            public void onPositionClicked(ImageView imgheart, final int position) {
                if (imgheart.getDrawable().getConstantState().equals(
                        ContextCompat.getDrawable(getBaseContext(),
                                R.drawable.unheart).getConstantState())) {
                    Log.i("msg2", "infif");
                    Toast.makeText(getBaseContext(),  " Preference Added", Toast.LENGTH_SHORT).show();
                    rotate = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.rotate);
                     preferen=new Preference(nameArray[position]);
                    mFirebaseDatabase1.child(userid).push().setValue(preferen);
                   imgheart.setImageResource(R.drawable.fillheart);
                    imgheart.startAnimation(rotate);
                }
                else{
                    Log.i("msg2", "insif");

                    mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("placeee", dataSnapshot.getKey());
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Preference preference = snapshot.getValue(Preference.class);
                                assert preference != null;
                                Log.i("prefss",preference.getPref_name());
                                Log.i("prefspos",nameArray[position]);
                                if (preference.getPref_name().equals(nameArray[position])) {
                                    snapshot.getRef().removeValue();
                                 //   break;
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });

                    Toast.makeText(getBaseContext(), "Preference Removed", Toast.LENGTH_SHORT).show();
                    imgheart.setImageResource(R.drawable.unheart);



                }
            }
        });

        horizontal_recycler_view.addOnScrollListener ( new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pos= ((LinearLayoutManager)horizontal_recycler_view.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    Log.e("position", String.valueOf(pos));

                    mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Log.i("placeee", dataSnapshot.getKey());
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Preference preference = snapshot.getValue(Preference.class);
                                assert preference != null;
                                if(preference.getPref_name()!=null){
                                    if (preference.getPref_name().equals(nameArray[pos])) {
                                        Log.i("nnn",nameArray[pos]);// imgheart.setImageResource(R.drawable.fillheart);
                                        ((ImageView)findViewById(R.id.unheartimg)).
                                                setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.fillheart));
                                    }
                                }
                                else
                                {
                                    Log.i("inside111","inside111");
                                    ((ImageView)findViewById(R.id.unheartimg)).
                                            setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.unheart));
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ccancel","onCancelled");
                        }
                    });





                }
            }
        });
        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.i("placeee", dataSnapshot.getKey());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Preference preference = snapshot.getValue(Preference.class);
                    assert preference != null;
                    if(preference.getPref_name()!=null){
                        if (preference.getPref_name().equals(nameArray[pos])) {
                            Log.i("nnn1111",nameArray[pos]);// imgheart.setImageResource(R.drawable.fillheart);
                            ((ImageView)findViewById(R.id.unheartimg)).
                                    setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.fillheart));
                        }
                    }
                    else
                    {
                        Log.i("inside111","inside111");
                        ((ImageView)findViewById(R.id.unheartimg)).
                                setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.unheart));
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ccancel","onCancelled");
            }
        });


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CategoryActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);



        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        if (item.equals("Furniture Stores")) {
          //  startActivity(new Intent(this,CategoryActivity.class));
            includerest.setVisibility(View.INVISIBLE);
            includeedu.setVisibility(View.VISIBLE);

            for (int i = 0; i < nameArray1.length; i++) {
                horizontalListfurn.add(new Arraylist(
                        nameArray1[i],
                        MyData.drawableArray1[i]
                ));
            }

            horizontalAdapterfurn = new CustomAdapterFurniture(horizontalListfurn, new listener_furn() {
                @Override
                public void onPositionClicked(ImageView imgheart, final int position) {
                    if (imgheart.getDrawable().getConstantState().equals(
                            ContextCompat.getDrawable(getBaseContext(),
                                    R.drawable.unheart).getConstantState())) {
                        Log.i("msg2", "infif");
                        Toast.makeText(getBaseContext(),  " Preference Added", Toast.LENGTH_SHORT).show();
                        rotate = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.rotate);
                        preferen=new Preference(nameArray1[position]);
                        mFirebaseDatabase1.child(userid).push().setValue(preferen);
                        imgheart.setImageResource(R.drawable.fillheart);
                        imgheart.startAnimation(rotate);
                    }
                    else{
                        Log.i("msg2", "insif");

                        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i("placeee", dataSnapshot.getKey());
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Preference preference = snapshot.getValue(Preference.class);
                                    assert preference != null;
                                    Log.i("prefss",preference.getPref_name());
                                    Log.i("prefspos",nameArray1[position]);
                                    if (preference.getPref_name().equals(nameArray1[position])) {
                                        snapshot.getRef().removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

                        Toast.makeText(getBaseContext(), "Preference Removed", Toast.LENGTH_SHORT).show();
                        imgheart.setImageResource(R.drawable.unheart);
                    }
                }
            });

            horizontal_recycler_view_furn = (RecyclerView) findViewById(R.id.horizontal_recycler_view_furniture);

            horizontal_recycler_view_furn.addOnScrollListener ( new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //Dragging
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        pos= ((LinearLayoutManager)horizontal_recycler_view_furn.getLayoutManager())
                                .findFirstVisibleItemPosition();
                        Log.e("position", String.valueOf(pos));

                        mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Log.i("placeee", dataSnapshot.getKey());
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Preference preference = snapshot.getValue(Preference.class);
                                    assert preference != null;
                                    if(preference.getPref_name()!=null) {
                                        if (preference.getPref_name().equals(nameArray1[pos])) {
                                            Log.i("nnnfurn", nameArray1[pos]);// imgheart.setImageResource(R.drawable.fillheart);
                                      //      Toast.makeText(getBaseContext(),nameArray1[pos] + " Already Added",Toast.LENGTH_LONG).show();
//
                                            ((ImageView)findViewById(R.id.unheartimgfurn)).
                                                    setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.fillheart));
                                        }
                                    }
                                    else
                                    {
                                        Log.i("inside111","inside111");
                                        ((ImageView)findViewById(R.id.unheartimgfurn)).
                                                setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.unheart));
//                                    getImgheart=(ImageView)findViewById(R.id.unheart);
//                                    getImgheart
//                                            .setImageDrawable(ContextCompat.getDrawable(CategoryActivity.this, R.drawable.unheart));
                                    }

                                }






                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("ccancel","onCancelled");
                            }
                        });
                    }
                }
            });
            mFirebaseDatabase1.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Log.i("placeee", dataSnapshot.getKey());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Preference preference = snapshot.getValue(Preference.class);
                        assert preference != null;
                        if(preference.getPref_name()!=null){
                            if (preference.getPref_name().equals(nameArray1[pos])) {
                                Log.i("nnn",nameArray1[pos]);// imgheart.setImageResource(R.drawable.fillheart);
                                ((ImageView)findViewById(R.id.unheartimgfurn)).
                                        setImageDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.fillheart));

                            }
                        }
                        else
                        {
                            Log.i("inside111","inside111");
                            ((ImageView)findViewById(R.id.unheartimgfurn)).
                                    setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.unheart));

                        }

                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ccancel","onCancelled");
                }
            });




            LinearLayoutManager horizontalLayoutManagaerFurn
                    = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
            horizontal_recycler_view_furn.setLayoutManager(horizontalLayoutManagaerFurn);
            horizontal_recycler_view_furn.setAdapter(horizontalAdapterfurn);

           // horizontalAdapterfurn.notifyDataSetChanged();

        } else if (item.equals("Restaurant")) {
            includeedu.setVisibility(View.INVISIBLE);
            includerest.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    public void CategoryPreferences(View view) {
//        Intent i = new Intent(this, HomeScreenActivity.class);
//        startActivity(i);
//    }

//    public void getjunk(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        junk.setTypeface(junk.getTypeface(), Typeface.BOLD);
//
//        preferencelist.add("junk");
//    }
//
//    public void getChinese(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        preferencelist.add("chinese");
//        chinese.setTypeface(chinese.getTypeface(), Typeface.BOLD);
//
//
//    }
//
//    public void getitalian(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        preferencelist.add("Italian");
//        italian.setTypeface(desi.getTypeface(), Typeface.BOLD);
//
//
//    }
//
//    public void getdesi(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        preferencelist.add("desi");
//        desi.setTypeface(desi.getTypeface(), Typeface.BOLD);
//
//
//    }
//
//    public void getMexican(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        preferencelist.add("mexican");
//        mexican.setTypeface(mexican.getTypeface(), Typeface.BOLD);
//
//    }
//
//    public void getContinental(View view) {
//        Toast.makeText(this, "Preference Added", Toast.LENGTH_SHORT).show();
//        preferencelist.add("continental");
//        conti.setTypeface(conti.getTypeface(), Typeface.BOLD);
//    }

//    public void preferenceset(View view) {
//        ImageView imgheart=(ImageView)view.findViewById(R.id.unheart);
//        Animation rotate;
//
//        if (imgheart.getDrawable().getConstantState().equals(
//                ContextCompat.getDrawable(getApplicationContext(),
//                        R.drawable.fillheart).getConstantState())) {
//            Log.i("msg2","infif");
//
//            Toast.makeText(getApplicationContext(), "Preference Removed", Toast.LENGTH_LONG).show();
//            imgheart.setImageResource(R.drawable.unheart);
//        } else if(imgheart.getDrawable().getConstantState().equals(
//                ContextCompat.getDrawable(getApplicationContext(),
//                        R.drawable.unheart).getConstantState()))
//        {
//            Log.i("msg2","insif");
//            //Toast.makeText(v.getContext(), textViewName.toString() + " Marked Favourite", Toast.LENGTH_SHORT).show();
//            //imgheart=(ImageView)itemView.findViewById(R.id.img_heart);
//            rotate = AnimationUtils.loadAnimation(getApplicationContext(),
//                    R.anim.rotate);
//            imgheart.setImageResource(R.drawable.fillheart);
//            //  imgheart.setVisibility(View.VISIBLE);
//            imgheart.startAnimation(rotate);
//        }

}
