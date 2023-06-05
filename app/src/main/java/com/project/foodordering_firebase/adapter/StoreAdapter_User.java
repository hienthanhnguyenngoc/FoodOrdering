package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.admin.MenuActivity;
import com.project.foodordering_firebase.model.Store;
import com.project.foodordering_firebase.user.ViewStoreActivity;

public class StoreAdapter_User extends FirebaseRecyclerAdapter<Store,StoreAdapter_User.myViewHolder> {

    Context mContext;
    String mUserName;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StoreAdapter_User(Context context, @NonNull FirebaseRecyclerOptions<Store> options, String username) {
        super(options);
        mContext = context;
        mUserName = username;
    }

    @Override
    protected void onBindViewHolder(@NonNull StoreAdapter_User.myViewHolder holder, int position, @NonNull Store model) {
        holder.sCate.setText(model.getStoreCate());
        holder.sName.setText(model.getStoreName());
        holder.sAddress.setText(model.getAddress());
        if (holder.sImg != null) {
            // Do something with the ImageView
            Glide.with(holder.sImg.getContext()).load(model.getStoreImg()).into(holder.sImg);
        } else {
            // ImageView is null, do something else
            // For example, set a default image
            holder.sImg.setImageResource(R.drawable.ic_launcher_background);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewStoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("storeName", holder.sName.getText().toString());
                intent.putExtra("storeCate", holder.sCate.getText().toString());
                intent.putExtra("address", holder.sAddress.getText().toString());
                intent.putExtra("storeImg", model.getStoreImg());
                intent.putExtra("userName",mUserName);
                mContext.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public StoreAdapter_User.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_user,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView sName, sAddress, sCate;
        ImageView viewMenu;
        ShapeableImageView sImg;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.tvStoreName);
            sAddress = itemView.findViewById(R.id.tvAddress);
            sImg = itemView.findViewById(R.id.storeImg);
            recyclerView = itemView.findViewById(R.id.rcvStore);
            sCate = itemView.findViewById(R.id.tvStoreCate);
            viewMenu = itemView.findViewById(R.id.btnViewMenu);
        }
    }
}
