package com.example.appbannon.utils;

import android.annotation.SuppressLint;

import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    // Base URL
    public static final String BASE_URL = "http://192.168.1.20:8000/hatshop/";

    // Base image url
    public static final String BASE_IMAGE_URL = "images/";

    // mảng giỏ hàng
    public static List<GioHang> mangGioHang;

    // mảng mua hàng
    public static List<GioHang> mangMuaHang = new ArrayList<>();

    // current user
    public static User currentUser = new User();


    // hàm định dạng số lượng đã bán
    @SuppressLint("DefaultLocale")
    public static String formatQuantity(int quantity) {
        if (quantity < 1000) {
            return String.valueOf(quantity);
        } else if (quantity < 1000000) {
            return String.format("%.1fk", quantity / 1000.0);
        } else {
            return String.format("%.1fm", quantity / 1000000.0);
        }
    }
}
