package com.example.appbannon.retrofit;


import com.example.appbannon.model.DanhMucModel;
import com.example.appbannon.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("danhmuc.php")
    Observable<DanhMucModel> getDanhMuc();

    @GET("danhsachsanphammoi.php")
    Observable<SanPhamModel> getDanhSachSanPhamMoi();

    @GET("danhsachsanpham.php")
    Observable<SanPhamModel> getDanhSachSanPham();
}
