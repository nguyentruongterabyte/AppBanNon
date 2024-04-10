package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.appbannon.R;
import com.google.android.material.textfield.TextInputEditText;

public class DangNhapActivity extends AppCompatActivity {
    ToggleButton toggleButtonVisibility;
    TextInputEditText edtPassword;
    TextView tvChuyenDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
    }

    private void setEvent() {
        toggleButtonVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_on, 0, 0, 0);
            } else {
                // Hide password
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off, 0, 0, 0);
            }
        });

        // Chuyển màn hình đăng ký khi nhấn đăng kí
        tvChuyenDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
            startActivity(intent);
        });
    }

    private void setControl() {
        toggleButtonVisibility = findViewById(R.id.toggleButtonVisibility);
        edtPassword = findViewById(R.id.edtPassword);
        tvChuyenDangKy = findViewById(R.id.tvChuyenDangKy);
    }
}