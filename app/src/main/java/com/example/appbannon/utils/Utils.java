package com.example.appbannon.utils;

import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.1.8/bannon/";
    public static List<GioHang> mangGioHang;
    public static List<GioHang> mangMuaHang = new ArrayList<>();
    public static User currentUser = new User();
}
