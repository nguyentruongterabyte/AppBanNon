package com.example.appbannon.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.appbannon.R;
import com.example.appbannon.model.ToaDo;
import com.example.appbannon.networking.LocationApiCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button btnMyLocation;
    Button btnGetDirections;
    private GoogleMap gMap;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ToaDo toaDo = new ToaDo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationApiCalls.initialize(this);
        setContentView(R.layout.activity_map);
        initData();
        setControl();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        setEvent();
    }

    private void initData() {
        LocationApiCalls.getLocation(toaDoModel -> {
            if (toaDoModel.getStatus() == 200) {
                toaDo = toaDoModel.getResult();
//                Log.d("mylog", toaDo.toString());
            } else {
                Toast.makeText(this, "Không thể lấy được vị trí của cửa hàng", Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }

    private void setEvent() {
//        
        btnMyLocation.setOnClickListener(v -> enableMyLocation());
        btnGetDirections.setOnClickListener(v -> getDirections());
    }

    private void setControl() {
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnGetDirections = findViewById(R.id.btnGetDirections);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LocationApiCalls.getLocation(toaDoModel -> {
            if (toaDoModel.getStatus() == 200) {
                toaDo = toaDoModel.getResult();
                Log.d("mylog", toaDo.toString());
                gMap = googleMap;

                LatLng location = new LatLng(toaDo.getViDo(), toaDo.getKinhDo());
                gMap.addMarker(new MarkerOptions().position(location).title(toaDo.getTenViTri()));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            } else {
                Toast.makeText(this, "Không thể lấy được vị trí của cửa hàng", Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
//        Log.d("mylog", toaDo.toString());
    }

    private void enableMyLocation() {
        if (gMap != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getDirections() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + toaDo.getViDo() + ',' + toaDo.getKinhDo());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Verify that the Google Maps app is available before launching the intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Handle the case where the Google Maps app is not available
            Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
