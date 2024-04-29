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
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiBanHang {
    // Danh mục
    @GET("api/categories/user.php")
    Observable<DanhMucModel> getDanhMuc();


    // Sản phẩm
    @GET("api/product/featured.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi(
            @Query("amount") int amount
    );

    @GET("api/product/page.php")
    Observable<SanPhamModel> getDanhSachSanPham(
            @Query("page") int page,
            @Query("amount") int amount
    );

    @GET("api/product/search.php")
    Observable<SanPhamModel> getDanhSachSanPhamTimKiem(
            @Query("key") String key
    );

    // giỏ hàng
    @GET("api/cart/list.php")
    Observable<GioHangModel> getDanhSachSanPhamTrongGioHang(
            @Query("userId") int userId
    );

    @POST("api/cart/create.php")
    @FormUrlEncoded
    Observable<GioHangModel> themSanPhamVaoGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("tenSanPham") String tenSanPham,
            @Field("gia") String gia,
            @Field("hinhAnh") String hinhAnh,
            @Field("soLuong") int soLuong
    );

    @PUT("api/cart/update-products.php")
    @FormUrlEncoded
    Observable<GioHangModel> updateSanPhamTrongGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("gia") String gia,
            @Field("soLuong") int soLuong
    );

    @HTTP(method = "DELETE", path = "api/cart/delete-product.php", hasBody = true)
    @FormUrlEncoded
    Observable<GioHangModel> xoaSanPhamKhoiGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId
    );


    // Đơn hàng
    @POST("api/order/create.php")
    @FormUrlEncoded
    Observable<DonHangModel> createDonHang(
            @Field("sdt") String sdt,
            @Field("email") String email,
            @Field("userId") int userId,
            @Field("tongTien") String tongTien,
            @Field("diaChi") String diaChi,
            @Field("soLuong") int soLuong,
            @Field("chiTiet") String chiTiet
    );

    @GET("api/order/history.php")
    Observable<DonHangModel> xemDonHang(
            @Query("userId") int userId
    );

    @PUT("api/order/update-token.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("token") String token,
            @Field("id") int idDonHang
    );

    // user
    @POST("api/user/register.php")
    @FormUrlEncoded
    Observable<UserModel> dangKy(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

    @POST("api/user/login.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("api/user/reset-password-request.php")
    @FormUrlEncoded
    Observable<MessageModel> guiYeuCauResetMatKhau(
            @Field("email") String email
    );

}
