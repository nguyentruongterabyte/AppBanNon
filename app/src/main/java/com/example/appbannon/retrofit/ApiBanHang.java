package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMuc;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.model.ToaDo;
import com.example.appbannon.model.User;

import java.util.List;

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
    Observable<ResponseObject<List<DanhMuc>>> getDanhMuc();


    // Sản phẩm
    @GET("api/product/featured.php")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPhamMoi(
            @Query("amount") int amount
    );

    @GET("api/product/page.php")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPham(
            @Query("page") int page,
            @Query("amount") int amount
    );

    @GET("api/product/search.php")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPhamTimKiem(
            @Query("key") String key
    );

    // giỏ hàng
    @GET("api/cart/list.php")
    Observable<ResponseObject<List<GioHang>>> getDanhSachSanPhamTrongGioHang(
            @Query("userId") int userId
    );

    @POST("api/cart/create.php")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> themSanPhamVaoGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("tenSanPham") String tenSanPham,
            @Field("gia") String gia,
            @Field("hinhAnh") String hinhAnh,
            @Field("soLuong") int soLuong
    );

    @PUT("api/cart/update-products.php")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> updateSanPhamTrongGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("gia") String gia,
            @Field("soLuong") int soLuong
    );

    @HTTP(method = "DELETE", path = "api/cart/delete-product.php", hasBody = true)
    @FormUrlEncoded
    Observable<ResponseObject<Void>> xoaSanPhamKhoiGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId
    );


    // Đơn hàng
    @POST("api/order/create.php")
    @FormUrlEncoded
    Observable<ResponseObject<Integer>> createDonHang(
            @Field("sdt") String sdt,
            @Field("email") String email,
            @Field("userId") int userId,
            @Field("tongTien") String tongTien,
            @Field("diaChi") String diaChi,
            @Field("soLuong") int soLuong,
            @Field("chiTiet") String chiTiet
    );

    @GET("api/order/history.php")
    Observable<ResponseObject<List<DonHang>>> xemDonHang(
            @Query("userId") int userId
    );

    @PUT("api/order/update-token.php")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> updateToken(
            @Field("token") String token,
            @Field("id") int idDonHang
    );

    @PUT("api/order/cancel.php")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> huyDonMua(
            @Field("maDonHang") int maDonHang
    );

    // user
    @POST("api/user/register.php")
    @FormUrlEncoded
    Observable<ResponseObject<User>> dangKy(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

    @POST("api/user/login.php")
    @FormUrlEncoded
    Observable<ResponseObject<User>> dangNhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("api/user/reset-password-request.php")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> guiYeuCauResetMatKhau(
            @Field("email") String email
    );

    // Hóa đơn
    @GET("api/bill/get.php")
    Observable<ResponseObject<DonHang>> getHoaDon(@Query("maDonHang") int maDonHang);

    // Tọa độ
    @GET("api/location/get.php")
    Observable<ResponseObject<ToaDo>> getToaDo();
}
