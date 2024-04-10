package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMucModel;
import com.example.appbannon.model.DonHangModel;
import com.example.appbannon.model.GioHangModel;
import com.example.appbannon.model.MessageModel;
import com.example.appbannon.model.SanPhamModel;
import com.example.appbannon.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    // Danh mục
    @GET("danhmuc.php")
    Observable<DanhMucModel> getDanhMuc();


    // Sản phẩm
    @GET("products-featured.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi();

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

    // giỏ hàng
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


    // Đơn hàng
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

    // user
    @POST("user-register.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

}
