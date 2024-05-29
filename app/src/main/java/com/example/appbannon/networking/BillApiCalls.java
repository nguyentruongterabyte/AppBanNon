package com.example.appbannon.networking;

import android.content.Context;

import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BillApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    public static void get(int maDonHang, Consumer<ResponseObject<DonHang>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getHoaDon(maDonHang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }
}
