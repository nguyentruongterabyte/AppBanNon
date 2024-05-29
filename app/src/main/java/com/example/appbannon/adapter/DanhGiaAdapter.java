package com.example.appbannon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbannon.R;
import com.example.appbannon.model.DanhGia;

import java.util.List;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.MyViewHolder> {

    Context context;
    List<DanhGia> array;

    public DanhGiaAdapter(Context context, List<DanhGia> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_danh_gia, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DanhGia danhGia = array.get(position);
        holder.username.setText(danhGia.getUsername());
        holder.ratingBar.setRating(danhGia.getSoSao());
        holder.ratingDate.setText(danhGia.getNgayDanhGia());
        holder.productQuality.setText(danhGia.getChatLuongSanPham());
        holder.trueToDescription.setText(danhGia.getDungVoiMoTa());
        holder.comment.setText(danhGia.getNhanXet());

        if (danhGia.getHinhAnhDanhGiaList().size() > 0) {
            holder.imageContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, ratingDate, productQuality, trueToDescription, comment;
        RatingBar ratingBar;

        ImageView image1, image2;

        LinearLayout imageContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            ratingDate = itemView.findViewById(R.id.rating_date);
            trueToDescription = itemView.findViewById(R.id.true_to_description);
            productQuality = itemView.findViewById(R.id.product_quality);
            comment = itemView.findViewById(R.id.comment);

            imageContainer = itemView.findViewById(R.id.image_container);

            ratingBar = itemView.findViewById(R.id.rating_bar);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
        }
    }
}
