package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbannon.R;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Objects;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTongTienSP, tvSDT, tvEmail;

    TextInputEditText edtDiaChi;

    AppCompatButton btnDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        setControl();
        ActionToolBar();
        setEvent();
    }

    private void setEvent() {
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaChi = edtDiaChi.getText().toString().trim();
                if (TextUtils.isEmpty(diaChi)) {
                    Toast.makeText(ThanhToanActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    // post data
                    Log.d("test", new Gson().toJson(Utils.mangGioHang));
                }
            }
        });
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
        toolbar = findViewById(R.id.toolbar2);

        tvTongTienSP = findViewById(R.id.tvTongTienSP);
        tvSDT = findViewById(R.id.tvSDT);
        tvEmail = findViewById(R.id.tvEmail);

        edtDiaChi = findViewById(R.id.edtDiaChi);

        btnDatHang = findViewById(R.id.btnDatHang);

        DecimalFormat dft = new DecimalFormat("###,###,###");
        long tongTien = getIntent().getLongExtra("tongTien", 0);
        tvTongTienSP.setText(String.format("%sđ", dft.format(tongTien)));
    }
}