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
import com.example.appbannon.model.User;
import com.example.appbannon.networking.UserApiCalls;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {

    ToggleButton toggleButtonVisibilityPassword, toggleButtonVisibilityRePassword;
    TextInputEditText edtPassword, edtRePassword, edtEmail, edtMobile, edtUsername;
    TextView tvChuyenDangNhap;

    AppCompatButton btnDangKy;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        setControl();
        setEvent();
    }

    private void setEvent() {
        toggleButtonVisibilityPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // show password
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleButtonVisibilityPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_on, 0, 0, 0);
            } else {
                // Hide password
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButtonVisibilityPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off, 0, 0, 0);
            }

        });

        toggleButtonVisibilityRePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show retype password
                edtRePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleButtonVisibilityRePassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_on, 0, 0, 0);
            } else {
                // Hide retype password
                edtRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButtonVisibilityRePassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off, 0, 0, 0);
            }
        });

        // Chuyển màn hình đăng nhập khi nhấn vào đăng nhập
        tvChuyenDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện khi nút đăng ký được nhấn
        btnDangKy.setOnClickListener(v -> dangKy());
    }

    private void dangKy() {
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();
        String rePassword = Objects.requireNonNull(edtRePassword.getText()).toString().trim();
        String mobile = Objects.requireNonNull(edtMobile.getText()).toString().trim();
        String username = Objects.requireNonNull(edtUsername.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(rePassword)) {
            Toast.makeText(this, "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Bạn chưa nhập họ và tên", Toast.LENGTH_SHORT).show();
        } else {
            if (rePassword.equals(password)) {
                // post data
                User user = new User(email, password, username, mobile);
                UserApiCalls.register(user, userModel -> {
                    if (userModel.isSuccess()) {
                        Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, compositeDisposable);

            } else {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setControl() {
        toggleButtonVisibilityPassword = findViewById(R.id.toggleButtonVisibilityDK);
        toggleButtonVisibilityRePassword = findViewById(R.id.toggleButtonVisibilityRePasswordDK);

        edtPassword = findViewById(R.id.edtPasswordDK);
        edtRePassword = findViewById(R.id.edtRePasswordDK);
        edtEmail = findViewById(R.id.edtEmailDK);
        edtMobile = findViewById(R.id.edtMobile);
        edtUsername = findViewById(R.id.edtUsername);

        tvChuyenDangNhap = findViewById(R.id.tvChuyenDangNhap);

        btnDangKy = findViewById(R.id.btnDangKy);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}