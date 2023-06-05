package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.foodordering_firebase.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, country, lattitude, longitude, city;
    Button getLocation;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        anhXa();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });


    }

    public void anhXa(){
        address = findViewById(R.id.tvAddress);
        country = findViewById(R.id.tvCountry);
        lattitude = findViewById(R.id.tvLattitude);
        longitude = findViewById(R.id.tvLongitude);
        city = findViewById(R.id.tvCity);
        getLocation = findViewById(R.id.getLocation);

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
                            lattitude.setText("Lattitude: " +addresses.get(0).getLatitude());
                            longitude.setText("Longitude: " + addresses.get(0).getLongitude());
                            address.setText("Address: "+addresses.get(0).getAddressLine(0));
                            city.setText("City: "+ addresses.get(0).getLocality());
                            country.setText("Country: "+ addresses.get(0).getCountryName());
                        } catch (IOException e) {
                            e.printStackTrace();
                           //throw new RuntimeException(e);
                        }


                    }
                }
            });
        }
        else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(TestActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
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
}