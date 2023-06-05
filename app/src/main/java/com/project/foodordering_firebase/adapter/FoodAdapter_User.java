package com.project.foodordering_firebase.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Cart;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;
import com.project.foodordering_firebase.user.CartActivity;
import com.project.foodordering_firebase.user.ViewStoreActivity;

public class FoodAdapter_User extends FirebaseRecyclerAdapter<Food,FoodAdapter_User.myViewHolder> {

    Context mContext;
    String mUserName;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FoodAdapter_User(@NonNull FirebaseRecyclerOptions<Food> options, Context context, String username) {
        super(options);
        mContext = context;
        mUserName = username;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Food model) {
        holder.fName.setText(model.getFoodName());
        holder.fDes.setText(model.getFoodDes());
        holder.fPrice.setText(String.valueOf(model.getPrice()));
        if (holder.foodImg != null) {
            // Do something with the ImageView
            Glide.with(holder.foodImg.getContext()).load(model.getFoodImg()).into(holder.foodImg);
        } else {
            // ImageView is null, do something else
            // For example, set a default image
            holder.foodImg.setImageResource(R.drawable.ic_launcher_background);
        }


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Neu add 1 foodID 2 lan
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Food");
                Query query1 = reference.orderByChild("foodID").equalTo(model.getFoodID());

                final boolean[] foodExists = {false};

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final boolean[] foodExists = {false};
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String foodID = childSnapshot.getKey();

                            // Check if the food item already exists in the cart
                            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart")
                                    .child(mUserName)
                                    .child(foodID);
                            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // If the food item exists in the cart, update the quantity
                                        int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                                        cartRef.child("quantity").setValue(currentQuantity + 1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(mContext, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        foodExists[0] = true;
                                    }
                                    else {
                                        // If the food item does not exist in the cart, add a new cart item
                                        Cart cart = new Cart(mUserName, foodID, holder.fName.getText().toString(),
                                                (Integer.parseInt(model.getPrice())), 1, model.getFoodImg());

                                        DatabaseReference newCartRef = FirebaseDatabase.getInstance().getReference("Cart")
                                                .child(mUserName)
                                                .child(foodID);

                                        newCartRef.setValue(cart)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(mContext, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_user,parent,false);
        return new FoodAdapter_User.myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView btnAdd;
        TextView fName, fPrice, fDes;
        ShapeableImageView foodImg;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.foodImg);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
            fName = itemView.findViewById(R.id.tvFoodName);
            fPrice = itemView.findViewById(R.id.tvPricee);
            fDes = itemView.findViewById(R.id.tvFoodDes);
        }
    }

}
