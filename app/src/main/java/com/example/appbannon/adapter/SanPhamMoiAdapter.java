package com.example.appbannon.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbannon.Interface.ItemClickListener;
import com.example.appbannon.R;
import com.example.appbannon.activity.ChiTietActivity;
import com.example.appbannon.model.SanPham;
import com.example.appbannon.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {

    Context context;
    List<SanPham> array;

    public SanPhamMoiAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_san_pham_moi, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPham = array.get(position);
        holder.tvTen.setText(sanPham.getTenSanPham());

        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.tvGia.setText(String.format("đ%s", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
        holder.tvSoLuong.setText(sanPham.getDaBan() > 0 ? "Đã bán " + Utils.formatQuantity(sanPham.getDaBan()) : "");
        if (sanPham.getHinhAnh().contains("http")) {
            Glide.with(context).load(sanPham.getHinhAnh()).into(holder.image);
        } else {
            Glide.with(context).load(Utils.BASE_URL + Utils.BASE_IMAGE_URL + "product/" + sanPham.getHinhAnh()).into(holder.image);
        }
        holder.setItemClickListener((view, pos, isLongClick) -> {
            if (!isLongClick) {
                Intent intent = new Intent(context, ChiTietActivity.class);
                intent.putExtra("maSanPham", sanPham.getMaSanPham());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGia, tvTen, tvSoLuong;
        ImageView image;
        CardView cardView;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGia = itemView.findViewById(R.id.item_gia_san_pham_moi);
            tvTen = itemView.findViewById(R.id.item_ten_san_pham_moi);
            image = itemView.findViewById(R.id.item_san_pham_moi_image);
            cardView = itemView.findViewById(R.id.item_san_pham_moi_card_view);
            tvSoLuong = itemView.findViewById(R.id.item_so_luong_da_ban_moi);
            // Tỉ lệ chiều rộng của 2 sản phẩm trên một hàng luôn bằng nhau
            DisplayMetrics displayMetrics = itemView.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int margin = itemView.getResources().getDimensionPixelSize(R.dimen.product_margin);
            int numOfProductsPerRow = 2;
            int cardViewWidth = (screenWidth - (margin * (numOfProductsPerRow + 1))) / numOfProductsPerRow;
            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
            layoutParams.width = cardViewWidth;
            cardView.setLayoutParams(layoutParams);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
