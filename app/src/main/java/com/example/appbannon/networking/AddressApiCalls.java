package com.example.appbannon.networking;

import android.content.Context;

import com.example.appbannon.Interface.IResponse;
import com.example.appbannon.model.Address.District;
import com.example.appbannon.model.Address.GenericsBaseResponse;
import com.example.appbannon.model.Address.Province;
import com.example.appbannon.model.Address.Ward;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddressApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance2(Utils.BASE_ADDRESS_URL, context).create(ApiBanHang.class);
    }

    // Lấy danh sách tỉnh
    public static void getProvinces(Consumer<GenericsBaseResponse<List<Province>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getProvinces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> callback.accept(new GenericsBaseResponse<>(2, throwable.getMessage(), "Không lấy được", new ArrayList<>()))
                ));
    }

    // Lấy danh sách huyện theo id tỉnh
    public static void getDistricts(String provinceId, Consumer<GenericsBaseResponse<List<District>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDistricts(provinceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> callback.accept(new GenericsBaseResponse<>(2, throwable.getMessage(), "Không lấy được", new ArrayList<>()))
                ));
    }

    // Lấy danh sách xã theo id tỉnh
    public static void getWards(String districtId, Consumer<GenericsBaseResponse<List<Ward>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getWards(districtId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> callback.accept(new GenericsBaseResponse<>(2, throwable.getMessage(), "Không lấy được", new ArrayList<>()))
                ));
    }

}
