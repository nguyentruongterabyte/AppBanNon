package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbannon.R;
import com.example.appbannon.adapter.GioHangAdapter;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.utils.Utils;

import java.util.List;
import java.util.Objects;

public class GioHangActivity extends AppCompatActivity {

    TextView tvGioHangTrong, tvTongTien;
    Button btnMuaHang;
    Toolbar toolbar;
    RecyclerView recyclerViewGioHang;
    GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        setControl();
        initControl();
        ActionToolBar();
    }

    private void initControl() {
        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);

        if (Utils.mangGioHang.size() == 0) {
            tvGioHangTrong.setVisibility(View.VISIBLE);

        } else {
            adapter = new GioHangAdapter(getApplicationContext(), Utils.mangGioHang);
            recyclerViewGioHang.setAdapter(adapter);
        }
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
    private void setControl() {
        tvGioHangTrong = findViewById(R.id.tvGioHangTrong);
        tvTongTien = findViewById(R.id.tvTongTien);

        toolbar = findViewById(R.id.toolbar3);

        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);

        btnMuaHang = findViewById(R.id.btnMuaHang);


    }
}