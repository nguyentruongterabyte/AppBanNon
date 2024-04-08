package com.example.appbannon.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.adapter.SanPhamAdapter;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.ProductApiCalls;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DanhSachSanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerViewDSSanPham;
    ApiBanHang apiBanHang;
    List<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Toolbar toolbar;
    NotificationBadge badgeGioHang;
    FrameLayout frameLayoutGioHang;
    ImageView imgSearch;
    LinearLayoutManager layoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setControl();
        ActionToolBar();
        initData();
        getDanhSachSanPham(page);
        addEventLoad();
        setEventClick();

    }

    private void setEventClick() {
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(gioHang);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timKiem = new Intent(getApplicationContext(), TimKiemSanPhamActivity.class);
                startActivity(timKiem);
            }
        });
    }

    private void initData() {
        // get danh sách giỏ hàng từ database về lưu vào mảng mangGioHang
        CartApiCalls.getAll(gioHangList -> {
            Utils.mangGioHang = gioHangList;
            badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
        }, compositeDisposable);
    }

    private void addEventLoad() {
        recyclerViewDSSanPham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    // Trong khi user scroll màn hình
                    int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == mangSanPham.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // add null vào mảng để hàm getItemViewType xác định được
                // user kéo đến phần tử cuối cùng
                mangSanPham.add(null);
                sanPhamAdapter.notifyItemInserted(mangSanPham.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                // remove null
                for (int i = mangSanPham.size() - 1; i >= 0; i--) {
                    if (mangSanPham.get(i) == null) {
                        mangSanPham.remove(i);
                        break;
                    }
                }
                sanPhamAdapter.notifyItemRemoved(mangSanPham.size());
                page += 1;
                getDanhSachSanPham(page);
                sanPhamAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1000);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDanhSachSanPham(int page) {

        ProductApiCalls.getInAPage(page, sanPhamList -> {
            if (sanPhamAdapter == null) {
                mangSanPham = sanPhamList;
                sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                recyclerViewDSSanPham.setAdapter(sanPhamAdapter);
            } else {
                int pos = mangSanPham.size() - 1;
                int soLuongAdd = sanPhamList.size();
                for (int i = 0; i < soLuongAdd; i++) {
                    mangSanPham.add(sanPhamList.get(i));
                }
            }
        }, compositeDisposable);
    }

    private void setControl() {
        recyclerViewDSSanPham = findViewById(R.id.recyclerViewDSSanPham);
        toolbar = findViewById(R.id.toolbar);

//        layoutManager = new GridLayoutManager(this, 2);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewDSSanPham.setLayoutManager(layoutManager);
        recyclerViewDSSanPham.setHasFixedSize(true);

        badgeGioHang = findViewById(R.id.menuSoLuong);
        frameLayoutGioHang = findViewById(R.id.frameGioHangManHinhChinh);
        imgSearch = findViewById(R.id.imgSearch);

        mangSanPham = new ArrayList<>();
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