package com.example.appbannon.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbannon.R;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.networking.BillApiCalls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class InHoaDonActivity extends AppCompatActivity {

    Button btnXuatFile;
    TextView tvHoaDon;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    DonHang donHang = new DonHang();
    StringBuilder info = new StringBuilder();

    List<SanPham> sanPhamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BillApiCalls.initialize(this);
        setContentView(R.layout.activity_in_hoa_don);
        setControl();
        initData();
        setEvent();
    }

    private void setEvent() {
        btnXuatFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF(donHang);
            }
        });
    }

    private void initData() {
        int maDonHang = getIntent().getIntExtra("maDonHang", -1);
        Log.d("maDonHang", String.valueOf(maDonHang));
        if (maDonHang != -1) {
            BillApiCalls.get(maDonHang, hoaDonModel -> {
                if (hoaDonModel.getStatus() == 200) {
                    donHang = hoaDonModel.getResult();
                    sanPhamList = donHang.getItems();
                    taoThongTinHoaDon();
                }
            }, compositeDisposable);
        }
    }

    private void taoThongTinHoaDon() {

        DecimalFormat dft = new DecimalFormat("###,###,###");
        info =
                new StringBuilder("                 HAT SHOP\n" +
                        "97 Man Thiện, Phường Tăng Nhơn Phú A\n" +
                        "TP. Thủ Đức, Việt Nam\n" +
                        "--------------------------------------------------------------\n" +
                        "HÓA ĐƠN\n" +
                        "Mã hóa đơn: " + donHang.getMaDonHang() + "\n" +
                        "Ngày CT      : " + donHang.getNgayTao() + "\n" +
                        "--------------------------------------------------------------\n");
        for (SanPham sanPham : sanPhamList) {
            info.append(sanPham.getTenSanPham()).append("\n");
            info.append("x").append(sanPham.getSoLuong()).append("       ");
            info.append(dft.format(Long.parseLong(sanPham.getGiaSanPham()))).append("       ");
            info.append(dft.format(Long.parseLong(sanPham.getGiaSanPham()) * sanPham.getSoLuong())).append("\n");
        }
        info.append("--------------------------------------------------------------\n");
        info.append("Tổng tiền: ").append(dft.format(Long.parseLong(donHang.getTongTien()))).append("\n");
        info.append("Zalo pay: ").append(donHang.getHasToken() == 0 ? 0 : dft.format(Long.parseLong(donHang.getTongTien()))).append("\n");
        info.append("Thanh toán: ").append(donHang.getHasToken() == 0 ? dft.format(Long.parseLong(donHang.getTongTien())) : 0);
        tvHoaDon.setText(info.toString());
        Log.d("mylog", info.toString());
    }

    private void createPDF(DonHang donHang) {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(42);
        float x = 100;
        float y = 100;

        String[] lines = info.toString().split("\n");
        for (String line : lines) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }

        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "invoice_" + donHang.getMaDonHang() + ".pdf";
        File file = new File(downloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Invoice PDF created successfully!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setControl() {
        btnXuatFile = findViewById(R.id.btnXuatFile);
        tvHoaDon = findViewById(R.id.tvHoaDon);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}