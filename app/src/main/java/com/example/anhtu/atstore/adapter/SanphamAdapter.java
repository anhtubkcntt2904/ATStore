package com.example.anhtu.atstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by AnhTu on 5/16/2018.
 */

//bắt buộc phải khai báo Viewholder khi dùng recyclerView
public class SanphamAdapter extends RecyclerView.Adapter<SanphamAdapter.ItemHolder> {
    Context context;
    ArrayList<Sanpham> arraysanpham;

    public SanphamAdapter(Context context, ArrayList<Sanpham> arraysanpham) {
        this.context = context;
        this.arraysanpham = arraysanpham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //lấy view trong layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanphammoinhat, null);
        //ánh xạ theo itemHolder
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    //hỗ trợ get set các thuộc tính gắn lên cho layout
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Sanpham sanpham = arraysanpham.get(position);
        holder.txttensanpham.setText(sanpham.getTensanpham());
        //format lại giá trị hiển thị của giá sản phẩm khi hiển thị
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiasanpham.setText("Price: " + decimalFormat.format(sanpham.getGiasanpham()) + " Đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                //trước khi chưa load thì gán cho một hình trước
                .placeholder(R.drawable.noimage)
                //khi bị báo lỗi error
                .error(R.drawable.error)
                //nếu đã có hình ảnh thì đổ vào image view trong holder
                .into(holder.imghinhsanpham);
    }

    @Override
    public int getItemCount() {
        return arraysanpham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imghinhsanpham;
        public TextView txttensanpham, txtgiasanpham;

        //ánh xạ view vào đây
        public ItemHolder(View itemView) {
            super(itemView);
            imghinhsanpham = (ImageView) itemView.findViewById(R.id.imageviewsanpham);
            txtgiasanpham = (TextView) itemView.findViewById(R.id.textviewgiasanpham);
            txttensanpham = (TextView) itemView.findViewById(R.id.textviewtensanpham);
        }
    }
}
