package com.example.appbannon.networking;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.appbannon.Interface.ImageUploadCallback;
import com.example.appbannon.model.DanhGia;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    // Upload ảnh
    public static void uploadImage(MultipartBody.Part fileToUpload, ImageUploadCallback callback) {

        Call<ResponseObject<String>> call = apiBanHang.uploadRatingImage(fileToUpload);
        call.enqueue(new Callback<ResponseObject<String>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseObject<String>> call, @NonNull Response<ResponseObject<String>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Upload failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseObject<String>> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // Tạo đánh giá
    public static void create(DanhGia danhGia, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        String jsonHinhAnhDanhGia = danhGia.getHinhAnhDanhGiaList() != null ? new Gson().toJson(danhGia.getHinhAnhDanhGiaList()) : "[]";
        compositeDisposable.add(apiBanHang.taoDanhGia(
                                danhGia.getSoSao(),
                                danhGia.getDungVoiMoTa(),
                                danhGia.getChatLuongSanPham(),
                                danhGia.getNhanXet(),
                                danhGia.getMaDonHang(),
                                jsonHinhAnhDanhGia
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(callback, throwable -> new ResponseObject<>(500, throwable.getMessage()))
        );
    }
}
