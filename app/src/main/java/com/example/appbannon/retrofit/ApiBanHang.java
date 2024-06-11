package com.example.appbannon.retrofit;


import com.example.appbannon.model.Address.District;
import com.example.appbannon.model.Address.GenericsBaseResponse;
import com.example.appbannon.model.Address.Province;
import com.example.appbannon.model.Address.Ward;
import com.example.appbannon.model.DanhMuc;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.model.ToaDo;
import com.example.appbannon.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiBanHang {
    // Danh mục
    @GET("api/categories/user")
    Observable<ResponseObject<List<DanhMuc>>> getDanhMuc();


    // Sản phẩm
    @GET("api/product/featured")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPhamMoi(
            @Query("amount") int amount
    );

    @GET("api/product/page")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPham(
            @Query("page") int page,
            @Query("amount") int amount
    );

    @GET("api/product/get")
    Observable<ResponseObject<SanPham>> getSanPham(
            @Query("maSanPham") int maSanPham
    );

    @GET("api/product/search")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPhamTimKiem(
            @Query("key") String key
    );

    @Multipart
    @POST("api/product/upload-image")
    Call<ResponseObject<String>> uploadFile(
            @Part MultipartBody.Part file,
            @Part("maSanPham") RequestBody maSanPham
    );

    // giỏ hàng
    @GET("api/cart/list")
    Observable<ResponseObject<List<GioHang>>> getDanhSachSanPhamTrongGioHang(
            @Query("userId") int userId
    );

    @POST("api/cart/create")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> themSanPhamVaoGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("soLuong") int soLuong
    );

    @PUT("api/cart/update-products")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> updateSanPhamTrongGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId,
            @Field("soLuong") int soLuong
    );

    @HTTP(method = "DELETE", path = "api/cart/delete-product", hasBody = true)
    @FormUrlEncoded
    Observable<ResponseObject<Void>> xoaSanPhamKhoiGioHang(
            @Field("maSanPham") int maSanPham,
            @Field("userId") int userId
    );


    // Đơn hàng
    @POST("api/order/create")
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

    @GET("api/order/history")
    Observable<ResponseObject<List<DonHang>>> xemDonHang(
            @Query("userId") int userId
    );

    @PUT("api/order/update-token")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> updateToken(
            @Field("token") String token,
            @Field("id") int idDonHang
    );

    @PUT("api/order/cancel")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> huyDonMua(
            @Field("maDonHang") int maDonHang
    );

    // user
    @POST("api/user/register")
    @FormUrlEncoded
    Observable<ResponseObject<User>> dangKy(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("mobile") String mobile
    );

    @POST("api/user/refresh-token")
    @FormUrlEncoded
    Call<ResponseObject<String>> refreshToken(
            @Field("refreshToken") String refreshToken
    );

    @POST("api/user/login")
    @FormUrlEncoded
    Observable<ResponseObject<User>> dangNhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("api/user/reset-password-request")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> guiYeuCauResetMatKhau(
            @Field("email") String email
    );

    // Hóa đơn
    @GET("api/bill/get")
    Observable<ResponseObject<DonHang>> getHoaDon(@Query("maDonHang") int maDonHang);

    // Tọa độ
    @GET("api/location/get")
    Observable<ResponseObject<ToaDo>> getToaDo();

    // Đánh giá
    @POST("api/rating/create")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> taoDanhGia(
            @Field("soSao") int soSao,
            @Field("dungVoiMoTa") String dungVoiMoTa,
            @Field("chatLuongSanPham") String chatLuongSanPham,
            @Field("nhanXet") String nhanXet,
            @Field("maDonHang") int maDonHang,
            @Field("hinhAnhDanhGia") String hinhAnhDanhGia
    );

    @Multipart
    @POST("api/rating/upload-image")
    Call<ResponseObject<String>> uploadRatingImage(
            @Part MultipartBody.Part file
    );

    // Địa chỉ
    @GET("api-tinhthanh/1/0.htm")
    Observable<GenericsBaseResponse<List<Province>>> getProvinces();

    @GET("api-tinhthanh/2/{B}.htm")
    Observable<GenericsBaseResponse<List<District>>> getDistricts(@Path("B") String provinceId);

    @GET("api-tinhthanh/3/{B}.htm")
    Observable<GenericsBaseResponse<List<Ward>>> getWards(@Path("B") String districtId);


}
