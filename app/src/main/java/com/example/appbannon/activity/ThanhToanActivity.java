package com.example.appbannon.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.appbannon.R;
import com.example.appbannon.model.Address.Address;
import com.example.appbannon.model.Address.District;
import com.example.appbannon.model.Address.Province;
import com.example.appbannon.model.Address.Ward;
import com.example.appbannon.model.CreateOrder;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.networking.AddressApiCalls;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.networking.OrderApiCalls;
import com.example.appbannon.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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
    Spinner spinnerProvince, spinnerDistrict, spinnerWard;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongTien;
    int totalItem;
    DonHang donHang;
    List<Province> provinces = new ArrayList<>();
    List<District> districts;
    List<Ward> wards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrderApiCalls.initialize(this);
        CartApiCalls.initialize(this);
        AddressApiCalls.initialize(this);
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

        fetchProvinces();
    }

    private void setupProvinceSpinner(List<Province> provinces) {
        if (provinces != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            adapter.clear();
            for (Province province : provinces) {
                adapter.add(province.getName());
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProvince.setAdapter(adapter);
        }
    }

    private void setupDistrictSpinner(List<District> districts) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.clear();
        for (District district : districts) {
            adapter.add(district.getName());
        }
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(adapter);
    }

    private void setupWardSpinner(List<Ward> wards) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.clear();
        for (Ward ward : wards) {
            adapter.add(ward.getName());
        }
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWard.setAdapter(adapter);
    }

    private void fetchProvinces() {
        AddressApiCalls.getProvinces(provinceResponse -> {
            if (provinceResponse.getError() != 2) {
                provinces = provinceResponse.getData();
                for (int i = 0; i < provinces.size(); i++) {
                    Log.d("myLog", provinces.get(i).getName());
                }
                setupProvinceSpinner(provinces);
            } else {
                Log.d("myLog", provinceResponse.getError_text());
            }
        }, compositeDisposable);
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++) {
            totalItem += Utils.mangMuaHang.get(i).getSoLuong();
        }
    }

    private void setEvent() {

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Province được chọn từ danh sách
                Province selectedProvince = provinces.get(position);
                // Lấy id của Province được chọn
                String selectedProvinceId = selectedProvince.getId();
                Address address = new Address.Builder()
                        .province(selectedProvince.getName())
                        .build();

                edtDiaChi.setText(address.getProvince());

                AddressApiCalls.getDistricts(selectedProvinceId, districtResponse -> {
                    runOnUiThread(() -> {
                        if (districtResponse.getError() != 2) {
                            districts = districtResponse.getData();
                            setupDistrictSpinner(districts);
                        }
                    });
                }, compositeDisposable);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District selectedDistrict = districts.get(position);

                String selectedDistrictId = selectedDistrict.getId();
                Log.d("myLog", selectedDistrictId);
                Address address = new Address.Builder()
                        .province(spinnerProvince.getSelectedItem().toString())
                        .district(selectedDistrict.getName())
                        .build();

                edtDiaChi.setText(address.getDistrict() + ", " + address.getProvince());
                AddressApiCalls.getWards(selectedDistrictId, wardResponse -> {
                    if (wardResponse.getError() != 2) {
                        wards = wardResponse.getData();
                        runOnUiThread(() -> setupWardSpinner(wards));
                    }
                }, compositeDisposable);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Ward selectedWard = wards.get(position);

                Address address = new Address.Builder()
                        .province(spinnerProvince.getSelectedItem().toString())
                        .district(spinnerDistrict.getSelectedItem().toString())
                        .ward(spinnerWard.getSelectedItem().toString())
                        .build();
                edtDiaChi.setText(address.getWard() + ", " + address.getDistrict() + ", " + address.getProvince());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                // gửi api tạo đơn hàng và tạo chi tiết đơn hàng
                OrderApiCalls.create(donHang, maDonHang -> {
                    if (maDonHang != -1) {
                        // Nếu thêm đơn hàng thành công thì thực hiện xóa sản phẩm
                        // ra khỏi giỏ hàng

                        for (GioHang gh : Utils.mangMuaHang) {
                            // xóa sản phẩm đã mua trong database
                            CartApiCalls.delete(gh.getMaSanPham(), gh.getUserId(), ok -> CartApiCalls.getAll(Utils.currentUser.getId(), mangGioHang -> {
                                // cập nhật mảng giỏ hàng
                                Utils.mangGioHang = mangGioHang;
                            }, compositeDisposable), compositeDisposable);

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
                Log.d("myLog", donHang.toString());
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


                        Log.d("myLog", "Thanh toán zalo thành công");

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
                                OrderApiCalls.updateToken(token, maDonHang, isSuccess -> {
                                    if (isSuccess) {
                                        Toast.makeText(ThanhToanActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                        // xóa mảng mua hàng
                                        Utils.mangMuaHang.clear();
                                    } else {
                                        Toast.makeText(ThanhToanActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                    }
                                }, compositeDisposable);
                                Intent intent = new Intent(getApplicationContext(), XemDonHangActivity.class);
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

        spinnerProvince = findViewById(R.id.spinnerProvince);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerWard = findViewById(R.id.spinnerWard);

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