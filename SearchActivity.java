package com.example.mashwara.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mashwara.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class SearchActivity extends AppCompatActivity {

    private EditText etSearchBox;
    private Button btnSearchMovie;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    Intent trendingIntent = new Intent(getApplicationContext(), TrendingActivity.class);
                    startActivity(trendingIntent);
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_favorites:
                    Intent favoritesIntent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    startActivity(favoritesIntent);
                    return true;
            }
            return false;
        }
    };

    /**
     * Submits a search and starts a search result activity.
     */
    private void submitSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
        String TMDBQuery = etSearchBox.getText().toString();

        // Passing data to the Search Results activity
        intent.putExtra("SearchQuery", TMDBQuery);
        getApplicationContext().startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar;
        toolbar=(Toolbar) findViewById(R.id.toolbar_);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set bottombar active item
        navigation.setSelectedItemId(R.id.navigation_search);

        etSearchBox = findViewById(R.id.et_search_box);
        btnSearchMovie = findViewById(R.id.btn_search_movie);

        // Set on click listener on button
        btnSearchMovie.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submitSearch();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
