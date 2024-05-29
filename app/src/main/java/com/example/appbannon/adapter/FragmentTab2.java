package com.example.appbannon.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.model.DonHang;
import com.example.appbannon.networking.OrderApiCalls;
import com.example.appbannon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

// Fragment đơn hàng chờ xác nhận
public class FragmentTab2 extends Fragment {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerViewDonHang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerViewDonHang = view.findViewById(R.id.recyclerViewDonHang);
        recyclerViewDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        getOrders();
        return view;
    }

    private void getOrders() {
        OrderApiCalls.getAll(Utils.currentUser.getId(), donHangModel -> {
            if (donHangModel.getStatus() == 200) {
                List<DonHang> donHangChoXacNhan = new ArrayList<>();
                for (int i = 0; i < donHangModel.getResult().size(); i++) {
                    if (donHangModel.getResult().get(i).getTrangThai().equals("Chờ xác nhận")) {
                        donHangChoXacNhan.add(donHangModel.getResult().get(i));
                    }
                }
                DonHangAdapter adapter = new DonHangAdapter(getContext(), donHangChoXacNhan);
                recyclerViewDonHang.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), donHangModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrderApiCalls.initialize(requireContext());
    }
}
