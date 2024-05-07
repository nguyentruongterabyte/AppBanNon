package com.example.appbannon.utils;

import android.annotation.SuppressLint;

import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://10.252.1.179/hatshop/";
    public static final String BASE_IMAGE_URL = "images/";
    public static List<GioHang> mangGioHang;
    public static List<GioHang> mangMuaHang = new ArrayList<>();
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
