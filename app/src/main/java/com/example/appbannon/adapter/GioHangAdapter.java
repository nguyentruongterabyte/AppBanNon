package com.example.appbannon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbannon.Interface.ImageClickListener;
import com.example.appbannon.R;
import com.example.appbannon.model.EventBus.TinhTongEvent;
import com.example.appbannon.model.GioHang;
import com.example.appbannon.networking.CartApiCalls;
import com.example.appbannon.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {

    Context context;

    List<GioHang> gioHangList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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


        holder.checkBox.setChecked(GioHang.indexOf(Utils.mangMuaHang, gioHang) != -1);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.mangMuaHang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                } else {
                    int index = GioHang.indexOf(Utils.mangMuaHang, gioHang);
                    if (index != -1) {
                        Utils.mangMuaHang.remove(index);
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }
                }
            }
        });
        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giaTri) {
                GioHang gioHangUpdate = gioHangList.get(pos);
                if (giaTri == 1) {
                    // khi giá trị bằng 1 có nghĩa là người dùng nhấn vào nút giảm
                    if (gioHangUpdate.getSoLuong() > 1) {
                        // số lượng mới bằng số lượng cũ trừ đi 1
                        int soLuongMoi = gioHangUpdate.getSoLuong() - 1;

                        // lấy giá cũ lưu vào biến giaCu
                        long giaCu = Long.parseLong(gioHangUpdate.getGiaSanPham());

                        // giá mới = giá thành tiền cũ / số lượng cũ * số lượng mới
                        long giaMoi = giaCu / gioHangUpdate.getSoLuong() * soLuongMoi;

                        // update số lượng mới và giá thành tiền mới
                        gioHangUpdate.setSoLuong(soLuongMoi);
                        gioHangUpdate.setGiaSanPham(String.valueOf(giaMoi));

                        // Gọi api
                        CartApiCalls.update(gioHangUpdate, isSuccess -> {
                            if (isSuccess) {
                                // Nếu update thành công ở database thì
                                // mới thực hiện update ở mảng toàn cục
                                gioHangList.get(pos).setSoLuong(soLuongMoi);
                                holder.itemGioHangSoLuong.setText(String.valueOf(soLuongMoi));
                                holder.itemGioHangGiaSP.setText(String.format("Giá: %s", dft.format(giaMoi)));
                                // Gọi hàm tính tổng tiền bằng event bus bên GioHangActivity
                                EventBus.getDefault().postSticky(new TinhTongEvent());

                            }
                        }, compositeDisposable);
                    } else if (gioHangUpdate.getSoLuong() == 1) {
                        // Khi số lượng về 1 nhưng người dùng vẫn nhấn tiếp
                        // dấu trừ thì thực hiện hỏi người dùng có muốn
                        // xóa sản phẩm khỏi giỏ hàng hay không
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?");

                        // Nếu người dùng nhấn đồng ý thì thực hiện xóa sản phẩm ra khỏi
                        // giỏ hàng
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // gọi api xóa sản phẩm khỏi giỏ hàng
                                CartApiCalls.delete(gioHangUpdate, isSuccess -> {
                                    if (isSuccess) {
                                        int index = GioHang.indexOf(Utils.mangMuaHang, Utils.mangGioHang.get(pos));
                                        if (index != -1) {
                                            Utils.mangMuaHang.remove(index);
                                        }
                                        Utils.mangGioHang.remove(pos);
                                        notifyDataSetChanged();
                                        EventBus.getDefault().postSticky(new TinhTongEvent());
                                    }
                                }, compositeDisposable);
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                } else if (giaTri == 2) {
                    if (gioHangUpdate.getSoLuong() < 11) {
                        int soLuongMoi = gioHangUpdate.getSoLuong() + 1;
                        long giaCu = Long.parseLong(gioHangUpdate.getGiaSanPham());
                        long giaMoi = giaCu / gioHangUpdate.getSoLuong() * soLuongMoi;
                        gioHangUpdate.setSoLuong(soLuongMoi);
                        gioHangUpdate.setGiaSanPham(String.valueOf(giaMoi));
                        // gọi api update sản phẩm
                        CartApiCalls.update(gioHangUpdate, isSuccess -> {
                            if (isSuccess) {
                                gioHangList.get(pos).setSoLuong(soLuongMoi);
                                holder.itemGioHangSoLuong.setText(String.valueOf(soLuongMoi));
                                holder.itemGioHangGiaSP.setText(String.format("Giá: %s", dft.format(giaMoi)));

                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        }, compositeDisposable);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemGioHangImage, itemGioHangTru, itemGioHangCong;

        ImageClickListener listener;

        TextView itemGioHangGia, itemGioHangTenSP, itemGioHangSoLuong, itemGioHangGiaSP;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGioHangImage = itemView.findViewById(R.id.itemGioHangImage);
            itemGioHangGia = itemView.findViewById(R.id.itemGioHangGia);
            itemGioHangTenSP = itemView.findViewById(R.id.itemGioHangTenSP);
            itemGioHangSoLuong = itemView.findViewById(R.id.itemGioHangSoLuong);
            itemGioHangGiaSP = itemView.findViewById(R.id.itemGioHangGiaSP);
            itemGioHangTru = itemView.findViewById(R.id.itemGioHangTru);
            itemGioHangCong = itemView.findViewById(R.id.itemGioHangCong);
            checkBox = itemView.findViewById(R.id.itemGioHangCheck);

            // event click
            itemGioHangCong.setOnClickListener(this);
            itemGioHangTru.setOnClickListener(this);
        }

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (v == itemGioHangTru) {
                listener.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == itemGioHangCong) {
                listener.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }

}
