package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appbannon.R;

public class ChiTietActivity extends AppCompatActivity {

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    Button btnThemVaoGioHang;
    ImageView imageChiTiet;
    Spinner spinner;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        setControl();
        ActionToolBar();
        initData();
    }

    private void initData() {

    }

    private void setControl() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);

        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        imageChiTiet = findViewById(R.id.imageChiTiet);

        spinner = findViewById(R.id.spinner);

        toolbar = findViewById(R.id.toolbar);

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}