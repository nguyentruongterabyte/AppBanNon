package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbannon.R;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.OrderApiCalls;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTongTienSP, tvSDT, tvEmail;

    TextInputEditText edtDiaChi;

    AppCompatButton btnDatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongTien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        setControl();
        ActionToolBar();
        countItem();
        setEvent();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++) {
            totalItem += Utils.mangMuaHang.get(i).getSoLuong();
        }
    }

    private void setEvent() {
        btnDatHang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
                String diaChi = Objects.requireNonNull(edtDiaChi.getText()).toString().trim();
                if (TextUtils.isEmpty(diaChi)) {
                    Toast.makeText(ThanhToanActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    // post data
                    DonHang donHang = new DonHang();
                    donHang.setEmail(tvEmail.getText().toString());
                    donHang.setSdt(tvSDT.getText().toString());
                    donHang.setTongTien(String.valueOf(tongTien));
                    donHang.setDiaChi(edtDiaChi.getText().toString());
                    donHang.setSoLuong(totalItem);
                    donHang.setChiTiet(new Gson().toJson(Utils.mangMuaHang));
                    // gửi api tạo đơn hàng và tạo chi tiết đơn hàng
                    OrderApiCalls.create(donHang, isSuccess -> {
                        if (isSuccess) {
                            // Nếu thêm đơn hàng thành công thì thực hiện xóa sản phẩm
                            // ra khỏi giỏ hàng

                            for (GioHang gh : Utils.mangMuaHang) {
//                                for (int i = 0; i < Utils.mangGioHang.size(); i++) {
//                                    if (Utils.mangGioHang.get(i).getMaSanPham() == gh.getMaSanPham()) {
//
//                                        Log.d("test", gh.toString());
//                                        Utils.mangGioHang.remove(i);
//                                        break;
//                                    }
//                                }
                                CartApiCalls.delete(gh, ok -> {
                                    CartApiCalls.getAll(mangGioHang -> {
                                        Utils.mangGioHang = mangGioHang;
                                    }, compositeDisposable);
                                }, compositeDisposable);
                                Log.d("test", String.valueOf(Utils.mangGioHang.size()));

                            }
                            // mảng mua hàng sẽ trống
                            Utils.mangMuaHang = new ArrayList<>();
                            Toast.makeText(ThanhToanActivity.this, "Thêm đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ThanhToanActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    }, compositeDisposable);
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
        tongTien = getIntent().getLongExtra("tongTien", 0);
        tvTongTienSP.setText(String.format("%sđ", dft.format(tongTien)));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}