package com.example.appbannon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.MyViewHolder> {
    Context context;
    List<SanPham> sanPhamList;

    public ChiTietDonHangAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chi_tiet_don_hang, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);
        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.itemTenSPChiTietDonHang.setText(sanPham.getTenSanPham());
        holder.itemSoLuongChiTietDonHang.setText(String.format("x%d", sanPham.getSoLuong()));
        holder.itemGiaChiTietDonHang.setText(dft.format(Double.parseDouble(sanPham.getGiaSanPham())));
        Glide.with(context).load(sanPham.getHinhAnh()).into(holder.itemImageChiTietDonHang);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImageChiTietDonHang;
        TextView itemTenSPChiTietDonHang, itemSoLuongChiTietDonHang, itemGiaChiTietDonHang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageChiTietDonHang = itemView.findViewById(R.id.itemImageChiTietDonHang);
            itemTenSPChiTietDonHang = itemView.findViewById(R.id.itemTenSPChiTietDonHang);
            itemSoLuongChiTietDonHang = itemView.findViewById(R.id.itemSoLuongChiTietDonHang);
            itemGiaChiTietDonHang = itemView.findViewById(R.id.itemGiaChiTietDonHang);
        }
    }
}
