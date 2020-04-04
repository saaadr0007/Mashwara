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
            this.imgheart = (ImageView) itemView.findViewById(R.id.unheart);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                    listenerRef.get().onPositionClicked(imgheart,getAdapterPosition());


                }
            });

//
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
        //textViewVersion.setText(dataSet.get(listPosition).getVersion());
        imageView.setImageResource(dataSet.get(listPosition).getImage());


//        imgheart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (imgheart.getDrawable().getConstantState().equals(
//                        ContextCompat.getDrawable(getApplicationContext(),
//                                R.drawable.fillheart).getConstantState()))
//                {
//                    Toast.makeText(getApplicationContext(),"Preference Removed",Toast.LENGTH_LONG).show();
//                    imgheart.setImageResource(R.drawable.unheart);
//                }
//                else if(imgheart.getDrawable().getConstantState().equals(
//                        ContextCompat.getDrawable(getApplicationContext(),
//                                R.drawable.unheart).getConstantState())){
//                    Toast.makeText(v.getContext(), textViewName+" Marked Preference", Toast.LENGTH_SHORT).show();
//                    //imgheart=(ImageView)itemView.findViewById(R.id.img_heart);
//                    rotate = AnimationUtils.loadAnimation(getApplicationContext(),
//                            R.anim.rotate);
//                    imgheart.setImageResource(R.drawable.fillheart);
//                    //  imgheart.setVisibility(View.VISIBLE);
//                    imgheart.startAnimation(rotate);
//                }
//            }
//        });
    }@Override
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

//    public void preferenceset(View view) {
//        ImageView imgheart = (ImageView) view.findViewById(R.id.unheart);
//        Animation rotate;
//
//        if (imgheart.getDrawable().getConstantState().equals(
//                ContextCompat.getDrawable(getApplicationContext(),
//                        R.drawable.fillheart).getConstantState())) {
//            Log.i("msg2", "infif");
//
//            Toast.makeText(getApplicationContext(), "Preference Removed", Toast.LENGTH_LONG).show();
//            imgheart.setImageResource(R.drawable.unheart);
//        } else if (imgheart.getDrawable().getConstantState().equals(
//                ContextCompat.getDrawable(getApplicationContext(),
//                        R.drawable.unheart).getConstantState())) {
//            Log.i("msg2", "insif");
//            //Toast.makeText(v.getContext(), textViewName.toString() + " Marked Favourite", Toast.LENGTH_SHORT).show();
//            //imgheart=(ImageView)itemView.findViewById(R.id.img_heart);
//            rotate = AnimationUtils.loadAnimation(getApplicationContext(),
//                    R.anim.rotate);
//            imgheart.setImageResource(R.drawable.fillheart);
//            //  imgheart.setVisibility(View.VISIBLE);
//            imgheart.startAnimation(rotate);
//        }
//
//    }
}