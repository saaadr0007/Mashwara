package com.example.retrofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowInstance extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        progressDoalog = new ProgressDialog(ShowInstance.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Retrophoto>> call = service.getpersonalized("ChIJS29cpv0FGTkRU2RDEsHu2CI");
        call.enqueue(new Callback<List<Retrophoto>>() {
            @Override
            public void onResponse(Call<List<Retrophoto>> call, Response<List<Retrophoto>> response) {
                Log.d("ayakuch","yeah");
                progressDoalog.dismiss();
               // System.out.println(response);
                ArrayList<String> myList;
                myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Retrophoto>> call, Throwable t) {
                Log.d("niayakuch","yeah");

                progressDoalog.dismiss();
                Toast.makeText(ShowInstance.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Retrophoto> photoList) {
        recyclerView = findViewById(R.id.recy);
        System.out.println(photoList);
        adapter = new CustomAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowInstance.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
