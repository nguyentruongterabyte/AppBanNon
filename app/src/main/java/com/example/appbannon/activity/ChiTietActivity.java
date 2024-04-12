package com.example.appbannon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet, tvSoLuong;
    Button btnThemVaoGioHang;
    ImageView imageChiTiet;
    NotificationBadge badgeGioHang;
    FrameLayout frameLayoutGioHang;
    Toolbar toolbar;
    SanPham sanPham;
    ImageView imageChiTietTru, imageChiTietCong;

    int soLuongSPTrongGioHang = 0;

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

        // Xử lý khi nhấn vào nút trừ
        imageChiTietTru.setOnClickListener(v -> {
            int soLuong = Integer.parseInt(tvSoLuong.getText().toString());
            if (soLuong > 1) {
                // set lại số lượng = số lượng -1
                tvSoLuong.setText(String.valueOf(soLuong - 1));
            }
        });

        // Xử lý khi nhấn vào nút cộng
        imageChiTietCong.setOnClickListener(v -> {

            // Lấy số lượng từ text view số lượng
            int soLuong = Integer.parseInt(tvSoLuong.getText().toString());

            // nếu số lượng nhỏ hơn số lượng tối đa - số lượng sản phẩm trong giỏ hàng
            if (soLuong < sanPham.getSoLuong() - soLuongSPTrongGioHang) {
                // set lại số lượng = số lượng +1
                tvSoLuong.setText(String.valueOf(soLuong + 1));
            }
        });

        // Xử lý sự kiện nút thêm vào giỏ hàng được nhấn
        btnThemVaoGioHang.setOnClickListener(v -> themVaoGioHang());

        // Xử lý sự kiện nhấn vào biểu tượng giỏ hàng
        frameLayoutGioHang.setOnClickListener(v -> {
            Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
            startActivity(gioHang);
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
            int soLuong = Integer.parseInt(tvSoLuong.getText().toString());
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
                            // cập nhật lại textView số lượng = số lượng sản phẩm tối đa - (số lượng sản phẩm trong giỏ hàng + soLuong)
                            tvSoLuong.setText(String.valueOf(sanPham.getSoLuong() - (soLuongSPTrongGioHang + soLuong) > 0 ? 1 : 0));
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
                gioHang.setSoLuongToiDa(sanPham.getSoLuong());


                // Thêm sản phẩm vào giỏ hàng trong database
                CartApiCalls.add(gioHang, isSuccess -> {
                    if (isSuccess) {
                        Utils.mangGioHang.add(gioHang);
                        badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));
                        // cập nhật lại textView số lượng = số lượng sản phẩm tối đa - (số lượng sản phẩm trong giỏ hàng + soLuong)
                        tvSoLuong.setText(String.valueOf(sanPham.getSoLuong() - (soLuongSPTrongGioHang + soLuong) > 0 ? 1 : 0));
                    }
                }, compositeDisposable);


            }
        } else {
            // Lấy số lượng từ textView số lượng
            int soLuong = Integer.parseInt(tvSoLuong.getText().toString());
            // giá = giá sản phẩm * số lượng
            long gia = Long.parseLong(sanPham.getGiaSanPham()) * soLuong;

            GioHang gioHang = new GioHang();
            gioHang.setUserId(Utils.currentUser.getId());
            gioHang.setMaSanPham(sanPham.getMaSanPham());
            gioHang.setTenSanPham(sanPham.getTenSanPham());
            gioHang.setSoLuong(soLuong);
            gioHang.setHinhAnh(sanPham.getHinhAnh());
            gioHang.setGiaSanPham(String.valueOf(gia));
            gioHang.setSoLuongToiDa(sanPham.getSoLuong());

            // Thêm sản phẩm vào giỏ hàng trong database
            CartApiCalls.add(gioHang, isSuccess -> {
                Utils.mangGioHang.add(gioHang);
                badgeGioHang.setText(String.valueOf(Utils.mangGioHang.size()));

                // cập nhật lại textView số lượng = số lượng sản phẩm tối đa - (số lượng sản phẩm trong giỏ hàng + soLuong)
                tvSoLuong.setText(String.valueOf(sanPham.getSoLuong() - (soLuongSPTrongGioHang + soLuong) > 0 ? 1 : 0));
            }, compositeDisposable);
        }
    }

    private void initData() {
        sanPham = (SanPham) getIntent().getSerializableExtra("chiTietSanPham");
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

        // Nếu sản phẩm đã được thêm vào giỏ hàng trước đó
        // thì lấy số lượng đó ra để so sánh
        // với số lượng người dùng nhấn thêm
        for (int i = 0; i < Utils.mangGioHang.size(); i++) {
            GioHang gioHang = Utils.mangGioHang.get(i);
            if (gioHang.getMaSanPham() == sanPham.getMaSanPham()) {
                soLuongSPTrongGioHang = gioHang.getSoLuong();
            }
        }

        // Set số lượng để thêm vào giỏ hàng = 0 nếu số lượng sản phẩm
        // trong giỏ hàng đạt giơi hạn số lượng sản phẩm trong db
        // = 1 trong trường hợp còn lại
        assert sanPham != null;
        tvSoLuong.setText(String.valueOf(soLuongSPTrongGioHang != sanPham.getSoLuong() ? 1 : 0));

    }

    private void setControl() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);

        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        imageChiTiet = findViewById(R.id.imageChiTiet);
        imageChiTietTru = findViewById(R.id.imageChiTietTru);
        imageChiTietCong = findViewById(R.id.imageChiTietCong);

        tvSoLuong = findViewById(R.id.tvSoLuong);

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
        toolbar.setNavigationOnClickListener(v -> finish());
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

        // Nếu sản phẩm đã được thêm vào giỏ hàng trước đó
        // thì lấy số lượng đó ra để so sánh
        // với số lượng người dùng nhấn thêm
        for (int i = 0; i < Utils.mangGioHang.size(); i++) {
            GioHang gioHang = Utils.mangGioHang.get(i);
            if (gioHang.getMaSanPham() == sanPham.getMaSanPham()) {
                soLuongSPTrongGioHang = gioHang.getSoLuong();
            }
        }

        // Set số lượng để thêm vào giỏ hàng = 0 nếu số lượng sản phẩm
        // trong giỏ hàng đạt giơi hạn số lượng sản phẩm trong db
        // = 1 trong trường hợp còn lại
        assert sanPham != null;
        tvSoLuong.setText(String.valueOf(soLuongSPTrongGioHang != sanPham.getSoLuong() ? 1 : 0));
    }
}