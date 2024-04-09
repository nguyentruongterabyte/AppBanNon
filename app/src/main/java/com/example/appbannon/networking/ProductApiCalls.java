package com.example.appbannon.networking;


import android.util.Log;
import android.widget.Toast;

import com.example.appbannon.adapter.SanPhamAdapter;
import com.example.appbannon.adapter.SanPhamMoiAdapter;
import com.example.appbannon.model.SanPhamModel;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductApiCalls {
    private static final ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    // get 10 sản phẩm mới thêm vào danh sách sản phẩm
    public static void get10(Consumer<List<com.example.appbannon.model.SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
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

    // get sản phẩm trên 1 trang để hiển thị lên màn hình danh sách sản phẩm
    public static void getInAPage(int page, Consumer<List<com.example.appbannon.model.SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPham(page, 6)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                callback.accept(sanPhamModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }

    public static void search(String key, Consumer<List<com.example.appbannon.model.SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamTimKiem(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                callback.accept(sanPhamModel.getResult());
                                Log.d("arraySize=", String.valueOf(sanPhamModel.getResult().size()));
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                        ));
    }
}
