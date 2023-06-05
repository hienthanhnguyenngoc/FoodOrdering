package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Food;

import java.util.HashMap;
import java.util.Map;

public class FoodAdapter extends FirebaseRecyclerAdapter<Food,FoodAdapter.myViewHolder> {

    private Context mContext;
    private String mSName, mSCate;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Food model) {
        holder.foodName.setText(model.getFoodName());
        holder.price.setText(model.getPrice());
        holder.foodDes.setText(model.getFoodDes());
        if (holder.foodImg != null) {
            // Do something with the ImageView
            Glide.with(holder.foodImg.getContext()).load(model.getFoodImg()).into(holder.foodImg);
        } else {
            // ImageView is null, do something else
            // For example, set a default image
            holder.foodImg.setImageResource(R.drawable.ic_launcher_background);
        }

        if (holder.btnEdit != null) {
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.foodName.getContext())
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_food_popup))
                            .setExpanded(true,1200)
                            .create();

                    View view1 = dialogPlus.getHolderView();

                    //anh xa layout update_store_popup
                    EditText fName = view1.findViewById(R.id.txtFoodName);
                    EditText fDes = view1.findViewById(R.id.txtFoodDes);
                    EditText fImg = view1.findViewById(R.id.txtFoodImg);
                    EditText fPrice = view1.findViewById(R.id.txtFoodPrice);
                    Button save = view1.findViewById(R.id.btnSaveFoodInfo);


                    fName.setText(model.getFoodName());
                    fDes.setText(model.getFoodDes());
                    fImg.setText(model.getFoodImg());
                    fPrice.setText(model.getPrice());
                    Glide.with(holder.foodImg.getContext()).load(fImg.getText().toString()).into(holder.foodImg);


                    dialogPlus.show();

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("foodName",fName.getText().toString());
                            map.put("foodDes",fDes.getText().toString());
                            map.put("foodImg", fImg.getText().toString());
                            map.put("price", fPrice.getText().toString());


                            FirebaseDatabase.getInstance().getReference().child("Food").child(model.getFoodID())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.foodName.getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.foodName.getContext(), "Chỉnh sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    });
                }
            });
        }

        if (holder.btnDelete != null) {
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.foodName.getContext());
                    builder.setTitle("Xóa món ăn");
                    builder.setMessage("Bạn có muốn xóa món ăn này?");

                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("Food")
                                    .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                        }
                    });

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(holder.foodName.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
        }

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_admin,parent,false);
        return new FoodAdapter.myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView btnEdit, btnDelete;
        TextView foodName, price, foodDes;

        ShapeableImageView foodImg;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = itemView.findViewById(R.id.foodImg);
            btnEdit = itemView.findViewById(R.id.btnEditFood);
            btnDelete = itemView.findViewById(R.id.btnDeleteFood);
            foodDes = itemView.findViewById(R.id.tvFoodDes);
            foodName = itemView.findViewById(R.id.tvFoodName);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}
