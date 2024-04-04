package com.example.appbannon.model;

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
}
