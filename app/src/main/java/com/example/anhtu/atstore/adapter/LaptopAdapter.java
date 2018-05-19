package com.example.anhtu.atstore.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by AnhTu on 5/19/2018.
 */

public class LaptopAdapter extends BaseAdapter{
    //context để biết là adapter sẽ vẽ lên cho màn hình nào
    Context context;
    ArrayList<Sanpham> arraylaptop;

    public LaptopAdapter(Context context, ArrayList<Sanpham> arraylaptop) {
        this.context = context;
        this.arraylaptop = arraylaptop;
    }

    @Override
    public int getCount() {
        return arraylaptop.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylaptop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //tạo viewholder để load loại dữ liệu khi có sự thay đổi
    public  class ViewHolder{
        public TextView txttenlaptop,txtgialaptop,txtmotalaptop;
        public ImageView imglaptop;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //tìm và gán id cho item trong view
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //gán layout vào cho view
            view = inflater.inflate(R.layout.dong_laptop,null);
            viewHolder.txttenlaptop = view.findViewById(R.id.textviewtenlaptop);
            viewHolder.txtgialaptop = view.findViewById(R.id.textviewgialaptop);
            viewHolder.txtmotalaptop = view.findViewById(R.id.textviewmotalaptop);
            viewHolder.imglaptop = view.findViewById(R.id.imageviewlaptop);
            //set id cho view, set tag vào cho view
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        //gán dữ liệu lên cho view
        Sanpham sanpham = (Sanpham) getItem(position);
        viewHolder.txttenlaptop.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgialaptop.setText("Price: " + decimalFormat.format(sanpham.getGiasanpham()) + " Đ");
        viewHolder.txtmotalaptop.setMaxLines(2);
        //định dạng nếu dài quá sẽ hiện dấu ba chấm
        viewHolder.txtmotalaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotalaptop.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imglaptop);
        return view;
    }
}
