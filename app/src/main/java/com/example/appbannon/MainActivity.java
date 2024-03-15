package com.example.appbannon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarManHinhChinh;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        ActionBar();
        ActionViewFlipper();
    }

    private void ActionViewFlipper() {
        List<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://media.istockphoto.com/id/1184522745/vi/anh/c%C6%B0%E1%BB%A1i-ng%E1%BB%B1a-rodeo-v%C4%83n-h%C3%B3a-mi%E1%BB%81n-t%C3%A2y-hoang-d%C3%A3-ch%E1%BB%A7-%C4%91%E1%BB%81-kh%C3%A1i-ni%E1%BB%87m-%C3%A2m-nh%E1%BA%A1c-%C4%91%E1%BB%93ng-qu%C3%AA-americana-v%C3%A0-m%E1%BB%B9-v%E1%BB%9Bi.jpg?s=1024x1024&w=is&k=20&c=eQWSy0ok0umbVrToBbNZ7hbwTD7-75vgee2EaRDLkDk=");
        mangQuangCao.add("https://media.istockphoto.com/id/1453988945/vi/anh/m%C5%A9-x%C3%B4-m%C3%A0u-v%C3%A0ng-c%C3%A1ch-ly-tr%C3%AAn-m%C3%A0u-tr%E1%BA%AFng.jpg?s=1024x1024&w=is&k=20&c=x3MA6las9ZkUwCC4f4uD5vbS4YCyGGtrdIxxsirTn3c=");
        mangQuangCao.add("https://media.istockphoto.com/id/535518012/vi/anh/ph%E1%BB%A5-n%E1%BB%AF-m%C5%A9-xanh-v%E1%BB%9Bi-m%E1%BA%A1ng-che-m%E1%BA%B7t.jpg?s=1024x1024&w=is&k=20&c=O17EaK8ZGJXvPpYGTKWtCMqLyeRQ9BNjrslYXMqQ2Sk=");

        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

//        Set animation cho view flipper
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbarManHinhChinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarManHinhChinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarManHinhChinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setControl() {
        toolbarManHinhChinh = findViewById(R.id.toolbarManHinhChinh);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleViewManHinhChinh);
        navigationView = findViewById(R.id.navigationView);
        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);
        drawerLayout = findViewById(R.id.drawerLayout);
    }
}