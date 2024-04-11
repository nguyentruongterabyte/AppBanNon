package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ChiTietActivity extends AppCompatActivity {

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    Button btnThemVaoGioHang;
    ImageView imageChiTiet;
    Spinner spinnerSo;
    NotificationBadge badgeGioHang;
    FrameLayout frameLayoutGioHang;
    Toolbar toolbar;
    SanPham sanPham;


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        setControl();
        ActionToolBar();
        initData();
        setEvent();
    }

    private void setEvent() {
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themVaoGioHang();
            }
        });

        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(gioHang);
            }
        });
    }

    // hàm xử lý khi nhấn nút thêm vào giỏ hàng
    private void themVaoGioHang() {
        // Nếu size của mảng giỏ hàng > 0 thì phải kiểm tra từng phần tử có
        // bị lặp với sản phẩm vừa thêm vào giỏ hay không
        if (Utils.mangGioHang.size() > 0) {

            // biến flag để đánh dấu có tồn tại sản phẩm ở trong mảng giỏ hàng hay không
            // flag = false: không tồn tại
            // flag = true: tồn tại
            boolean flag = false;
            int soLuong = Integer.parseInt(spinnerSo.getSelectedItem().toString());
            // Lặp qua mảng giỏ hàng xem có sản phẩm nào trùng mã sản phẩm
            // thêm vào giỏ hàng không
            for (int i = 0; i < Utils.mangGioHang.size(); i++) {
                if (Utils.mangGioHang.get(i).getMaSanPham() == sanPham.getMaSanPham()) {
                    GioHang gioHangCu = Utils.mangGioHang.get(i);
                    // Cập nhật số lượng sản phẩm đã có trong danh sách giỏ hàng (api)
                    int soLuongMoi = soLuong + gioHangCu.getSoLuong();
                    // set số lượng mới cho sản phẩm trong giỏ hàng
                    Utils.mangGioHang.get(i).setSoLuong(soLuongMoi);
                    long gia = Long.parseLong(sanPham.getGiaSanPham()) * soLuongMoi;
                    // Set giá mới cho sản phẩm trong giỏ hàng
                    Utils.mangGioHang.get(i).setGiaSanPham(String.valueOf(gia));

                    flag = true;

                    // update lại giá và số lượng sản phẩm trong giỏ hàng database
                    CartApiCalls.update(Utils.mangGioHang.get(i), isSuccess -> {
                        if (isSuccess) {
                            badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
                        } else {
                            int index = GioHang.indexOf(Utils.mangGioHang, gioHangCu);
                            if (index != -1) {
                                Utils.mangGioHang.set(index, gioHangCu);
                            }
                        }
                    }, compositeDisposable);

                }

            }
            if (!flag) {
                // Sau khi kiểm tra trong mảng giỏ hàng nếu sản phẩm chưa tồn tại
                // trong mảng giỏ hàng thì thực hiện add sản phẩm vào giỏ hàng
                long gia = Long.parseLong(sanPham.getGiaSanPham()) * soLuong;
                // Lưu sản phẩm vào bảng giỏ hàng trong database
                GioHang gioHang = new GioHang();
                gioHang.setUserId(Utils.currentUser.getId());
                gioHang.setMaSanPham(sanPham.getMaSanPham());
                gioHang.setTenSanPham(sanPham.getTenSanPham());
                gioHang.setSoLuong(soLuong);
                gioHang.setHinhAnh(sanPham.getHinhAnh());
                gioHang.setGiaSanPham(String.valueOf(gia));


                // Thêm sản phẩm vào giỏ hàng trong database
                CartApiCalls.add(gioHang, isSuccess -> {
                    if (isSuccess) {
                        Utils.mangGioHang.add(gioHang);
                        badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
                    }
                }, compositeDisposable);


            }
        } else {
            // Lấy số lượng từ spinner số lượng
            int soLuong = Integer.parseInt(spinnerSo.getSelectedItem().toString());
            // giá = giá sản phẩm * số lượng
            long gia = Long.parseLong(sanPham.getGiaSanPham()) * soLuong;

            GioHang gioHang = new GioHang();
            gioHang.setUserId(Utils.currentUser.getId());
            gioHang.setMaSanPham(sanPham.getMaSanPham());
            gioHang.setTenSanPham(sanPham.getTenSanPham());
            gioHang.setSoLuong(soLuong);
            gioHang.setHinhAnh(sanPham.getHinhAnh());
            gioHang.setGiaSanPham(String.valueOf(gia));

            // Thêm sản phẩm vào giỏ hàng trong database
            CartApiCalls.add(gioHang, isSuccess -> {
                Utils.mangGioHang.add(gioHang);
                badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
            }, compositeDisposable);
        }

    }

    private void initData() {
        sanPham = (SanPham) getIntent().getSerializableExtra("chiTietSanPham");
//        assert sanPham != null;
        if (sanPham != null) {
            DecimalFormat dft = new DecimalFormat("###,###,###");
            tvTenSanPham.setText(sanPham.getTenSanPham());
            tvGiaSanPham.setText(String.format("Giá: %s đ", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
            String moTa = "- Màu sắc: " + sanPham.getMauSac() +
                    "\n- Giới tính: " + sanPham.getGioiTinh() +
                    "\n- Số lượng: " + sanPham.getSoLuong();
            tvMoTaChiTiet.setText(moTa);
            Glide.with(getApplicationContext()).load(sanPham.getHinhAnh()).into(imageChiTiet);
        }
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        spinnerSo.setAdapter(adapterSpinner);
    }

    private void setControl() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);

        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        imageChiTiet = findViewById(R.id.imageChiTiet);

        spinnerSo = findViewById(R.id.spinnerSo);

        toolbar = findViewById(R.id.toolbar);

        badgeGioHang = findViewById(R.id.menu_sl);

        frameLayoutGioHang = findViewById(R.id.frameGioHang);

        if (Utils.mangGioHang != null) {
            badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
    }
}