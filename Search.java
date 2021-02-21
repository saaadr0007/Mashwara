package com.example.mashwara.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.mashwara.Adapters.SearchAdapter;
import com.example.mashwara.R;
import com.example.mashwara.Models.SearchItem;

import java.util.List;

    public class Search extends AppCompatActivity {
        private com.example.mashwara.Adapters.SearchAdapter adapter;
        private List<SearchItem> searchList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search);
       //     fillExampleList();
            setUpRecyclerView();
        }


        private void setUpRecyclerView() {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new SearchAdapter(searchList);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.search_menu, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
            return true;
        }
    }

