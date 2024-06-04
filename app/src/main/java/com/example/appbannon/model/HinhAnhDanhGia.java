package com.example.appbannon.model;

import androidx.annotation.NonNull;

public class HinhAnhDanhGia {
    private int maHinhAnh;
    private int maDanhGia;
    private String hinhAnhURL;

    public HinhAnhDanhGia(int maHinhAnh, int maDanhGia, String hinhAnhURL) {
        this.maHinhAnh = maHinhAnh;
        this.maDanhGia = maDanhGia;
        this.hinhAnhURL = hinhAnhURL;
    }

    public HinhAnhDanhGia(int maDanhGia, String hinhAnhURL) {
        this.maDanhGia = maDanhGia;
        this.hinhAnhURL = hinhAnhURL;
    }

    public HinhAnhDanhGia(String hinhAnhURL) {
        this.hinhAnhURL = hinhAnhURL;
    }

    public int getMaHinhAnh() {
        return maHinhAnh;
    }

    public void setMaHinhAnh(int maHinhAnh) {
        this.maHinhAnh = maHinhAnh;
    }

    public int getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(int maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public String getHinhAnhURL() {
        return hinhAnhURL;
    }

    public void setHinhAnhURL(String hinhAnhURL) {
        this.hinhAnhURL = hinhAnhURL;
    }

    @NonNull
    @Override
    public String toString() {
        return "HinhAnhDanhGia{" +
                "maHinhAnh=" + maHinhAnh +
                ", maDanhGia=" + maDanhGia +
                ", hinhAnhURL='" + hinhAnhURL + '\'' +
                '}';
    }
}
