package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.admin.ListBillActivity;
import com.project.foodordering_firebase.admin.MenuActivity;
import com.project.foodordering_firebase.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends FirebaseRecyclerAdapter<User,UserAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull User model) {
        holder.userName.setText(model.getUserName());
        holder.passWord.setText(model.getPassWord());
        holder.fullName.setText(model.getFullName());
        holder.phoneNumber.setText(model.getPhoneNumber());

        //xem hoa don
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext(); // Use the view's context
                Intent intent = new Intent(context, ListBillActivity.class);
                intent.putExtra("userName", holder.userName.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.userName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_user_popup))
                        .setExpanded(true,1000)
                        .create();
                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText username = view1.findViewById(R.id.txtUserName);
                EditText password = view1.findViewById(R.id.txtPassword);
                EditText phonenumber = view1.findViewById(R.id.txtPhoneNumber);
                EditText fullname = view1.findViewById(R.id.txtFullName);

                Button save = view1.findViewById(R.id.btnSaveNewInfo);

                username.setText(model.getUserName());
                password.setText(model.getPassWord());
                phonenumber.setText(model.getPhoneNumber());
                fullname.setText(model.getFullName());

                dialogPlus.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("userName",username.getText().toString());
                        map.put("passWord",password.getText().toString());
                        map.put("fullName",fullname.getText().toString());
                        map.put("phoneNumber",phonenumber.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("User")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.userName.getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.userName.getContext(), "Chỉnh sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.userName.getContext());
                builder.setTitle("Xóa tài khoản");
                builder.setMessage("Bạn có muốn xóa tài khoản này?");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("User")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.userName.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView userName, passWord, fullName, phoneNumber;
        ImageView btnEditUser, btnDeleteUser, detail;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tvUserName);
            passWord = itemView.findViewById(R.id.tvPassword);
            fullName = itemView.findViewById(R.id.tvFullName);
            phoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
            btnEditUser = itemView.findViewById(R.id.btnEditUser);
            detail = itemView.findViewById(R.id.viewDetail);
        }
    }
}
