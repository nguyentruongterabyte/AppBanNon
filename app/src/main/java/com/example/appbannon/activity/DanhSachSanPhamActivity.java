package com.example.appbannon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appbannon.R;
import com.example.appbannon.adapter.SanPhamAdapter;
import com.example.appbannon.adapter.SanPhamMoiAdapter;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.retrofit.ApiBanHang;
import com.example.appbannon.retrofit.RetrofitClient;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachSanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerViewDSSanPham;
    ApiBanHang apiBanHang;
    List<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setControl();
        if (isConnected(this)) {
            getDanhSachSanPham();
        }
    }

    private void getDanhSachSanPham() {
        compositeDisposable.add(apiBanHang.getDanhSachSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                mangSanPham = sanPhamModel.getResult();
                                sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                                recyclerViewDSSanPham.setAdapter(sanPhamAdapter);
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void setControl() {
        recyclerViewDSSanPham = findViewById(R.id.recyclerViewDSSanPham);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewDSSanPham.setLayoutManager(layoutManager);
        recyclerViewDSSanPham.setHasFixedSize(true);

        mangSanPham = new ArrayList<>();
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}