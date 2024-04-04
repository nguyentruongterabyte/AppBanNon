package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMucModel;
import com.example.appbannon.model.GioHangModel;
import com.example.appbannon.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiBanHang {
    @GET("danhmuc.php")
    Observable<DanhMucModel> getDanhMuc();

    @GET("danhsachsanphammoi.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi();

    @GET("danhsachgiohang.php")
    Observable<GioHangModel> getDanhSachSanPhamTrongGioHang();


    @POST("themsanphamvaogiohang.php")
    @FormUrlEncoded
    Observable<GioHangModel> themSanPhamVaoGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("tenSanPham") String tenSanPham,
            @Field("gia") String gia,
            @Field("hinhAnh") String hinhAnh,
            @Field("soLuong") int soLuong
            );

    @POST("updatesanphamtronggiohang.php")
    @FormUrlEncoded
    Observable<GioHangModel> updateSanPhamTrongGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("gia") String gia,
            @Field("soLuong") int soLuong
    );

    @POST("danhsachsanpham.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDanhSachSanPham(
            @Field("page") int page
    );

}
