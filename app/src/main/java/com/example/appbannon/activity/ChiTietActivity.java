package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.model.SanPham;

import java.text.DecimalFormat;
import java.util.Objects;

public class ChiTietActivity extends AppCompatActivity {

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    Button btnThemVaoGioHang;
    ImageView imageChiTiet;
    Spinner spinnerSo;
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
        SanPham sanPham = (SanPham) getIntent().getSerializableExtra("chiTietSanPham" );
//        assert sanPham != null;
        if (sanPham != null) {
            DecimalFormat dft = new DecimalFormat("###,###,###");
            tvTenSanPham.setText(sanPham.getTenSanPham());
            tvGiaSanPham.setText(String.format("Giá: %s đ", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
            String moTa = "- Màu sắc: " + sanPham.getMauSac() +
                    "\n- Giới tính: " + sanPham.getGioiTinh() +
                    "\n- Số lượng: " + sanPham.getSoLuong();
            tvMoTaChiTiet.setText(moTa);
            Glide.with(getApplicationContext()).load(sanPham.getHinhAnh()).into(imageChiTiet);
        }
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        spinnerSo.setAdapter(adapterSpinner);
    }

    private void setControl() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);

        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        imageChiTiet = findViewById(R.id.imageChiTiet);

        spinnerSo = findViewById(R.id.spinnerSo);

        toolbar = findViewById(R.id.toolbar);

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}