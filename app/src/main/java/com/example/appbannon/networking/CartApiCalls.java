package com.example.appbannon.networking;

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
    private static final ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    // call api lấy danh sách giỏ hàng
    public static void getAll(Consumer<List<GioHang>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPhamTrongGioHang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gioHangModel -> {
                            if (gioHangModel.isSuccess()) {
                               callback.accept(gioHangModel.getResult());
                               for (int i = 0; i < gioHangModel.getResult().size(); i++) {
                                   System.out.println("san pham " + i + ": " +  gioHangModel.getResult().get(i).toString());
                               }
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
                                gioHang.getTenSanPham(),
                                gioHang.getGiaSanPham(),
                                gioHang.getHinhAnh(),
                                gioHang.getSoLuong()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                gioHangModel -> {
                                    callback.accept(gioHangModel.isSuccess());
                                }, throwable -> {
                                    callback.accept(false);
                                }
                        )
        );
    }
    public static void update(GioHang gioHang, Consumer<Boolean> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.updateSanPhamTrongGioHang(
                        gioHang.getMaSanPham(),
                        gioHang.getGiaSanPham(),
                        gioHang.getSoLuong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gioHangModel -> {
                            callback.accept(gioHangModel.isSuccess());
                        }, throwable -> {
                            callback.accept(false);
                        }
                )
        );
    }
}
