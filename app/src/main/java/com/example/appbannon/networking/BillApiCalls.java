package com.example.appbannon.networking;

import com.example.appbannon.model.HoaDonModel;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BillApiCalls {
    private static final ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    public static void get(int maDonHang, Consumer<HoaDonModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getHoaDon(maDonHang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new HoaDonModel(500, throwable.getMessage()));
                })
        );
    }
}
