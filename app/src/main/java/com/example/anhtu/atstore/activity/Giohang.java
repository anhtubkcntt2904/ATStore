package com.example.anhtu.atstore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.adapter.GiohangAdapter;
import com.example.anhtu.atstore.ultil.CheckConnection;

import java.text.DecimalFormat;

public class Giohang extends AppCompatActivity {
    ListView lvgiohang;
    TextView txtthongbao;
    static TextView txttongtien;
    Button btnthanhtoan, btntieptucmua;
    Toolbar toolbargiohang;
    GiohangAdapter giohangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        AnhXa();
        ActionToolbar();
        CheckData();
        EvenUltil();
        CatchOnItemListView();
        EventButton();
    }

    private void EventButton() {
        //khi click vào button tiếp tục mua hàng, trờ về mainactivity
        btntieptucmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), Thongtinkhachhang.class);
                    startActivity(intent);
                } else {
                    CheckConnection.showToast_Short(getApplicationContext(), "No product in cart");
                }
            }
        });
    }

    //bắt sự kiện khi long click vào item list view trong giỏ hàng để xóa sản phẩm
    private void CatchOnItemListView() {
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Giohang.this);
                builder.setTitle("Confirm delete item");
                builder.setMessage("Are you sure to delete this product from cart?");
                //trường hợp click vào nút yes
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.manggiohang.size() <= 0) {
                            txtthongbao.setVisibility(View.VISIBLE);
                        } else {
                            MainActivity.manggiohang.remove(position);
                            giohangAdapter.notifyDataSetChanged();
                            EvenUltil();
                            if (MainActivity.manggiohang.size() <= 0) {
                                txtthongbao.setVisibility(View.VISIBLE);
                            } else {
                                txtthongbao.setVisibility(View.INVISIBLE);
                                giohangAdapter.notifyDataSetChanged();
                                EvenUltil();
                            }
                        }
                    }
                });
                //nếu click vào không xóa
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohangAdapter.notifyDataSetChanged();
                        EvenUltil();
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    //set lại giá trị tổng tiền khi có thay đổi
    public static void EvenUltil() {
        long tongtien = 0;
        for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
            tongtien += MainActivity.manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtien) + " Đ");
    }

    private void CheckData() {
        //nếu không có sản phẩm nào trong giỏ hàng
        if (MainActivity.manggiohang.size() <= 0) {
            //cập nhật khi có thay đổi
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.VISIBLE);
            lvgiohang.setVisibility(View.INVISIBLE);
        } else {
            //cập nhật khi có thay đổi
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.INVISIBLE);
            lvgiohang.setVisibility(View.VISIBLE);
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbargiohang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbargiohang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        lvgiohang = findViewById(R.id.listviewgiohang);
        txtthongbao = findViewById(R.id.textviewthongbao);
        txttongtien = findViewById(R.id.textviewtongtien);
        btnthanhtoan = findViewById(R.id.buttonthanhtoangiohang);
        btntieptucmua = findViewById(R.id.buttontieptucmuahang);
        toolbargiohang = findViewById(R.id.toolbargiohang);
        giohangAdapter = new GiohangAdapter(Giohang.this, MainActivity.manggiohang);
        lvgiohang.setAdapter(giohangAdapter);
    }
}
