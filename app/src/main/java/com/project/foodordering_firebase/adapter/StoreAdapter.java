package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.project.foodordering_firebase.admin.MenuActivity;
import com.project.foodordering_firebase.model.Store;

import java.util.HashMap;
import java.util.Map;


public class StoreAdapter extends FirebaseRecyclerAdapter<Store,StoreAdapter.myViewHolder>{

    Context mContext;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StoreAdapter(Context context, FirebaseRecyclerOptions<Store> options) {
        super(options);
        mContext = context;
    }

    public StoreAdapter(FirebaseRecyclerOptions<Store> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Store model) {
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

        if (holder.btnEditStore != null) {
            holder.btnEditStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.sName.getContext())
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_store_popup))
                            .setExpanded(true,1200)
                            .create();

                    View view1 = dialogPlus.getHolderView();

                    //anh xa layout update_store_popup
                    EditText sName = view1.findViewById(R.id.txtSName);
                    EditText sAddress = view1.findViewById(R.id.txtSAddress);
                    EditText sImg2 = view1.findViewById(R.id.txtSImg);
                    Button save = view1.findViewById(R.id.btnSaveStoreInfo);
                    Spinner spinner = view1.findViewById(R.id.spinner_store);


                    String[] categories = {"Mì-Bún-Phở", "Cơm", "Tráng Miệng", "Đồ Ăn Nhanh", "Thức Uống", "Ăn Vặt"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.sName.getContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    String sCate = spinner.getSelectedItem().toString();

                    // Get the position of the value of sCate in the categories array
                    int selectionPosition = adapter.getPosition(sCate);

                    // Set the selection of the Spinner to the value of sCate
                    spinner.setSelection(selectionPosition);

                    sName.setText(model.getStoreName());
                    sAddress.setText(model.getAddress());
                    sImg2.setText(model.getStoreImg());
                    Glide.with(holder.sImg.getContext()).load(sImg2.getText().toString()).into(holder.sImg);


                    dialogPlus.show();

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("address",sAddress.getText().toString());
                            map.put("storeName",sName.getText().toString());
                            map.put("storeCate", spinner.getSelectedItem().toString());
                            map.put("storeImg", sImg2.getText().toString());


                            FirebaseDatabase.getInstance().getReference().child("Store").child(model.getStoreID())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.sName.getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.sName.getContext(), "Chỉnh sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    });
                }
            });
        }

        if (holder.btnDeleteStore != null) {
            holder.btnDeleteStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.sName.getContext());
                    builder.setTitle("Xóa cửa hàng");
                    builder.setMessage("Bạn có muốn xóa cửa hàng này?");

                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("Store")
                                    .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                        }
                    });

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(holder.sName.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
        }

        if (holder.btnSDetail != null) {
            holder.btnSDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext(); // Use the view's context
                    Intent intent = new Intent(context, MenuActivity.class);
                    intent.putExtra("sName", holder.sName.getText().toString());
                    intent.putExtra("sCate", holder.sCate.getText().toString());
                    context.startActivity(intent);
                }
            });
        }

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_admin,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView sName, sAddress, sCate;
        ImageView btnEditStore, btnDeleteStore, btnSDetail;
        RecyclerView recyclerView;
        ShapeableImageView sImg;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            sName = itemView.findViewById(R.id.tvStoreName);
            sAddress = itemView.findViewById(R.id.tvAddress);
            sCate = itemView.findViewById(R.id.tvStoreCate);
            sImg = itemView.findViewById(R.id.sImg);


            btnDeleteStore = itemView.findViewById(R.id.btnDeleteStore);
            btnEditStore = itemView.findViewById(R.id.btnEditStore);
            btnSDetail = itemView.findViewById(R.id.btnSDetail);

            recyclerView = itemView.findViewById(R.id.rvListStore);
        }
    }

}