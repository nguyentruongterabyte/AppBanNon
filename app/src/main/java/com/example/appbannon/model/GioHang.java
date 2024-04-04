package com.example.appbannon.model;

import androidx.annotation.NonNull;

import java.util.List;

public class GioHang {
    private int maSanPham;
    private String tenSanPham;
    private String giaSanPham;
    private String hinhAnh;
    private int soLuong;

    public GioHang() {
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public static int indexOf(List<GioHang> mangGioHang, GioHang gioHang) {
        int index = -1;
        for (int i = 0; i < mangGioHang.size(); i++) {
            if (mangGioHang.get(i).getMaSanPham() == gioHang.getMaSanPham()) {
                index = i;
                break;
            }
        }
        return index;
    }

    @NonNull
    @Override
    public String toString() {
        return "GioHang{" +
                "maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", giaSanPham='" + giaSanPham + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", soLuong=" + soLuong +
                '}';
    }
}
