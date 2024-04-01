package com.example.appbannon.exercise5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSP extends SQLiteOpenHelper {

    public DatabaseSP(@Nullable Context context) {
        super(context, "dbSanPham", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tạo bảng sản phẩm khi khởi tạo
        String sqlSanPham = "create table tbSanPham (masp text, tensp text, giasp text, loaisp text)";
        db.execSQL(sqlSanPham);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlGioHang = "create table tbGioHang(masp text, tensp text, giasp text, loaisp text)";
        db.execSQL(sqlGioHang);
    }

    public void themDL(SanPham sanPham) {
        // thêm một sản phẩm mới
        SQLiteDatabase db = getWritableDatabase();
        String sql = "insert into tbSanPham values(?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{sanPham.getMaSP(), sanPham.getTenSP(), sanPham.getGiaSP(), sanPham.getLoaiSP()});
    }

    public void themDLVaoGioHang(SanPham sanPham) {
        // thêm sản phẩm vào giỏ hàng
        SQLiteDatabase db = getWritableDatabase();
        String sql = "insert into tbGioHang values (?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{sanPham.getMaSP(), sanPham.getTenSP(), sanPham.getGiaSP(), sanPham.getLoaiSP()});
    }

    public List<SanPham> docDL() {
        // Lấy tất cả sản phẩm trong database
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from tbSanPham";
        List<SanPham> data = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(cursor.getString(0));
                sanPham.setTenSP(cursor.getString(1));
                sanPham.setGiaSP(cursor.getString(2));
                sanPham.setLoaiSP(cursor.getString(3));
                data.add(sanPham);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public List<SanPham> docDLTrongGioHang() {

        // Lấy tất cả sản phẩm trong giỏ hàng
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from tbGioHang";
        List<SanPham> data = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(cursor.getString(0));
                sanPham.setTenSP(cursor.getString(1));
                sanPham.setGiaSP(cursor.getString(2));
                sanPham.setLoaiSP(cursor.getString(3));
                data.add(sanPham);
            } while (cursor.moveToNext());
        }

        return data;

    }

    public void xoaDL(SanPham sanPham) {
        // Xóa một sản phẩm trong danh sách sản phẩm
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from tbSanPham where masp=?";
        db.execSQL(sql, new Object[]{sanPham.getMaSP()});
    }

    public void xoaDLTrongGioHang(SanPham sanPham) {
        // Xóa một sản phẩm trong giỏ hàng
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from tbGioHang where masp=?";
        db.execSQL(sql, new Object[]{sanPham.getMaSP()});
    }

    public void suaDL(SanPham sanPham) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update tbSanPham set tensp=?, giasp=?, loaisp=? where masp=?";
        db.execSQL(sql, new Object[]{sanPham.getTenSP(), sanPham.getGiaSP(), sanPham.getLoaiSP(), sanPham.getMaSP()});

    }

}
