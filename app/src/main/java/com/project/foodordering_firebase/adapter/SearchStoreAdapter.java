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
import com.google.android.material.imageview.ShapeableImageView;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Store;
import com.project.foodordering_firebase.user.ViewStoreActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchStoreAdapter extends RecyclerView.Adapter<SearchStoreAdapter.myViewHolder>{
    Context context;
    List<Store> storeList;
    String mUserName;

    public SearchStoreAdapter(Context context, List<Store> storeList, String mUserName) {
        this.context = context;
        this.storeList = storeList;
        this.mUserName = mUserName;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_store_user,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Store model = storeList.get(position);
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
        //Glide.with(context).load(model.getStoreImg()).into(holder.sImg);


        holder.viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewStoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("storeName", holder.sName.getText().toString());
                intent.putExtra("storeCate", holder.sCate.getText().toString());
                intent.putExtra("address", holder.sAddress.getText().toString());
                intent.putExtra("storeImg", model.getStoreImg());
                intent.putExtra("userName",mUserName);
                context.startActivity(intent);
/*
               Intent i = new Intent(context, ViewStoreActivity.class);
               i.putExtra("storeName",storeList.get(holder.getAdapterPosition()).getStoreName());
               i.putExtra("storeCate",storeList.get(holder.getAdapterPosition()).getStoreCate());
               i.putExtra("address",storeList.get(holder.getAdapterPosition()).getAddress());
               i.putExtra("storeImg",storeList.get(holder.getAdapterPosition()).getStoreImg());
               i.putExtra("userName",mUserName);
               context.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
    public void searchDataList(ArrayList<Store> searchList) {
        storeList = searchList;
        notifyDataSetChanged();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView sName, sAddress, sCate;
        ImageView viewMenu;
        ShapeableImageView sImg;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            sName = itemView.findViewById(R.id.tvStoreName);
            sAddress = itemView.findViewById(R.id.tvAddress);
            sCate = itemView.findViewById(R.id.tvStoreCate);
            sImg = itemView.findViewById(R.id.storeImg);
            viewMenu = itemView.findViewById(R.id.btnViewMenu);
        }
    }
}


