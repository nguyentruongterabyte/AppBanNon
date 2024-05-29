package com.example.appbannon.networking;

import android.content.Context;

import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.model.ToaDo;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LocationApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    public static void getLocation(Consumer<ResponseObject<ToaDo>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getToaDo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable ->
                {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }
}
