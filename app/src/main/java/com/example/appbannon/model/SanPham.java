package com.example.appbannon.model;

import java.io.Serializable;
import java.util.List;

public class SanPham implements Serializable {
    private int maSanPham;
    private String tenSanPham;
    private String giaSanPham;
    private String hinhAnh;

    private int soLuong;
    private String gioiTinh;
    private String mauSac;

    private int daBan;
    // đánh giá
    private List<DanhGia> danhGiaSanPham;

    public int getDaBan() {
        return daBan;
    }

    public void setDaBan(int daBan) {
        this.daBan = daBan;
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

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }
    public List<DanhGia> getDanhGiaSanPham() {
        return danhGiaSanPham;
    }

    public void setDanhGiaSanPham(List<DanhGia> danhGiaSanPham) {
        this.danhGiaSanPham = danhGiaSanPham;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", giaSanPham='" + giaSanPham + '\'' +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", soLuong=" + soLuong +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", mauSac='" + mauSac + '\'' +
                  ", daBan=" + daBan +
                ", danhGiaDSanPham=" + danhGiaSanPham +
                '}';
    }
}
// De t
