package com.example.appbannon.exercise5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbannon.R;

import java.util.ArrayList;
import java.util.List;

public class Bai5Activity extends AppCompatActivity {
    EditText edtMaSP, edtTenSP, edtGiaSP;
    Spinner spinnerLoaiSP;
    Button btnThem, btnXoa, btnSua, btnThoat;
    ImageView ivHinhLoaiSP;
    ListView lvDanhSach;
    List<SanPham> dataSP = new ArrayList<>();
    List<String> dataLoaiSP = new ArrayList<>();
    ArrayAdapter adapterLoaiSP;
    CustomAdapterSanPham adapterSanPham;
    DatabaseSP dbSanPham;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai5);
        setControl();
        setEvent();
    }

    private void setEvent() {
        dbSanPham = new DatabaseSP(this);
        Init();

        adapterLoaiSP = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataLoaiSP);
        spinnerLoaiSP.setAdapter(adapterLoaiSP);
        spinnerLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // khi người dùng chọn loại sản phẩm thì thay đổi hình ảnh tương tự
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerLoaiSP.getSelectedItem().equals("SamSung")) {
                    ivHinhLoaiSP.setImageResource(R.drawable.samsung);
                }
                else if (spinnerLoaiSP.getSelectedItem().equals("Iphone")) {
                    ivHinhLoaiSP.setImageResource(R.drawable.iphone);
                }
                else if (spinnerLoaiSP.getSelectedItem().equals("Nokia")) {
                    ivHinhLoaiSP.setImageResource(R.drawable.nokia);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterSanPham = new CustomAdapterSanPham(this, R.layout.layout_item_san_pham_bai_5, dataSP);
        lvDanhSach.setAdapter(adapterSanPham);
//        docDL();

        // Bắt sự kiện nút thêm được nhấn
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themDL();
                docDL();
                edtMaSP.setText("");
                edtTenSP.setText("");
                edtGiaSP.setText("");
            }
        });

        // Bắt sự kiện item sản phẩm trong list view được nhấn
        // và hiển thị vào các edit text tương ứng
        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy thông tin item sản phẩm được nhấn vào
                SanPham sanPham = dataSP.get(position);
                // Lưu vị trí sản phẩm vào biến index
                index = position;
                // hiển thị thông tin của item được nhấn vào
                // lên các edit text tương ứng
                edtMaSP.setText(sanPham.getMaSP());
                edtTenSP.setText(sanPham.getTenSP());
                edtGiaSP.setText(sanPham.getGiaSP());

                // đổi lại loại sản phẩm tương ứng với sản phẩm được nhấn vào
                switch (sanPham.getLoaiSP()) {
                    case "SamSung":
                        spinnerLoaiSP.setSelection(0);
                        ivHinhLoaiSP.setImageResource(R.drawable.samsung);
                        break;
                    case "Iphone":
                        spinnerLoaiSP.setSelection(1);
                        ivHinhLoaiSP.setImageResource(R.drawable.iphone);
                        break;
                    case "Nokia":
                        spinnerLoaiSP.setSelection(2);
                        ivHinhLoaiSP.setImageResource(R.drawable.nokia);
                        break;
                }
            }
        });

        // Bắt sự kiện nhấn giữ một item trong list view danh sách sản phẩm
        // và xóa sản phẩm đó
        lvDanhSach.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SanPham sanPham = dataSP.get(position);
                dbSanPham.xoaDL(sanPham);
                docDL();
                return false;
            }
        });

        // bắt sự kiện nút xóa được nhấn
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(edtMaSP.getText().toString());
                dbSanPham.xoaDL(sanPham);
                docDL();
            }
        });

        // bắt sự kiện nút sửa được nhấn
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(edtMaSP.getText().toString());
                sanPham.setTenSP(edtTenSP.getText().toString());
                sanPham.setGiaSP(edtGiaSP.getText().toString());
                sanPham.setLoaiSP(spinnerLoaiSP.getSelectedItem().toString());

                dbSanPham.suaDL(sanPham);
                docDL();
            }
        });

        // bắt sự kiện nút thoát được nhấn
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        docDL();
    }

    private void themDL() {
        // Khởi tạo một sản phẩm mới với các thông tin lấy từ người dùng nhập vào
        SanPham sanPham = new SanPham();
        sanPham.setMaSP(edtMaSP.getText().toString());
        sanPham.setTenSP(edtTenSP.getText().toString());
        sanPham.setGiaSP(edtGiaSP.getText().toString());
        sanPham.setLoaiSP(spinnerLoaiSP.getSelectedItem().toString());

        //  Lưu sản phẩm vào database
        dbSanPham.themDL(sanPham);
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();

    }

    private void docDL() {
        // xóa data sản phẩm trong list
        dataSP.clear();
        dataSP.addAll(dbSanPham.docDL());

        adapterSanPham.notifyDataSetChanged();
    }

    private void Init() {
        dataLoaiSP.add("SamSung");
        dataLoaiSP.add("Iphone");
        dataLoaiSP.add("Nokia");
    }

    private void setControl() {
        edtMaSP = findViewById(R.id.edtMaSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);

        spinnerLoaiSP = findViewById(R.id.spinnerLoaiSP);

        ivHinhLoaiSP = findViewById(R.id.ivHinhLoaiSP);

        btnThem = findViewById(R.id.btnThem);
        btnXoa = findViewById(R.id.btnXoa);
        btnSua = findViewById(R.id.btnSua);
        btnThoat = findViewById(R.id.btnThoat);

        lvDanhSach = findViewById(R.id.lvDanhSach);
    }
}