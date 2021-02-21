package com.example.mashwara.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mashwara.Adapters.MovieRecyclerViewAdapter;
import com.example.mashwara.Models.Movie;
import com.example.mashwara.R;
import com.example.mashwara.data.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class SearchResultsActivity extends AppCompatActivity {

    private String searchQuery;
    private TextView tvErrorMessage;
    private TextView tvResultsTitle;
    private ProgressBar pbLoadingIndicator;
    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    return true;
                case R.id.navigation_search:
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                case R.id.navigation_favorites:
                    Intent favoritesIntent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    startActivity(favoritesIntent);
                    return true;
            }
            return false;
        }
    };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar;
        toolbar=(Toolbar) findViewById(R.id.toolbar_);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_trending);


        // Bind components
        tvErrorMessage = findViewById(R.id.tv_error_message);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        tvResultsTitle = findViewById(R.id.tv_results_title);

        rvMovieList = findViewById(R.id.rv_movie_list);
        rvMovieList.setNestedScrollingEnabled(false);

        // Get received data from previous intent
        Intent intent = getIntent();
        searchQuery = intent.getExtras().getString("SearchQuery");

        // Start search query
        URL TMDBSearchURL = NetworkUtils.buildSearchUrl(searchQuery);
        new SearchResultsActivity.TMDBQueryTask().execute(TMDBSearchURL);
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TMDBSearchResults = null;
            try {
                TMDBSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TMDBSearchResults;
        }

        /**
         * Executes when the API call is finished.
         */
        @Override
        protected void onPostExecute(String s) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                showRecyclerView();
                try {
                    parseMovies(s);
                    populateRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * Parses the movie JSON string and inserts the results as a new Movie object into the Movie array.
     *
     * @param moviesJSONString
     * The JSON string containing the results.
     */
    private void parseMovies(String moviesJSONString) throws JSONException {
        JSONObject resultJSONObject = new JSONObject(moviesJSONString);
        JSONArray moviesJSONArray = resultJSONObject.getJSONArray("results");
        movies = new ArrayList<Movie>();

        // Loop through json array
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            JSONObject movieJSONObject = new JSONObject(moviesJSONArray.get(i).toString());
            if (!movieJSONObject.isNull("poster_path")) {
                int movieId = movieJSONObject.getInt("id");
                String posterPath = movieJSONObject.getString("poster_path");

                // Add movie object
                movies.add(new Movie(movieId, posterPath));
            }
        }

        // Set result count
        tvResultsTitle.setText(movies.size() + " " +  getResources().getString(R.string.results_heading) + " '" + searchQuery + "'");
    }

    /**
     * Populates the recyclerview with the retrieved movies.
     */
    private void populateRecyclerView() {
        MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(getApplicationContext(), movies);
        rvMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvMovieList.setAdapter(rvAdapter);
    }

    /**
     * Show recycler view.
     */
    private void showRecyclerView() {
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    /**
     * Show error message.
     */
    private void showErrorMessage() {
        rvMovieList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
}
