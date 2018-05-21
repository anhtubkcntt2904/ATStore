package com.example.anhtu.atstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.model.Giohang;
import com.example.anhtu.atstore.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPham extends AppCompatActivity {
    Toolbar toolbarChitiet;
    ImageView imgChitiet;
    TextView txtten, txtgia, txtmota;
    Spinner spinner;
    Button btndatmua;
    //thông tin sản phẩm hiển thị
    int id = 0;
    String TenChitiet = "";
    int GiaChitiet = 0;
    String HinhanhChitiet = "";
    String MotaChitiet = "";
    int Idsanpham = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        AnhXa();
        ActionToolbar();
        GetInformation();
        CatchEventSpinner();
        EventButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //gắn vào menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //bắt sự kiện khi click vào item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //nếu click vào item giỏ hàng cart thì sang màn hình giỏ hàng
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.anhtu.atstore.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //bắt sự kiện thêm vào giỏ hàng
    private void EventButton() {
        btndatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra số lượng sản phẩm nhập vào spinner
                if (MainActivity.manggiohang.size() > 0) {
                    //kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
                    boolean exists = false;
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                        //nếu sản phẩm đã được thêm vào từ trước
                        if (MainActivity.manggiohang.get(i).getIdsp() == id) {
                            //tăng số lượng sản phâm đó lên
                            MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp() + sl);
                            //nếu số lượng lớn hơn 10
                            if (MainActivity.manggiohang.get(i).getSoluongsp() > 10) {
                                //set lại = 10
                                MainActivity.manggiohang.get(i).setSoluongsp(10);
                            }
                            //set giá mới cho sản phẩm trong giỏ hàng
                            MainActivity.manggiohang.get(i).setGiasp(GiaChitiet * MainActivity.manggiohang.get(i).getSoluongsp());
                            exists = true;
                        }
                    }
                    //nếu sản phẩm chưa được thêm vào từ trước
                    if (exists == false) {
                        //thêm mới vào giỏ hàng
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long Giamoi = soluong * GiaChitiet;
                        MainActivity.manggiohang.add(new Giohang(id, TenChitiet, Giamoi, HinhanhChitiet, soluong));
                    }
                } else {
                    //nếu số lượng <=0
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    //tổng tiền cho sản phẩm trong giỏ hàng
                    long Giamoi = soluong * GiaChitiet;
                    MainActivity.manggiohang.add(new Giohang(id, TenChitiet, Giamoi, HinhanhChitiet, soluong));
                }
                Intent intent = new Intent(getApplicationContext(), com.example.anhtu.atstore.activity.Giohang.class);
                startActivity(intent);
            }
        });
    }

    //bắt sự kiện hiển thị spinner
    private void CatchEventSpinner() {
        Integer[] soluong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(arrayadapter);
    }

    private void GetInformation() {
        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        id = sanpham.getID();
        TenChitiet = sanpham.getTensanpham();
        GiaChitiet = sanpham.getGiasanpham();
        HinhanhChitiet = sanpham.getHinhanhsanpham();
        MotaChitiet = sanpham.getMotasanpham();
        Idsanpham = sanpham.getIDSanpham();
        txtten.setText(TenChitiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtgia.setText("Price : " + decimalFormat.format(GiaChitiet) + " Đ    ");
        txtmota.setText(MotaChitiet);
        Picasso.with(getApplicationContext()).load(HinhanhChitiet)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(imgChitiet);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarChitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChitiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbarChitiet = findViewById(R.id.toolbarchitietsanpham);
        imgChitiet = findViewById(R.id.imageviewchitietsanpham);
        txtten = findViewById(R.id.textviewtenchitietsanpham);
        txtgia = findViewById(R.id.textviewgiachitietsanpham);
        txtmota = findViewById(R.id.textviewmotachitietsanpham);
        spinner = findViewById(R.id.spinner);
        btndatmua = findViewById(R.id.buttondatmua);
    }
}
