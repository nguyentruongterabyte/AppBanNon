package com.example.appbannon.retrofit;


import com.example.appbannon.model.SanPhamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanHang {
    @GET("danhsachsanpham.php")
    Observable<SanPhamModel> getDanhSachSP();
}
