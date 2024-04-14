package com.example.appbannon.networking;


import com.example.appbannon.model.MessageModel;
import com.example.appbannon.model.User;
import com.example.appbannon.model.UserModel;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserApiCalls {
    private static final ApiBanHang apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

    // Đăng kí tài khoản
    public static void register(User user, Consumer<UserModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.dangKy(user.getEmail(), user.getPassword(), user.getUsername(), user.getMobile())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new UserModel(false, throwable.getMessage()));
                })
        );
    }

    // Đăng nhập
    public static void login(String email, String password, Consumer<UserModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.dangNhap(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new UserModel(false, throwable.getMessage()));
                })
        );
    }

    // Gửi yêu cầu reset mật khẩu
    public static void requestResetPassword(String email, Consumer<MessageModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.guiYeuCauResetMatKhau(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new MessageModel(false, throwable.getMessage()));
                }));
    }
}
