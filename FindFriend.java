package com.example.mashwara.Activities;
//package com.example.mashwara.Activities;

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

import java.util.ArrayList;
import java.util.List;

public class FindFriend extends AppCompatActivity {
    private List<SearchItem> searchList;
    private List<SearchItem> searchListFull;

    private com.example.mashwara.Adapters.SearchAdapter adapter;
    String[] Username ={"Saad","Tayyaba","Nabeel","Mahnoor","Hamna","Nabeel","Mahnoor","Hamna"};
    String[] followbutton={"Follow","Unfollow","Follow","Unfollow","Follow","Follow","Unfollow","Follow"};
    int[] imgs={R.drawable.saad,R.drawable.person,R.drawable.twitter,R.drawable.ic_search,R.drawable.ic_search,R.drawable.person,R.drawable.twitter,R.drawable.ic_search};
    ArrayList<SearchItem> searchItems;

    private void fillExampleList() {
        searchList = new ArrayList<>();
        searchList.add(new SearchItem(R.drawable.saad, "Saad","Unollow"));
        searchList.add(new SearchItem(R.drawable.person, "Tayyaba","Unfollow"));
        searchList.add(new SearchItem(R.drawable.twitter, "Nabeel","Follow"));
        searchList.add(new SearchItem(R.drawable.ic_search, "Hamna","Unfollow"));
        searchList.add(new SearchItem(R.drawable.ic_search, "Munib","Follow"));
        searchList.add(new SearchItem(R.drawable.ic_search, "Heem","Follow"));
        searchList.add(new SearchItem(R.drawable.ic_search, "Heem","Follow"));
        searchList.add(new SearchItem(R.drawable.ic_search, "Heem","Follow"));
//        searchList.add(new SearchItem(R.drawable.ic_search, "Seven"));
//        searchList.add(new SearchItem(R.drawable.ic_search, "Eight"));
//        searchList.add(new SearchItem(R.drawable.ic_search, "Nine"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillExampleList();
        setContentView(R.layout.activity_find_friend);
        getSupportActionBar();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Find Friends");

        searchItems = new ArrayList<>();

        for (int i = 0; i < Username.length; i++) {
            SearchItem searchItem = new SearchItem();

            searchItem.setText1(Username[i]);
            searchItem.setFollowbutton(followbutton[i]);
            searchItem.setImageResource(imgs[i]);

            searchItems.add(searchItem);
            RecyclerView recyclerView = findViewById(R.id.recyclerView1);
            //recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new SearchAdapter(searchItems);


            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
