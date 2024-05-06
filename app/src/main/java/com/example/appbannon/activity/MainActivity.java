package com.example.appbannon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.adapter.DanhMucAdapter;
import com.example.appbannon.adapter.SanPhamMoiAdapter;
import com.example.appbannon.model.DanhMuc;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.CategoryApiCalls;
import com.example.appbannon.networking.ProductApiCalls;
import com.example.appbannon.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarManHinhChinh;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    NotificationBadge badgeGioHang;
    FrameLayout frameLayoutGioHang;
    DanhMucAdapter danhMucAdapter;
    SanPhamMoiAdapter sanPhamAdapter;
    List<DanhMuc> mangDanhMuc;
    List<SanPham> mangSanPham;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            // Lấy thông tin user đã lưu trữ từ paper
            Utils.currentUser = Paper.book().read("user");
        }

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
        CartApiCalls.getAll(Utils.currentUser.getId(), gioHangList -> {
            Utils.mangGioHang = gioHangList;
            badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
        }, compositeDisposable);
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // start activity trang chủ
                    Intent trangChu = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(trangChu);
                    break;
                case 1:
                    // start activity danh sách sản phẩm
                    Intent danhSachSanPham = new Intent(getApplicationContext(), DanhSachSanPhamActivity.class);
                    startActivity(danhSachSanPham);
                    break;
                case 2:
                    Intent donHang = new Intent(getApplicationContext(), XemDonHangActivity.class);
                    startActivity(donHang);
                    break;
                case 3:
                    Intent map = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(map);
                    break;
                case 4:
                    // xóa key user
                    Paper.book().delete("user");
                    Paper.book().delete("isLogin");
                    Intent dangNhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(dangNhap);
                    finish();
                    break;
            }
        });
        frameLayoutGioHang.setOnClickListener(v -> {
            Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
            startActivity(gioHang);
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
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/emoji_df7627e1d5.png");
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/Hinh_Thumb_399a769927.jpeg");
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/Thumb_Gui_tiet_kiem_dbf899ee1a_e200cb7867.jpg");
        mangQuangCao.add("https://simg.zalopay.com.vn/zlp-website/assets/web_720x360_424_TKTK_MUC_LAI_SUAT_5_7_5d8a3bf64f.jpg");


        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        // Set animation cho view flipper
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

        viewFlipper.setInAnimation(slideIn);
        viewFlipper.setOutAnimation(slideOut);

    }

    private void ActionBar() {
        setSupportActionBar(toolbarManHinhChinh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarManHinhChinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarManHinhChinh.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
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

        badgeGioHang = findViewById(R.id.menuSoLuong);
        frameLayoutGioHang = findViewById(R.id.frameGioHangManHinhChinh);
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

    @Override
    protected void onResume() {
        super.onResume();
        badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));

    }
}