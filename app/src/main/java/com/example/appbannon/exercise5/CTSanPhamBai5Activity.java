package com.example.appbannon.exercise5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbannon.R;

import java.io.Serializable;

public class CTSanPhamBai5Activity extends AppCompatActivity {

    EditText edtMaSanPham, edtTenSanPham, edtGiaSanPham;
    Button btnXoaSP, btnSuaSP, btnQuayLai;
    DatabaseSP dbSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctsan_pham_bai5);
        setControl();
        setEvent();
    }

    private void setEvent() {
        dbSanPham = new DatabaseSP(this);
        SanPham sanPham =(SanPham) getIntent().getSerializableExtra("sanPham");
        assert sanPham != null;
        edtMaSanPham.setText(sanPham.getMaSP());
        edtTenSanPham.setText(sanPham.getTenSP());
        edtGiaSanPham.setText(sanPham.getGiaSP());

        // Bắt sự kiện nút sửa được nhấn
        btnSuaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPham.setTenSP(edtTenSanPham.getText().toString());
                sanPham.setGiaSP(edtGiaSanPham.getText().toString());
                dbSanPham.suaDL(sanPham);
                Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        // Bắt sự kiện nút quay lại được nhấn
        // trở về main activity
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setControl() {
        edtMaSanPham = findViewById(R.id.edtMaSanPham);
        edtTenSanPham = findViewById(R.id.edtTenSanPham);
        edtGiaSanPham = findViewById(R.id.edtGiaSanPham);

        btnXoaSP = findViewById(R.id.btnXoaSP);
        btnSuaSP = findViewById(R.id.btnSuaSP);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }
}