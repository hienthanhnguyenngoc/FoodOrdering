package com.project.foodordering_firebase.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.FavStore;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.user.ViewStoreActivity;

public class FavStoreAdapter extends FirebaseRecyclerAdapter<FavStore,FavStoreAdapter.myViewHolder> {
    Context context;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavStoreAdapter(@NonNull FirebaseRecyclerOptions<FavStore> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull FavStore model) {
        holder.storeName.setText(model.getStoreName());
        Glide.with(holder.storeImg.getContext()).load(model.getStoreImg()).into(holder.storeImg);


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference storesRef = FirebaseDatabase.getInstance().getReference("FavStore");
//                storesRef.orderByChild("storeName").equalTo(model.getStoreName()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            // If the store with the given name exists in the database, get its address
//                            DataSnapshot storeSnapshot = snapshot.getChildren().iterator().next();
//                            String address = storeSnapshot.child("address").getValue(String.class);
//                            Intent i = new Intent(context, ViewStoreActivity.class);
//                            i.putExtra("storeName", model.getStoreName());
//                            i.putExtra("storeImg", model.getStoreImg());
//                            i.putExtra("address", address);
//                            context.startActivity(i);
//                        } else {
//                            Toast.makeText(context, "Error 404!!!", Toast.LENGTH_SHORT).show();
//                            // Handle the case where the store with the given name does not exist in the database
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Handle the error case
//                    }
//                });
//            }
//        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_store,parent,false);
        return new FavStoreAdapter.myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView storeImg;
        TextView storeName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImg = itemView.findViewById(R.id.storeImg);
            storeName = itemView.findViewById(R.id.storeName);
        }
    }
}
