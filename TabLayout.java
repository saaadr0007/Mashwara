package com.example.mashwara.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.mashwara.Adapters.TabsAdapter;
import com.example.mashwara.Fragments.Books_frag;
import com.example.mashwara.Fragments.Movies_frag;
import com.example.mashwara.Fragments.Music_frag;
import com.example.mashwara.Fragments.Restaurant_frag;
import com.example.mashwara.R;
import com.example.mashwara.ui.gallery.GalleryFragment;
import com.example.mashwara.ui.home.HomeFragment;
import com.example.mashwara.ui.send.SendFragment;
import com.google.android.material.navigation.NavigationView;


public class TabLayout extends AppCompatActivity{
    private AppBarConfiguration mAppBarConfiguration;
    private OnClickListener onClick;


static final String name = "Saad Rafique";
    private LinearLayout myprofile;
    public void initialise_tabs() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().hide();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        com.google.android.material.tabs.TabLayout tabLayout;
        tabLayout = (com.google.android.material.tabs.TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.music_tab).setIcon(R.drawable.music));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.movies_tab).setIcon(R.drawable.video));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Restaurants).setIcon(R.drawable.dish));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Books).setIcon(R.drawable.book));

        tabLayout.setTabGravity(com.google.android.material.tabs.TabLayout.GRAVITY_FILL);


//attaching Viewpager with the adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);

        viewPager.addOnPageChangeListener(new com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {

            }


        });
    }
    public void listener(View v) {
        Intent i = new Intent(TabLayout.this, editAccount.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent=new Intent(this,login.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        //return true;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);


        initialise_tabs();
        myprofile = (LinearLayout) findViewById(R.id.myprofile);



//        myprofile.setOnClickListener(listener);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                  R.id.nav_friends,R.id.nav_circle,R.id.nav_collection
                 ,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
     //  setupDrawerContent(navigationView);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = SendFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_circle:

                Intent i = new Intent(TabLayout.this, TabLayout.class);
                startActivity(i);
//                setTitle(menuItem.getTitle());

                //fragmentClass = GalleryFragment.class;
                break;
            case R.id.nav_friends:
                Intent intent = new Intent(TabLayout.this, FindFriend.class);
                startActivity(intent);
                //setTitle(menuItem.getTitle());

                break;
            case R.id.nav_collection:
                Intent intent1 = new Intent(TabLayout.this, TabLayout.class);
                startActivity(intent1);

                //setTitle(menuItem.getTitle());

                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
//                break;
//            default:
//                fragmentClass = FirstFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        //drawer.closeDrawers();
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

//    @Override
//    public boolean onNavigationItemSelected( MenuItem item) {
//        int id = item.getItemId();
//        Fragment fragment = null;
//        Class fragmentClass = null;
//        if (id == R.id.nav_friends) {
//            Intent intent = new Intent(TabLayout.this, FindFriend.class);
//            startActivity(intent);
////
//        } else if (id == R.id.nav_circle) {
//            Intent i = new Intent(TabLayout.this, TabLayout.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_collection) {
//            Intent i = new Intent(TabLayout.this, FindFriend.class);
//            startActivity(i);
//        } else if (id == R.id.nav_logout) {
//            Intent i = new Intent(TabLayout.this, login.class);
//            startActivity(i);
//        }
////            try {
////                fragment = (Fragment) fragmentClass.newInstance();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//        //   FragmentManager fragmentManager = getSupportFragmentManager();
//        // fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//
//
//    }

    public void onClickBtn(View view) {
        Intent i = new Intent(TabLayout.this, editAccount.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
//abi adapter class banani hai....