package com.example.appbannon.networking;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbannon.Interface.ImageUploadCallback;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    // get 10 sản phẩm mới thêm vào danh sách sản phẩm
    public static void get10(Consumer<List<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamMoi(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.getStatus() == 200) {
                                callback.accept(sanPhamModel.getResult());

                            } else {
                                callback.accept(new ArrayList<>());

                            }
                        }, throwable -> {

                            callback.accept(new ArrayList<>());
                        }
                )
        );
    }
    // get sản phẩm khi có mã sản phẩm
    public static void getProduct(int productId, Consumer<ResponseObject<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getSanPham(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback, throwable -> {
                            callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                        }
                )
        );
    }

    // get sản phẩm trên 1 trang để hiển thị lên màn hình danh sách sản phẩm
    public static void getInAPage(int page, int amount, Consumer<List<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPham(page, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.getStatus() == 200) {
                                callback.accept(sanPhamModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }

    // Tìm kiếm sản phẩm
    public static void search(String key, Consumer<List<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamTimKiem(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.getStatus() == 200) {
                                callback.accept(sanPhamModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }

    // Upload ảnh
    public static void uploadImage(MultipartBody.Part fileToUpload, int maSanPham, ImageUploadCallback callback) {

        RequestBody maSanPhamBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(maSanPham));
        Call<ResponseObject<String>> call = apiBanHang.uploadFile(fileToUpload, maSanPhamBody);
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

}
