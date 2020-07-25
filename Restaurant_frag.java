package com.example.mashwara.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mashwara.Adapters.RestAdapter;
import com.example.mashwara.Models.GetDataService;
import com.example.mashwara.Models.RestModel;
import com.example.mashwara.Models.RetrofitClientInstance;
import com.example.mashwara.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Restaurant_frag extends Fragment {

    // Displays list of Locations based on user preferences
    ArrayList<RestModel> list;
    RestAdapter mRestAdapter;
    RestAdapter mListAdapter;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    Context context;
    private static Retrofit retrofit;
    private static final String BASE_URL = "";
    private Restaurant_frag fragment;
    final String CLIENT_ID = "ZWJHMOZRYFCD5YZF2IKH2GWSPHGMZMMCE3SE4BUGBPKISGW3";
    final String CLIENT_PASSWORD = "MAEQQMA4INB5ZEF2GA2TOCXF3A2PHP4AEANYUETGASHANRY1";

    // Coordinates of Miami Dade College Wolfson Campus (for Testing Purposes)
    final String Latitude = "31.482053";
    final String Longitude = "74.303524";
    View view;

    public RecyclerView recyclerview;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_restaurant_frag, container, false);
        this.view=view;
        context=container.getContext();
        // Display Results from User Preference
//        recyclerview =  view.findViewById(R.id.Rest);
//        mRestAdapter = new RestAdapter(view.getContext(),list);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
//        recyclerview.setLayoutManager(linearLayoutManager);
//        recyclerview.setAdapter( mRestAdapter);

        // Get Data Passed from Previous Fragment
//        Bundle bundle = getArguments();
//
//        if (bundle != null) {
//
//            // Receive message value from intent
//            String mealMessage = bundle.getString("meal");
//
//            // mealMessage serves as query for FetchData
//            //FetchData fetchData=new FetchData(this);
//
//            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
//            Call<List<RestModel>> call = service.getAllPhotos();
//            call.enqueue(new Callback<List<RestModel>>() {
//                @Override
//                public void onResponse(Call<List<RestModel>> call, Response<List<RestModel>> response) {
//                    //      progressDialog.dismiss();
//                    Log.e("res","gotresponse "+response.body());
//                    generateDataList(response.body());
//                }
//                @Override
//                public void onFailure(Call<List<RestModel>> call, Throwable t) {
//                    //    progressDialog.dismiss();
//                    Log.e("res","noresponse");
//                    //Toast.makeText(FetchData.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
        return view;
    }
private void generateDataList(List<RestModel> restModels) {
        recyclerView =(RecyclerView) this.view.findViewById(R.id.Rest);
        mListAdapter = new RestAdapter(context,restModels);
        Log.e("size",restModels.toString());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        }


        }