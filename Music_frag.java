package com.example.mashwara.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mashwara.Adapters.MusicAdapter;
import com.example.mashwara.Models.MusicModel;
import com.example.mashwara.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Music_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Music_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Music_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    com.example.mashwara.Adapters.MusicAdapter MusicAdapter;
    ArrayList<MusicModel> MusicModels;


    public static final String[] Musicnames= {"Breaking Bad","Rick and Morty", "FRIENDS","Sherlock","Stranger Things","Rick and Morty", "FRIENDS","Sherlock","Stranger Things"};
    public static final int[] Musicimages = {R.drawable.udemy,R.drawable.fastlogo,R.drawable.ieee,R.drawable.pucitlogo,R.drawable.nau,R.drawable.fastlogo,R.drawable.ieee,R.drawable.pucitlogo,R.drawable.nau};



    private OnFragmentInteractionListener mListener;

    public Music_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Music_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Music_frag newInstance(String param1, String param2) {
        Music_frag fragment = new Music_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentHolder;
        parentHolder = inflater.inflate(R.layout.fragment_music_frag, container, false);
        MusicModels=new ArrayList<>();

        for (int i = 0; i < Musicnames.length; i++) {
            MusicModel MusicModel = new MusicModel();

            MusicModel.setMusicName(Musicnames[i]);
            MusicModel.setMusicImg(Musicimages[i]);

            MusicModels.add(MusicModel);
        }


        MusicAdapter = new MusicAdapter(MusicModels);

        recyclerView = (RecyclerView)parentHolder.findViewById(R.id.Music);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(MusicAdapter);

        return parentHolder;
        
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}