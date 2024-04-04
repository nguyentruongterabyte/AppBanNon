package com.example.appbannon.adapter;

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
import com.example.appbannon.model.GioHang;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {

    Context context;

    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_gio_hang, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.itemGioHangTenSP.setText(gioHang.getTenSanPham());
        holder.itemGioHangSoLuong.setText(String.valueOf(gioHang.getSoLuong()));
        Glide.with(context).load(gioHang.getHinhAnh()).into(holder.itemGioHangImage);
        DecimalFormat dft = new DecimalFormat("###,###,###");
        // giá của 1 sản phẩm * số lượng
        holder.itemGioHangGiaSP.setText(String.format("Giá: %sđ", dft.format(Double.parseDouble(gioHang.getGiaSanPham()))));
        // giá của 1 sản phẩm
        long gia = Long.parseLong(gioHang.getGiaSanPham()) / gioHang.getSoLuong();
        holder.itemGioHangGia.setText(dft.format(gia));
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView itemGioHangImage;

        TextView itemGioHangGia, itemGioHangTenSP, itemGioHangSoLuong, itemGioHangGiaSP;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGioHangImage = itemView.findViewById(R.id.itemGioHangImage);
            itemGioHangGia = itemView.findViewById(R.id.itemGioHangGia);
            itemGioHangTenSP = itemView.findViewById(R.id.itemGioHangTenSP);
            itemGioHangSoLuong = itemView.findViewById(R.id.itemGioHangSoLuong);
            itemGioHangGiaSP = itemView.findViewById(R.id.itemGioHangGiaSP);

        }
    }
}
