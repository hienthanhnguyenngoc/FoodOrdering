package com.project.foodordering_firebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Bill;
import com.project.foodordering_firebase.user.DetailOrderActivity;
import com.project.foodordering_firebase.user.HistoryBookingActivity;
import com.project.foodordering_firebase.user.HomeActivity;
import com.project.foodordering_firebase.user.InfoActivity;

public class BillAdapter_Admin extends FirebaseRecyclerAdapter<Bill, BillAdapter_Admin.myViewHolder> {
    Context mContext;
    String mUserName;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public BillAdapter_Admin (Context context, FirebaseRecyclerOptions<Bill> options, String username) {
        super(options);
        mContext = context;
        mUserName = username;
    }

    public BillAdapter_Admin(FirebaseRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Bill model) {
        holder.bId.setText("Mã đơn hàng: "+ model.getBillID());
        holder.bDate.setText("Ngày: "+model.getDatetime());
        holder.btnBDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent i = new Intent(context, DetailOrderActivity.class);

                i.putExtra("userName", mUserName);
                i.putExtra("fullName", model.getFullName());
                i.putExtra("phoneNumber", model.getPhoneNumber());
                i.putExtra("address",model.getAddress());
                i.putExtra("totalPrice", model.getTotalPrice());
                i.putExtra("billID", holder.bId.getText().toString());
                i.putExtra("datetime", holder.bDate.getText().toString());

                context.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_booking,parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView bId, bDate;
        ImageView btnBDetail;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            bId = itemView.findViewById(R.id.tvorderID);
            bDate = itemView.findViewById(R.id.tvDate);
            btnBDetail = itemView.findViewById(R.id.tvViewDetail);
        }
    }
}

