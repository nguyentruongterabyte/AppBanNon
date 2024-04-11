package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

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
import com.example.appbannon.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class TimKiemSanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerViewDSSanPhamTimKiem;
    Toolbar toolbar;
    NotificationBadge badgeGioHang;
    FrameLayout frameLayoutGioHang;
    EditText edtSearch;
    SanPhamAdapter sanPhamAdapter;
    List<SanPham> mangSanPham;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_san_pham);
        setControl();
        ActionToolBar();
        initData();
        setEventClick();
        setEventTextChanged();
    }

    private void setEventTextChanged() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mangSanPham.clear();
                    sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                    recyclerViewDSSanPhamTimKiem.setAdapter(sanPhamAdapter);
                } else {
                    getDataSearch(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getDataSearch(String searchKey) {
        ProductApiCalls.search(searchKey, sanPhamList -> {
            mangSanPham.clear();
            mangSanPham = sanPhamList;
            sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
            recyclerViewDSSanPhamTimKiem.setAdapter(sanPhamAdapter);
        }, compositeDisposable);

    }

    private void setEventClick() {
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(gioHang);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewDSSanPhamTimKiem = findViewById(R.id.recyclerViewDSSanPhamTimKiem);
        badgeGioHang = findViewById(R.id.menuSoLuong);
        frameLayoutGioHang = findViewById(R.id.frameGioHangTimKiem);
        edtSearch = findViewById(R.id.edtSearch);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewDSSanPhamTimKiem.setLayoutManager(layoutManager);
        recyclerViewDSSanPhamTimKiem.setHasFixedSize(true);

        mangSanPham = new ArrayList<>();
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

    private void initData() {
        // get danh sách giỏ hàng từ database về lưu vào mảng mangGioHang
        CartApiCalls.getAll(Utils.currentUser.getId(), gioHangList -> {
            Utils.mangGioHang = gioHangList;
            badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
        }, compositeDisposable);
    }
}