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
import com.example.appbannon.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> {

    Context context;
    List<SanPham> array;

    public SanPhamAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_san_pham, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = array.get(position);
        holder.tvTen.setText(sanPham.getTenSanPham());

        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.tvGia.setText(String.format("Giá: %sđ", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
        Glide.with(context).load(sanPham.getHinhAnh()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvGia, tvTen;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGia = itemView.findViewById(R.id.item_gia_san_pham);
            tvTen = itemView.findViewById(R.id.item_ten_san_pham);
            image = itemView.findViewById(R.id.item_san_pham_image);
        }
    }
}
