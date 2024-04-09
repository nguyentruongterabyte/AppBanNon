package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMucModel;
import com.example.appbannon.model.DonHangModel;
import com.example.appbannon.model.GioHangModel;
import com.example.appbannon.model.MessageModel;
import com.example.appbannon.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("danhmuc.php")
    Observable<DanhMucModel> getDanhMuc();

    @GET("products-featured.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi();

    @GET("cart-list.php")
    Observable<GioHangModel> getDanhSachSanPhamTrongGioHang();


    @POST("cart-create.php")
    @FormUrlEncoded
    Observable<GioHangModel> themSanPhamVaoGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("tenSanPham") String tenSanPham,
            @Field("gia") String gia,
            @Field("hinhAnh") String hinhAnh,
            @Field("soLuong") int soLuong
            );

    @POST("cart-update-products.php")
    @FormUrlEncoded
    Observable<GioHangModel> updateSanPhamTrongGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("gia") String gia,
            @Field("soLuong") int soLuong
    );

    @POST("cart-delete-product.php")
    @FormUrlEncoded
    Observable<GioHangModel> xoaSanPhamKhoiGioHang(
            @Field("maSanPham") int maSanPham
    );

    @POST("products-page.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDanhSachSanPham(
            @Field("page") int page,
            @Field("amount") int amount
    );

    @POST("products-search.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDanhSachSanPhamTimKiem(
            @Field("key") String key
    );

    @POST("orders-create.php")
    @FormUrlEncoded
    Observable<DonHangModel> createDonHang(
            @Field("sdt") String sdt,
            @Field("email") String email,
            @Field("tongTien") String tongTien,
            @Field("diaChi") String diaChi,
            @Field("soLuong") int soLuong,
            @Field("chiTiet") String chiTiet
    );

    @POST("orders-update-token.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("token") String token,
            @Field("id") int idDonHang
    );

}
