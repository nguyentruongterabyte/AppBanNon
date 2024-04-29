package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.appbannon.R;
import com.example.appbannon.model.CreateOrder;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.OrderApiCalls;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTongTienSP, tvSDT, tvEmail;

    TextInputEditText edtDiaChi;

    AppCompatButton btnDatHang, btnZaloPay;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongTien;
    int totalItem;
    DonHang donHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        // Zalo
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2554, Environment.SANDBOX);
        setControl();
        initData();
        ActionToolBar();
        countItem();
        setEvent();
    }

    private void initData() {
        tvEmail.setText(Utils.currentUser.getEmail());
        tvSDT.setText(Utils.currentUser.getMobile());
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++) {
            totalItem += Utils.mangMuaHang.get(i).getSoLuong();
        }
    }

    private void setEvent() {
        btnDatHang.setOnClickListener(v -> {

            String diaChi = Objects.requireNonNull(edtDiaChi.getText()).toString().trim();
            if (TextUtils.isEmpty(diaChi)) {
                Toast.makeText(ThanhToanActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                // post data
                DonHang donHang = new DonHang();
                donHang.setEmail(tvEmail.getText().toString());
                donHang.setSdt(tvSDT.getText().toString());
                donHang.setUserId(Utils.currentUser.getId());
                donHang.setTongTien(String.valueOf(tongTien));
                donHang.setDiaChi(edtDiaChi.getText().toString());
                donHang.setSoLuong(totalItem);
                donHang.setChiTiet(new Gson().toJson(Utils.mangMuaHang));
                System.out.println("chitiet" + new Gson().toJson(Utils.mangMuaHang));

                // gửi api tạo đơn hàng và tạo chi tiết đơn hàng
                OrderApiCalls.create(donHang, maDonHang -> {
                    if (maDonHang != -1) {
                        // Nếu thêm đơn hàng thành công thì thực hiện xóa sản phẩm
                        // ra khỏi giỏ hàng

                        for (GioHang gh : Utils.mangMuaHang) {
                            // xóa sản phẩm đã mua trong database
                            CartApiCalls.delete(gh.getMaSanPham(), gh.getUserId(), ok -> {
                                CartApiCalls.getAll(Utils.currentUser.getId(), mangGioHang -> {
                                    // cập nhật mảng giỏ hàng
                                    Utils.mangGioHang = mangGioHang;
                                }, compositeDisposable);
                            }, compositeDisposable);

                        }
                        // mảng mua hàng sẽ trống
                        Utils.mangMuaHang.clear();
                        Toast.makeText(ThanhToanActivity.this, "Thêm đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ThanhToanActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }, compositeDisposable);
            }
        });

        btnZaloPay.setOnClickListener(v -> {
            String diaChi = Objects.requireNonNull(edtDiaChi.getText()).toString().trim();
            if (TextUtils.isEmpty(diaChi)) {
                Toast.makeText(ThanhToanActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                donHang = new DonHang();
                donHang.setUserId(Utils.currentUser.getId());
                donHang.setEmail(tvEmail.getText().toString());
                donHang.setSdt(tvSDT.getText().toString());
                donHang.setTongTien(String.valueOf(tongTien));
                donHang.setDiaChi(edtDiaChi.getText().toString());
                donHang.setSoLuong(totalItem);
                donHang.setChiTiet(new Gson().toJson(Utils.mangMuaHang));
                requestZaloPay();

            }
        });
    }

    private void requestZaloPay() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(String.valueOf(tongTien));
            String code = data.getString("return_code");
            Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");

                ZaloPaySDK.getInstance().payOrder(ThanhToanActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {

                        OrderApiCalls.create(donHang, maDonHang -> {
                            if (maDonHang != -1) {
                                // Nếu thêm đơn hàng thành công thì thực hiện xóa sản phẩm
                                // ra khỏi giỏ hàng
                                // xóa mảng mua hàng và cập nhật lại mảng giỏ hàng
                                // xóa giỏ hàng trên database
                                for (GioHang gh : Utils.mangMuaHang) {
                                    // xóa sản phẩm đã mua trong database
                                    CartApiCalls.delete(gh.getMaSanPham(), gh.getUserId(), ok -> {
                                        if (ok) {
                                            CartApiCalls.getAll(Utils.currentUser.getId(), mangGioHang -> {
                                                // cập nhật mảng giỏ hàng
                                                Utils.mangGioHang = mangGioHang;
                                            }, compositeDisposable);
                                        }
                                    }, compositeDisposable);

                                }
                                // update token thanh toán của đơn hàng vừa tạo
                                CartApiCalls.updateToken(token, maDonHang, isSuccess -> {
                                    if (isSuccess) {
                                        Toast.makeText(ThanhToanActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                        // xóa mảng mua hàng
                                        Utils.mangMuaHang.clear();
                                    } else {
                                        Toast.makeText(ThanhToanActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                    }
                                }, compositeDisposable);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }, compositeDisposable);


                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("test", "Canceled");
                        Toast.makeText(ThanhToanActivity.this, "Đã hủy giao dịch", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Toast.makeText(ThanhToanActivity.this, "Lỗi trong quá trình thanh toán", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar2);

        tvTongTienSP = findViewById(R.id.tvTongTienSP);
        tvSDT = findViewById(R.id.tvSDT);
        tvEmail = findViewById(R.id.tvEmail);

        edtDiaChi = findViewById(R.id.edtDiaChi);

        btnDatHang = findViewById(R.id.btnDatHang);
        btnZaloPay = findViewById(R.id.btnZaloPay);

        DecimalFormat dft = new DecimalFormat("###,###,###");
        tongTien = getIntent().getLongExtra("tongTien", 0);
        tvTongTienSP.setText(String.format("%sđ", dft.format(tongTien)));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}