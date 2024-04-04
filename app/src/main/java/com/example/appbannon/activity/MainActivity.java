package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.adapter.DanhMucAdapter;
import com.example.appbannon.adapter.SanPhamMoiAdapter;
import com.example.appbannon.model.DanhMuc;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.CategoryApiCalls;
import com.example.appbannon.networking.ProductApiCalls;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarManHinhChinh;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    DanhMucAdapter danhMucAdapter;
    SanPhamMoiAdapter sanPhamAdapter;
    List<DanhMuc> mangDanhMuc;
    List<SanPham> mangSanPham;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setControl();
        ActionBar();
        ActionViewFlipper();
        initData();
        if (isConnected(this)) {
            ActionViewFlipper();
            getDanhMuc();
            getDanhSachSanPhamMoi();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "Không có internet, vui lòng kết nối wifi", Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
         // get danh sách giỏ hàng từ database về lưu vào mảng mangGioHang
        CartApiCalls.getAll(gioHangList -> {
            Utils.mangGioHang = gioHangList;
        }, compositeDisposable);
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangChu);
                        break;
                    case 1:
                        Intent danhSachSanPham = new Intent(getApplicationContext(), DanhSachSanPhamActivity.class);
                        startActivity(danhSachSanPham);
                        break;
                    case 2:
                        Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                        startActivity(gioHang);
                        break;
                }
            }
        });
    }

    private void getDanhSachSanPhamMoi() {
        ProductApiCalls.get10(sanPhamMoiList -> {
            mangSanPham = sanPhamMoiList;
            sanPhamAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSanPham);
            recyclerViewManHinhChinh.setAdapter(sanPhamAdapter);
        }, compositeDisposable);
    }

    private void getDanhMuc() {
        CategoryApiCalls.get(danhMucList -> {
            mangDanhMuc = danhMucList;
            danhMucAdapter = new DanhMucAdapter(mangDanhMuc, getApplicationContext());
            listViewManHinhChinh.setAdapter(danhMucAdapter);
        }, compositeDisposable);
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
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        viewFlipper.setInAnimation(slideIn);
        viewFlipper.setOutAnimation(slideOut);

    }

    private void ActionBar() {
        setSupportActionBar(toolbarManHinhChinh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);

        navigationView = findViewById(R.id.navigationView);
        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);
        drawerLayout = findViewById(R.id.drawerLayout);
//        Khởi tạo list
        mangDanhMuc = new ArrayList<>();
        mangSanPham = new ArrayList<>();
        if (Utils.mangGioHang == null) {
            Utils.mangGioHang = new ArrayList<>();
        }
//        Khởi tạo adapter
        danhMucAdapter = new DanhMucAdapter(mangDanhMuc, getApplicationContext());
        listViewManHinhChinh.setAdapter(danhMucAdapter);


    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}