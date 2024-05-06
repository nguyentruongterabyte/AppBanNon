package com.example.appbannon.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class SanPhamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public SanPhamAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            // Nếu có data thì ta hiện lên
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_san_pham, parent, false);
            return new MyViewHolder(item);
        } else {
            // Ngược lại hiện loading
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPham sanPham = array.get(position);
            myViewHolder.tvTen.setText(sanPham.getTenSanPham());

            DecimalFormat dft = new DecimalFormat("###,###,###");
            myViewHolder.tvGia.setText(String.format("đ%s", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
            myViewHolder.tvSoLuong.setText(sanPham.getDaBan() > 0 ? "Đã bán " + sanPham.getDaBan() : "");
            if (sanPham.getHinhAnh().contains("http")) {
                Glide.with(context).load(sanPham.getHinhAnh()).into(myViewHolder.image);
            } else {
                Glide.with(context).load(Utils.BASE_URL + Utils.BASE_IMAGE_URL + "product/" + sanPham.getHinhAnh()).into(myViewHolder.image);
            }

            myViewHolder.setItemClickListener((view, pos, isLongClick) -> {
                if (!isLongClick) {
                    // click vào item
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chiTietSanPham", sanPham);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;


        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvGia, tvTen, tvSoLuong;
        ImageView image;
        CardView cardView;
        private ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.item_ten_san_pham);
            tvGia = itemView.findViewById(R.id.item_gia_san_pham);
            tvSoLuong = itemView.findViewById(R.id.item_so_luong_da_ban);
            image = itemView.findViewById(R.id.item_san_pham_image);
            cardView = itemView.findViewById(R.id.item_san_pham_card_view);

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
