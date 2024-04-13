package com.example.appbannon.networking;

import com.example.appbannon.model.DonHang;
import com.example.appbannon.model.DonHangModel;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderApiCalls {
    private static final ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    // Xem đơn hàng
    public static void getAll(int userId, Consumer<DonHangModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.xemDonHang(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new DonHangModel(false, throwable.getMessage()));
                }));
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
                                donHangModel -> {
                                    callback.accept(donHangModel.getMaDonHang());
                                }, throwable -> {
                                    callback.accept(-1);
                                }
                        )
        );
    }
}
