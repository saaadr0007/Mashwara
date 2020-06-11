package com.example.mashwara.Activities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mashwara.Models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FavoriteService extends Service {
    String movie=null;


    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void addinFirebase()
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("LikedMovies").push();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        String title=movie;
        Log.i("getdata","Yo");
        assert uid != null;
        databaseReference.child("LikedMovies").child(uid).child("movie").setValue(title);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
       Bundle b= intent.getExtras();
        assert b != null;
        movie=b.getString("movie");
       addinFirebase();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
       // onDestroy();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}