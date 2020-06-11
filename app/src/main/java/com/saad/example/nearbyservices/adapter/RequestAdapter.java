package com.saad.example.nearbyservices.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saad.example.nearbyservices.R;
import com.saad.example.nearbyservices.ui.ProfileActivity;
import com.saad.example.nearbyservices.ui.usr_prof;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KSHITIZ on 3/29/2018.
 *
 * ------FOR REQUEST FRAGMENT------
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private List<String> requestList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;

    private Context ctx;

    public RequestAdapter(List<String> requestList) {
        this.requestList = requestList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_single_user,parent,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        return new RequestAdapter.RequestViewHolder(view);

    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName,disemail;
        public TextView displayStatus;
        public CircleImageView displayImage;
        public ImageView imageView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            ctx = itemView.getContext();

            displayName = (TextView)itemView.findViewById(R.id.textViewSingleListName);
            displayName = (TextView)itemView.findViewById(R.id.textViewSingleListemail);

            //displayStatus = (TextView) itemView.findViewById(R.id.textViewSingleListStatus);
            displayImage = (CircleImageView)itemView.findViewById(R.id.circleImageViewUserImage);
            //imageView = (ImageView)itemView.findViewById(R.id.userSingleOnlineIcon);
            //imageView.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(ctx, ProfileActivity.class);
                    intent.putExtra("user_id",requestList.get(pos));
                    ctx.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        String user_id = requestList.get(position);
        mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("username").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                holder.displayName.setText(userName);
                holder.displayName.setText(email);

                if (dataSnapshot.child("thumb_image").getValue()!=null) {
                    String userThumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                    // String userStatus =dataSnapshot.child("status").getValue().toString();

                    //holder.displayStatus.setText(userStatus);
                    Picasso.with(holder.displayImage.getContext()).load(userThumbImage).placeholder(R.drawable.man).into(holder.displayImage);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]{"Open Profile"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Select Options");
                        builder.setItems(options,new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Intent intent=new Intent(ctx, ProfileActivity.class);
                                    intent.putExtra("user_id",requestList.get(position));
                                    ctx.startActivity(intent);
                                }
                            }
                        });
                        builder.show();

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
