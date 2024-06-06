package com.example.appbannon.networking;

import android.content.Context;

import com.example.appbannon.model.GioHang;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartApiCalls {
    private static ApiBanHang apiBanHang;

    public static void initialize(Context context) {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiBanHang.class);
    }

    // call api lấy danh sách giỏ hàng
    public static void getAll(int userId, Consumer<List<GioHang>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamTrongGioHang(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gioHangModel -> {
                            if (gioHangModel.getStatus() == 200) {
                                callback.accept(gioHangModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }

    // call api thêm sản phẩm vào giỏ hàng
    public static void add(GioHang gioHang, Consumer<Boolean> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.themSanPhamVaoGioHang(
                                gioHang.getMaSanPham(),
                                gioHang.getUserId(),
                                gioHang.getSoLuong()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                gioHangModel -> {
                                    callback.accept(gioHangModel.getStatus() == 200);
                                }, throwable -> {
                                    callback.accept(false);
                                }
                        )
        );
    }

    // call api cập nhật giá và số lượng của sản phẩm trong giỏ hàng
    public static void update(GioHang gioHang, Consumer<Boolean> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.updateSanPhamTrongGioHang(
                        gioHang.getMaSanPham(),
                        gioHang.getUserId(),
                        gioHang.getSoLuong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gioHangModel -> {
                            callback.accept(gioHangModel.getStatus() == 200);
                        }, throwable -> {
                            callback.accept(false);
                        }
                )
        );
    }

    // call api xóa sản phẩm khỏi giỏ hàng
    public static void delete(int maSanPham, int userId, Consumer<Boolean> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.xoaSanPhamKhoiGioHang(maSanPham, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gioHangModel -> {
                            callback.accept(gioHangModel.getStatus() == 200);
                        }, throwable -> {
                            callback.accept(false);
                        }
                )
        );

    }


}
