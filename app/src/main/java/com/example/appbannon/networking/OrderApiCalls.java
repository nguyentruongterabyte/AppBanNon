package com.example.appbannon.networking;

import android.content.Context;
import android.util.Log;

import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.ResponseObject;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    // Xem đơn hàng
    public static void getAll(int userId, Consumer<ResponseObject<List<DonHang>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.xemDonHang(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable ->
                        callback.accept(new ResponseObject<>(500, throwable.getMessage()))
                ));
    }

    public static void updateToken(String token, int idDonHang, Consumer<Boolean> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.updateToken(token, idDonHang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            callback.accept(messageModel.getStatus() == 200);
                        }, throwable -> {
                            callback.accept(false);
                        }
                ));
    }

    // Tạo đơn hàng
    public static void create(DonHang donHang, Consumer<Integer> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.createDonHang(
                                donHang.getSdt(),
                                donHang.getEmail(),
                                donHang.getUserId(),
                                donHang.getTongTien(),
                                donHang.getDiaChi(),
                                donHang.getSoLuong(),
                                donHang.getChiTiet()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                donHangModel ->
                                        callback.accept(donHangModel.getResult())
                                , throwable -> {
                                    Log.d("myLog", throwable.getMessage());
                                    callback.accept(-1);
                                }


                        )
        );
    }

    // Hủy đơn mua
    public static void cancel(int maDonHang, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.huyDonMua(maDonHang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback, throwable -> new ResponseObject<>(500, throwable.getMessage())
                )
        );
    }
}
