package com.saad.example.nearbyservices.ui;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.retrofit.ShowInstance;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.adapter.HomeScreenItemListAdapter;
import com.saad.example.nearbyservices.utils.PlaceDetailProvider;

public class HomeScreenActivity extends AppCompatActivity {

    //View Reference Variable
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private RelativeLayout relativeLayout;
    private HomeScreenItemListAdapter mHomeScreenItemListAdapter;
    private String[] itemString;
    private DrawerLayout mDrawerLayout;
    SharedPreferences.Editor editor;

    private ActionBarDrawerToggle mToggle;



    private NavigationView mNavigationView;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        setTitle("All Services");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        //set the drawerListener
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.location_favourite_icon:
                        startActivity(new Intent(HomeScreenActivity.this, FavouritePlaceListActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.home:
                        startActivity(new Intent(HomeScreenActivity.this, LandingActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;

//                    case R.id.share_icon:
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.setType("text/plain");
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout AroundMe Application");
//                        startActivity(Intent.createChooser(shareIntent, "Share App.."));
//                        mDrawerLayout.closeDrawers();
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
                        startActivity(new Intent(HomeScreenActivity.this,PreferenceActivity.class));
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

        itemString = PlaceDetailProvider.popularPlaceTagName;
        mHomeScreenItemListAdapter = new HomeScreenItemListAdapter(this, itemString);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(36);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setAdapter(mHomeScreenItemListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu to add items to action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.removeItem(R.id.share_icon);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, PlaceSearchResultActivity.class)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


