package com.example.appbannon.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.adapter.ChiTietDonHangAdapter;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.networking.OrderApiCalls;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    TextView maDonHang, totalItems, totalCost, tvKH, tvDC, tvTrangThai;
    RecyclerView recyclerViewChiTietDonHang;
    Space spaceBtnHuy, spaceBtnDanhGia;
    AppCompatButton btnPDF, btnHuy, btnDanhGia;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DonHang donHang = new DonHang();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setControl();
        initData();
        setEvent();


    }

    private void setEvent() {
        btnPDF.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), InHoaDonActivity.class);
            intent.putExtra("maDonHang", donHang.getMaDonHang());
            startActivity(intent);
        });

        btnHuy.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận hủy đơn mua")
                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) ->
                                OrderApiCalls.cancel(donHang.getMaDonHang(), donHangModel -> {
                                    if (donHangModel.getStatus() == 200) {
                                        Toast.makeText(getApplicationContext(), donHangModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(this, donHangModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, compositeDisposable)
                        )
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @SuppressLint("DefaultLocale")
    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            donHang = (DonHang) intent.getSerializableExtra("donHang");
            if (donHang != null && donHang.getItems() != null) {
                maDonHang.setText(String.format("Mã đơn hàng: %s", donHang.getMaDonHang()));
                totalItems.setText(String.format("%d sản phẩm", donHang.getSoLuong()));
                DecimalFormat dft = new DecimalFormat("###,###,###");
                totalCost.setText(String.format("Thành tiền: %s", dft.format(Long.parseLong(donHang.getTongTien()))));

                tvKH.setText(String.format("Mã khách hàng: %d", donHang.getUserId()));
                tvDC.setText(String.format("Địa chỉ: %s", donHang.getDiaChi()));

                tvTrangThai.setText(String.format("Trạng thái: %s", donHang.getTrangThai()));

                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
                layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());
                ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(getApplicationContext(), donHang.getItems());
                recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
                recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);
                recyclerViewChiTietDonHang.setRecycledViewPool(viewPool);
                ChiTietDonHangAdapter adapter = new ChiTietDonHangAdapter(getApplicationContext(), donHang.getItems());
                recyclerViewChiTietDonHang.setAdapter(adapter);

                // Nếu đơn hàng có trạng thái 'chờ xác nhận' thì có thể hủy
                if (donHang.getTrangThai().equalsIgnoreCase("Chờ xác nhận")) {
                    btnHuy.setVisibility(View.VISIBLE);
                    spaceBtnHuy.setVisibility(View.VISIBLE);
                } else if (donHang.getTrangThai().equalsIgnoreCase("Đã giao")) {
                    // Nếu đơn hàng có trạng thái đã giao thì có thể đánh giá
                    btnDanhGia.setVisibility(View.VISIBLE);
                    spaceBtnDanhGia.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setControl() {
        maDonHang = findViewById(R.id.maDonHang);
        totalItems = findViewById(R.id.totalItems);
        totalCost = findViewById(R.id.totalCost);
        tvKH = findViewById(R.id.tvKH);
        tvDC = findViewById(R.id.tvDC);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        recyclerViewChiTietDonHang = findViewById(R.id.recyclerViewChiTietDonHang);

        btnPDF = findViewById(R.id.btnPDF);
        btnHuy = findViewById(R.id.btnHuy);
        btnDanhGia = findViewById(R.id.btnDanhGia);

        spaceBtnHuy = findViewById(R.id.spaceBtnHuy);
        spaceBtnDanhGia = findViewById(R.id.spaceBtnDanhGia);
    }


}