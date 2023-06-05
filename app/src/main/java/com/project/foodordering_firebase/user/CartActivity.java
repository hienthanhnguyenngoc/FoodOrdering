package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.CartAdapter;
import com.project.foodordering_firebase.adapter.FoodAdapter_User;
import com.project.foodordering_firebase.adapter.StoreAdapter_User;
import com.project.foodordering_firebase.model.Bill;
import com.project.foodordering_firebase.model.Cart;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CartActivity extends AppCompatActivity{

    ImageView back;
    Button btnBook;
    TextView deleteAll, price;

    RecyclerView recyclerView;

    CartAdapter cartAdapter;
    private double total = 0;
    private String formattedTotal, address, phone, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        anhXa();

        //
        Bundle b = getIntent().getExtras();
        String userName = b.getString("userName");
        address = b.getString("address");
        phone = b.getString("phoneNumber");
        fullname = b.getString("fullName");


        recyclerView = findViewById(R.id.rvCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cart").child(userName)
                                .orderByChild("userName").equalTo(userName), Cart.class)
                        .build();


        cartAdapter = new CartAdapter(options, getApplicationContext(), userName, total);
        recyclerView.setAdapter(cartAdapter);

        //tính tổng tiền
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userName);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = 0;
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    double price = foodSnapshot.child("price").getValue(Double.class);
                    int quantity = foodSnapshot.child("quantity").getValue(Integer.class);
                    double foodPrice = price * quantity;
                    total += foodPrice;
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    formattedTotal = formatter.format(total);
                    if (quantity == 0) {
                        total = 0;
                    }
                }
                Log.d("Cart", "Total price: " + total);
                if (total == 0) {
                    price.setText(0 + " VNĐ");
                } else {
                    price.setText(formattedTotal + " VNĐ");
                }
                //calculateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle errors
            }
        });

        //đặt hàng
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra trong giỏ hàng có sản phẩm hay không
                if (total == 0) {
                    Toast.makeText(CartActivity.this, "Không có sản phẩm nào trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    //build trang dialog xác nhận thông tin (dialog_confirm_xml)
                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(CartActivity.this);
                    View dialogView = inflater.inflate(R.layout.dialog_confirm_info, null);
                    builder.setView(dialogView);

                    EditText edFullName = dialogView.findViewById(R.id.txtFullName);
                    EditText edPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);
                    EditText edAddress = dialogView.findViewById(R.id.txtAddress);
                    Button confirm = dialogView.findViewById(R.id.btnConfirm);

                    edFullName.setText(fullname);
                    edPhoneNumber.setText(phone);
                    edAddress.setText(address);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //xác nhận đặt hàng
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String address = edAddress.getText().toString().trim();
                            String fullname = edFullName.getText().toString().trim();
                            String phone = edPhoneNumber.getText().toString().trim();

                            //kiểm tra điền đủ thông tin giao hàng
                            if (address.equals("")) {
                                Toast.makeText(CartActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            }
                            else {
//                                DatabaseReference foodReference = FirebaseDatabase.getInstance().getReference("Food");
//                                foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot storeSnapshot : snapshot.getChildren()) {
//                                            String getFoodName = storeSnapshot.child("foodName").getValue(String.class);
//                                            if (getFoodName.equals(foodName)) {
//                                                foodID = storeSnapshot.child("foodID").getValue(String.class);
//                                                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userName).child(foodID);
//                                                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        for (DataSnapshot storeSnapshot : snapshot.getChildren()) {
//                                                            foodName = storeSnapshot.child("foodName").getValue(String.class);
//                                                            pricee = storeSnapshot.child("price").getValue(Integer.class);
//                                                            quantity = storeSnapshot.child("quantity").getValue(Integer.class);
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bill").child(userName);
                                //tạo bill
                                //lấy ngày giờ tạo bill
                                Date now = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String datetime = format.format(now);

                                //tạo node billID
                                String billID = reference.push().getKey();
                                Bill bill = new Bill(userName, billID, datetime, fullname, phone, address, total);

                                reference.child(billID).setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cart")
                                                .child(userName);
                                        ref.removeValue();
                                        boolean isLoggedIn = getIntent().getBooleanExtra("login_status", false);
                                        Intent intent = new Intent(CartActivity.this, BookingSuccessActivity.class);
                                        intent.putExtra("login_status", isLoggedIn);
                                        intent.putExtra("userName", userName);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

        //xóa tất cả
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Xóa tất cả");
                builder.setMessage("Bạn có muốn sóa tất cả món ăn trong giỏ hàng?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart");
                        reference.child(userName).removeValue();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do something
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);

        //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent o = new Intent(getApplicationContext(), HomeActivity.class);
//                o.putExtra("userName", userName);
//                startActivity(o);
                onBackPressed();
            }
        });
    }

    public void anhXa() {
        back = findViewById(R.id.back);
        btnBook = findViewById(R.id.btnBook);
        deleteAll = findViewById(R.id.tvDeleteAll);
        price = findViewById(R.id.tv_totalprice);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (cartAdapter != null) {
            cartAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cartAdapter != null) {
            cartAdapter.stopListening();
        }
    }

}