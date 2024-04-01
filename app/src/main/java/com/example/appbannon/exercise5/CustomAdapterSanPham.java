package com.example.appbannon.exercise5;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appbannon.R;

import java.util.List;


public class CustomAdapterSanPham extends ArrayAdapter {
    Context context;
    int resource;
    List<SanPham> data;

    public CustomAdapterSanPham(@NonNull Context context, int resource, List<SanPham> data) {
        super(context, resource, data);
        this.data = data;
        this.context = context  ;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(resource, null);
        Button btnChiTiet = convertView.findViewById(R.id.btnChiTiet);
        ImageView ivHinh = convertView.findViewById(R.id.ivHinh);
        TextView tvTenSP = convertView.findViewById(R.id.tvTenSP);

        SanPham sanPham = data.get(position);
        tvTenSP.setText(sanPham.getTenSP());

        switch (sanPham.getLoaiSP()) {
            case "SamSung":
                ivHinh.setImageResource(R.drawable.samsung);
                break;
            case "Iphone":
                ivHinh.setImageResource(R.drawable.iphone);
                break;
            case "Nokia":
                ivHinh.setImageResource(R.drawable.nokia);
                break;
        }

        btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CTSanPhamBai5Activity.class);
                intent.putExtra("sanPham", sanPham);
                context.startActivity(intent);

            }
        });

        return convertView;
    }
}