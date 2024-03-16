package com.example.appbannon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbannon.R;
import com.example.appbannon.model.SanPham;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {

    List<SanPham> array;
    Context context;

    public SanPhamAdapter(List<SanPham> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        TextView textTenSanPham;
        ImageView ivHinhAnh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_item_san_pham, null);
            viewHolder.textTenSanPham = view.findViewById(R.id.item_ten_san_pham);
            viewHolder.ivHinhAnh = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.textTenSanPham.setText(array.get(i).getTenSanPham());
            Glide.with(context).load(array.get(i).getHinhAnh()).into(viewHolder.ivHinhAnh);
        }
        return view;
    }
}
