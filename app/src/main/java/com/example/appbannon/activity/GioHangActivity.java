package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbannon.R;
import com.example.appbannon.adapter.GioHangAdapter;
import com.example.appbannon.model.EventBus.TinhTongEvent;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class GioHangActivity extends AppCompatActivity {

    TextView tvGioHangTrong, tvTongTien;
    Button btnMuaHang;
    Toolbar toolbar;
    RecyclerView recyclerViewGioHang;
    GioHangAdapter adapter;
    long tongTienSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        setControl();
        initControl();
        ActionToolBar();
        setEvent();
        tinhTongTien();
    }

    private void setEvent() {
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.mangMuaHang.size() == 0) {
                    Toast.makeText(GioHangActivity.this, "Bạn phải chọn ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    intent.putExtra("tongTien", tongTienSP);
                    startActivity(intent);
                }
            }
        });
    }

    private void tinhTongTien() {
        tongTienSP = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++) {
            tongTienSP += Long.parseLong(Utils.mangMuaHang.get(i).getGiaSanPham());
        }
        DecimalFormat dft = new DecimalFormat("###,###,###");
        tvTongTien.setText(dft.format(tongTienSP));
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
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    private void setControl() {
        tvGioHangTrong = findViewById(R.id.tvGioHangTrong);
        tvTongTien = findViewById(R.id.tvTongTien);

        toolbar = findViewById(R.id.toolbar3);

        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);

        btnMuaHang = findViewById(R.id.btnMuaHang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Đăng ký vào event bus để bắt sự kiện
        // người dùng nhấn tăng giảm số lượng
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        // khi người dùng nhấn vào tăng giảm số lượng
        // trong giỏ hàng thì event bus sẽ bắt sự
        // kiện này và gọi hàm tính tổng tiền
        if (event != null) {
            tinhTongTien();
        }
    }

}