package com.example.appbannon.networking;

import android.content.Context;

import com.example.appbannon.model.DanhMuc;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    // call api lấy danh sách danh mục
    public static void get(Consumer<List<DanhMuc>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhMuc()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        danhMucModel -> {
                            if (danhMucModel.getStatus() == 200) {
                                callback.accept(danhMucModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }
}
