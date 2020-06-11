package com.saad.example.nearbyservices.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.clicklistener;
import com.saad.example.nearbyservices.ui.Arraylist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Team Mashwara on 3/18/2020.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<Arraylist> dataSet;
    private clicklistener clicklistener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        private WeakReference<clicklistener> listenerRef;

        ImageView imageViewIcon, imgheart;

        Animation rotate;


        public MyViewHolder(final View itemView, final clicklistener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            this.textViewName = (TextView) itemView.findViewById(R.id.txtView);
            this.imgheart = (ImageView) itemView.findViewById(R.id.unheartimg);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                    listenerRef.get().onPositionClicked(imgheart,getAdapterPosition());
                }
            });
        }
    }

    public CustomAdapter(ArrayList<Arraylist> data,clicklistener listener) {
        this.dataSet = data;
        this.clicklistener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_res, parent, false);


        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view,clicklistener);
        // imgheart=(ImageView) itemView.findViewById(R.id.unheart);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        // TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        imageView.setImageResource(dataSet.get(listPosition).getImage());
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}