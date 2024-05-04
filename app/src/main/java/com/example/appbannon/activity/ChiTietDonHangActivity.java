package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.adapter.ChiTietDonHangAdapter;
import com.example.appbannon.model.DonHang;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    TextView maDonHang, totalItems, totalCost, tvKH, tvDC, tvTrangThai;
    RecyclerView recyclerViewChiTietDonHang;
    Button btnPDF;
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
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InHoaDonActivity.class);
                intent.putExtra("maDonHang", Integer.parseInt(donHang.getMaDonHang()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            donHang = (DonHang) intent.getSerializableExtra("donHang");
            if (donHang != null && donHang.getItems() != null) {
                maDonHang.setText("Mã đơn hàng: " + donHang.getMaDonHang());
                totalItems.setText(donHang.getSoLuong() + " sản phẩm");
                DecimalFormat dft = new DecimalFormat("###,###,###");
                totalCost.setText("Thành tiền: " + dft.format(Long.parseLong(donHang.getTongTien())));

                tvKH.setText("Mã khách hàng: " + donHang.getUserId());
                tvDC.setText("Địa chỉ: " + donHang.getDiaChi());

                tvTrangThai.setText("Trạng thái: " + donHang.getTrangThai());

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
    }


}