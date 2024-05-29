package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appbannon.R;
import com.example.appbannon.networking.UserApiCalls;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ResetPasswordActivity extends AppCompatActivity {

    TextInputEditText edtEmailReset;
    AppCompatButton btnReset;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserApiCalls.initialize(this);
        setContentView(R.layout.activity_reset_password);
        setControl();
        setEvent();
    }

    private void setEvent() {
        // Xử lý sự kiện khi nút reset được nhấn
        btnReset.setOnClickListener(v -> {
            String email = Objects.requireNonNull(edtEmailReset.getText()).toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Bạn chưa nhập địa chỉ email", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                UserApiCalls.requestResetPassword(email, messageModel -> {
                    if (messageModel.getStatus() == 200) {
                        Toast.makeText(this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, compositeDisposable);
            }
        });
    }

    private void setControl() {
        edtEmailReset = findViewById(R.id.edtEmailReset);
        btnReset = findViewById(R.id.btnReset);
        progressBar = findViewById(R.id.processBarReset);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}