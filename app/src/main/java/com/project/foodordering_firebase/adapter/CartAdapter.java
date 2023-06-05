package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Cart;

public class CartAdapter extends FirebaseRecyclerAdapter<Cart,CartAdapter.myViewHolder> {
    String userName;
    Context mContext;

    double totalPrice;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param total
     */

    public CartAdapter(@NonNull FirebaseRecyclerOptions<Cart> options, Context context, String username, double total) {
        super(options);
        userName = username;
        mContext = context;
        totalPrice = total;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Cart model) {
        holder.fName.setText(model.getFoodName());
        holder.fPrice.setText(String.valueOf(model.getPrice()));
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        Glide.with(holder.fImg.getContext()).load(model.getFoodImg()).into(holder.fImg);


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the food item already exists in the cart
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart")
                        .child(userName).child(model.getFoodID());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // If the food item exists in the cart, update the quantity
                            int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                            reference.child("quantity").setValue(currentQuantity + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                int sl = Integer.parseInt(holder.quantity.getText().toString()) + 1;
                holder.quantity.setText(String.valueOf(sl));
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(holder.quantity.getText().toString()) - 1;
                if (sl >= 1) {

                    // Check if the food item already exists in the cart
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart")
                            .child(userName).child(model.getFoodID());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // If the food item exists in the cart, update the quantity
                                int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                                reference.child("quantity").setValue(currentQuantity - 1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    holder.quantity.setText(String.valueOf(sl));

                } else {
                    holder.quantity.setText("1");
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.fName.getContext());
                builder.setTitle("Xóa món ăn");
                builder.setMessage("Bạn có muốn xóa món " + holder.fName.getText().toString()+ " không?");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart")
                                .child(userName)
                                .child(model.getFoodID());
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(holder.fName.getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.fName.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public CartAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView fName, fPrice, quantity;
        ImageView plus, minus, fImg, delete;

        RecyclerView recyclerView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.tvFoodName);
            fPrice = itemView.findViewById(R.id.tvFoodPrice);
            quantity = itemView.findViewById(R.id.tvQuantity);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            fImg = itemView.findViewById(R.id.foodImg);
            recyclerView = itemView.findViewById(R.id.rvCart);
            delete = itemView.findViewById(R.id.btnDeleteFood);
        }
    }

}
