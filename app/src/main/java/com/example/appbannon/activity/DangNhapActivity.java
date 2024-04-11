package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appbannon.R;
import com.example.appbannon.networking.UserApiCalls;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DangNhapActivity extends AppCompatActivity {
    ToggleButton toggleButtonVisibility;
    TextInputEditText edtPassword, edtEmail;
    TextView tvChuyenDangKy;
    AppCompatButton btnDangNhap;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_visibility_on,
                        0,
                        0,
                        0);
            } else {
                // Hide password
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_visibility_off,
                        0,
                        0,
                        0);
            }
        });

        // Chuyển màn hình đăng ký khi nhấn đăng kí
        tvChuyenDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi button đăng nhập được nhấn
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
                String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu email và mật khẩu cho lần đăng nhập sau
                    Paper.book().write("email", email);
                    Paper.book().write("password", password);

                    //post data
                    UserApiCalls.login(email, password, userModel -> {
                        if (userModel.isSuccess()) {
                            Utils.currentUser = userModel.getResult();
                            // Nếu đăng nhập thành công, chuyển về màn hình trang chủ
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Thông báo lỗi nếu email hoặc mật khẩu không đúng
                            Toast.makeText(DangNhapActivity.this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, compositeDisposable);

                }
            }
        });
    }

    private void setControl() {
        // Khởi tạo paper để nhớ email và mật khẩu khi thoát khỏi ứng dụng
        Paper.init(this);

        toggleButtonVisibility = findViewById(R.id.toggleButtonVisibility);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        tvChuyenDangKy = findViewById(R.id.tvChuyenDangKy);

        btnDangNhap = findViewById(R.id.btnDangNhap);

        // đọc dữ liệu của email và password những lần đăng nhập trước đó
        if (Paper.book().read("email") != null && Paper.book().read("password") != null) {
            edtEmail.setText(Paper.book().read("email"));
            edtPassword.setText(Paper.book().read("password"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.currentUser.getEmail() != null && Utils.currentUser.getPassword() != null) {
            edtEmail.setText(Utils.currentUser.getEmail());
            edtPassword.setText(Utils.currentUser.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}