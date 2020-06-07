package com.saad.example.nearbyservices.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.clicklistener;
import com.saad.example.nearbyservices.listener_furn;
import com.saad.example.nearbyservices.ui.Arraylist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Team Mashwara on 4/6/2020.
 */
public class CustomAdapterFurniture  extends RecyclerView.Adapter<CustomAdapterFurniture.MyViewHolder> {

    private ArrayList<Arraylist> dataSet;
    private com.saad.example.nearbyservices.listener_furn clicklistener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        private WeakReference<listener_furn> listenerRef;

        ImageView imageViewIcon, imgheart;

        Animation rotate;


        public MyViewHolder(final View itemView, final listener_furn listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            this.textViewName = (TextView) itemView.findViewById(R.id.txtView);
            this.imgheart = (ImageView) itemView.findViewById(R.id.unheartimgfurn);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
                    listenerRef.get().onPositionClicked(imgheart, getAdapterPosition());
                }
            });
        }
    }

    public CustomAdapterFurniture(ArrayList<Arraylist> data, listener_furn listener) {
        this.dataSet = data;
        this.clicklistener = listener;
    }

    @Override
    public CustomAdapterFurniture.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_furn, parent, false);


        //view.setOnClickListener(MainActivity.myOnClickListener);

        CustomAdapterFurniture.MyViewHolder myViewHolder = new CustomAdapterFurniture.MyViewHolder(view, clicklistener);
        // imgheart=(ImageView) itemView.findViewById(R.id.unheart);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapterFurniture.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        // TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        //textViewVersion.setText(dataSet.get(listPosition).getVersion());
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
