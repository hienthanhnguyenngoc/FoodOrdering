package com.project.foodordering_firebase.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.ViewPager.ViewPagerAdapter;
import com.project.foodordering_firebase.adapter.FavStoreAdapter;
import com.project.foodordering_firebase.adapter.FoodAdapter;
import com.project.foodordering_firebase.adapter.FoodAdapter_User;
import com.project.foodordering_firebase.model.FavStore;
import com.project.foodordering_firebase.model.Food;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    int currentPage = 0;

    ViewPagerAdapter viewPagerAdapter;

    private String number;

    ImageView com, bun, trangMieng, doAnNhanh, thucUong, anVat;
    TextView tCom, tBun, tTrangMieng, tDoAnNhanh, tThucUong, tAnVat, diadiem, search;
    ImageView info, cart, viewLocation;
    RecyclerView rvFavStore;
    FavStoreAdapter favStoreAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    String mapUrl;
    private String phoneNumber, fullName;

    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhXa();
        boolean isLoggedIn = getIntent().getBooleanExtra("login_status", false);
        setTitleCate();

        //lấy userName
        Intent intent = getIntent();
        String username = intent.getStringExtra("userName");

        //click search view
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("userName", username);
                startActivity(i);
            }
        });

        //click button info
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a Bundle object and put the username value in it
                Bundle bundle = new Bundle();
                bundle.putString("userName", pushUsername());
                bundle.putString("phoneNumber", pushPhoneNumber());
                bundle.putString("fullName", pushFullname());
                bundle.putString("passWord", pushPassword());

                // create an Intent to start the InfoActivity and put the Bundle as an extra
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("userName", username);
                intent.putExtra("login_status", isLoggedIn);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //click button cart
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an Intent to start the CartActivity and put the Bundle as an extra
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("userName", username);
                intent.putExtra("login_status", isLoggedIn);
                intent.putExtra("address",number);
                intent.putExtra("phoneNumber",pushPhoneNumber());
                intent.putExtra("fullName",pushFullname());
                startActivity(intent);
            }
        });

        //Nhận biến từ ListStoreActivity
        Intent i = getIntent();
        String storeName = i.getStringExtra("storeName");
        String storeImage = i.getStringExtra("storeImg");
        String sAddress = i.getStringExtra("address");
        String userName = i.getStringExtra("userName");
        String storeCate = i.getStringExtra("storeCate");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //set adapter cho RV list cua hang yeu thich
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFavStore.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<FavStore> options =
                new FirebaseRecyclerOptions.Builder<FavStore>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("FavStore").child(username), FavStore.class)
                        .build();

        favStoreAdapter = new FavStoreAdapter(options);
        rvFavStore.setAdapter(favStoreAdapter);
    }

    public void anhXa(){
        // banner trang chủ
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        setAutoScrollViewScroll();
        viewPager.setClipToOutline(true);

        //recyclerview
        rvFavStore = findViewById(R.id.rvFavStore);

        // menu home
        info = findViewById(R.id.iconInfo);
        cart = findViewById(R.id.iconCart);
        diadiem = findViewById(R.id.tvLocation);
        viewLocation = findViewById(R.id.viewLocation);
        search = findViewById(R.id.tvSearch);


        //cate - image view
        com = findViewById(R.id.ivCom);
        bun = findViewById(R.id.ivBun);
        trangMieng = findViewById(R.id.ivTrangMieng);
        doAnNhanh = findViewById(R.id.ivDoAnNhanh);
        thucUong = findViewById(R.id.ivThucUong);
        anVat = findViewById(R.id.ivAnVat);


        //cate - text view
        tCom = findViewById(R.id.titleCom);
        tBun = findViewById(R.id.titleBun);
        tTrangMieng = findViewById(R.id.titleTrangMieng);
        tDoAnNhanh = findViewById(R.id.titleDAN);
        tThucUong = findViewById(R.id.titleNuoc);
        tAnVat = findViewById(R.id.titleAnVat);
    }

    public void setTitleCate() {
        String cate1 = tCom.getText().toString();
        String cate2 = tAnVat.getText().toString();
        String cate3 = tThucUong.getText().toString();
        String cate4 = tBun.getText().toString();
        String cate5 = tDoAnNhanh.getText().toString();
        String cate6 = tTrangMieng.getText().toString();

        boolean isLoggedIn = getIntent().getBooleanExtra("login_status", false);


        if (com != null) {
            //danh mục Cơm
            com.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate1);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }

        if (bun != null) {
            //danh mục Bún
            bun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate4);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }

        if (anVat != null) {
            //danh mục Ăn Vặt
            anVat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate2);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }

        if (thucUong != null) {
            //danh mục Nước
            thucUong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate3);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }

        if (doAnNhanh != null) {
            //danh mục Đồ Ăn Nhanh
            doAnNhanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate5);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }

        if (trangMieng != null) {
            //danh mục Tráng miệng
            trangMieng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListStoreActivity.class);
                    intent.putExtra("storeCate", cate6);
                    intent.putExtra("userName", pushUsername());
                    intent.putExtra("login_status", isLoggedIn);
                    startActivity(intent);
                }
            });
        }
    }

    public String pushUsername(){
        //nhan username tu Login
        String username = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            username = b.getString("userName");
        }
        return username;
    }

    public String pushPassword(){

        String password = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            password = b.getString("passWord");
        }
        return password;
    }

    public String pushPhoneNumber(){
        //nhan phone tu DB
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(pushUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot info : snapshot.getChildren()) {
                    phoneNumber = info.child("phoneNumber").getValue(String.class);
                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
        });
        return phoneNumber;
    }
    public String pushFullname(){
        //nhan fullName tu DB
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(pushUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot info : snapshot.getChildren()) {
                    fullName = info.child("fullName").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return fullName;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (favStoreAdapter != null) {
            favStoreAdapter.startListening();
        }
    }

    public void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                            String latitude = Double.toString(addresses.get(0).getLatitude());
                            String longitude = Double.toString(addresses.get(0).getLongitude());
                            //diadiem.setText(addresses.get(0).getAddressLine(0));
                            String diachi = addresses.get(0).getAddressLine(0);
                            String[] addressParts = diachi.split(",\\s+");
                            number = addressParts[0];
                            diadiem.setText(number);
                            mapUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                            //city.setText("City: "+ addresses.get(0).getLocality());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        }
        else {
            askPermission();
        }

        //icon location
        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
                startActivity(intent);
            }
        });

    }

    private void askPermission() {
        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Vui lòng cho phép quyền định vị", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setAutoScrollViewScroll() {
        Timer timer;
        final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY_MS, PERIOD_MS);
    }
}