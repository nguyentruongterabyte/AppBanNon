package com.example.appbannon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.activity.ChiTietDonHangActivity;
import com.example.appbannon.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> donHanglist;

    public DonHangAdapter(Context context, List<DonHang> donHanglist) {
        this.context = context;
        this.donHanglist = donHanglist;
    }

    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_don_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = donHanglist.get(position);
        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.tvMaDonHang.setText(String.format("Đơn hàng: %s", donHang.getMaDonHang()));
        holder.tvTotalItems.setText(String.format("%s sản phẩm", donHang.getSoLuong()));
        holder.tvTotalCost.setText(String.format("Thành tiền: đ%s", dft.format(Double.parseDouble(donHang.getTongTien()))));
        holder.tvStatus.setText(donHang.getTrangThai());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerViewChiTietDonHang.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());
        //  adapter chi tiết đơn hàng
        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(context, donHang.getItems());
        holder.recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
        holder.recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);
        holder.recyclerViewChiTietDonHang.setRecycledViewPool(viewPool);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietDonHangActivity.class);
                intent.putExtra("donHang", donHang);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donHanglist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvMaDonHang, tvTotalItems, tvTotalCost, tvStatus;
        RecyclerView recyclerViewChiTietDonHang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang = itemView.findViewById(R.id.maDonHang);
            tvTotalItems = itemView.findViewById(R.id.totalItems);
            tvTotalCost = itemView.findViewById(R.id.totalCost);
            tvStatus = itemView.findViewById(R.id.trangThai);
            recyclerViewChiTietDonHang = itemView.findViewById(R.id.recyclerViewChiTietDonHang);

        }
    }
}
