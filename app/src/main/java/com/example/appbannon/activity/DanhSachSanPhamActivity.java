package com.example.appbannon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhSachSanPhamActivity extends AppCompatActivity {

    RecyclerView recyclerViewDSSanPham;
    ApiBanHang apiBanHang;
    List<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Toolbar toolbar;
    LinearLayoutManager layoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setControl();
        ActionToolBar();
        getDanhSachSanPham(page);
        addEventLoad();

    }

    private void addEventLoad() {
        recyclerViewDSSanPham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    // Trong khi user scroll màn hình
                    int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == mangSanPham.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // add null vào mảng để hàm getItemViewType xác định được
                // user kéo đến phần tử cuối cùng
                mangSanPham.add(null);
                sanPhamAdapter.notifyItemInserted(mangSanPham.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                // remove null
                for (int i = mangSanPham.size() - 1; i >= 0; i--) {
                    if (mangSanPham.get(i) == null) {
                        mangSanPham.remove(i);
                        break;
                    }
                }
                sanPhamAdapter.notifyItemRemoved(mangSanPham.size());
                page += 1;
                getDanhSachSanPham(page);
                sanPhamAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDanhSachSanPham(int page) {
        compositeDisposable.add(apiBanHang.getDanhSachSanPham(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                if (sanPhamAdapter == null) {
                                    mangSanPham = sanPhamModel.getResult();
                                    sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                                    recyclerViewDSSanPham.setAdapter(sanPhamAdapter);
                                } else {
                                    int pos = mangSanPham.size() - 1;
                                    int soLuongAdd = sanPhamModel.getResult().size();
                                    for (int i = 0; i < soLuongAdd; i++) {
                                        mangSanPham.add(sanPhamModel.getResult().get(i));
                                    }

                                    sanPhamAdapter.notifyItemRangeInserted(pos, soLuongAdd);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu rồi", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void setControl() {
        recyclerViewDSSanPham = findViewById(R.id.recyclerViewDSSanPham);
        toolbar = findViewById(R.id.toolbar);

//        layoutManager = new GridLayoutManager(this, 2);
        layoutManager = new GridLayoutManager(this, 2);
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