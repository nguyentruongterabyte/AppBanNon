package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMucModel;
import com.example.appbannon.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("danhmuc.php")
    Observable<DanhMucModel> getDanhMuc();

    @GET("danhsachsanphammoi.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi();

    @POST("danhsachsanpham.php")
    @FormUrlEncoded
    Observable<SanPhamModel> getDanhSachSanPham(
            @Field("page") int page
    );
}
