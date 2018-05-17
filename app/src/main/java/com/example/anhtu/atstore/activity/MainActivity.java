package com.example.anhtu.atstore.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.adapter.LoaispAdapter;
import com.example.anhtu.atstore.adapter.SanphamAdapter;
import com.example.anhtu.atstore.model.Loaisp;
import com.example.anhtu.atstore.model.Sanpham;
import com.example.anhtu.atstore.ultil.CheckConnection;
import com.example.anhtu.atstore.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewmanhinhchinh;
    NavigationView navigationView;
    ListView listViewmanhinhchinh;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    //thông tin loại sản phẩm
    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<Sanpham> mangsanpham;
    SanphamAdapter sanphamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        //kiểm tra kết nối thành công thì thực hiện các hành động tương ứng
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            ActionViewFlipper();
            GetDuLieuLoaisp();
            GetDuLieuSPMoiNhat();
        } else {
            CheckConnection.showToast_Short(getApplicationContext(), "Please check your connection");
            finish();
        }
    }

    private void GetDuLieuSPMoiNhat() {
        //đọc nội dung của đường dẫn url bằng thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //hỗ trợ việc đọc json
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdansanphammoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String Tensanpham = "";
                    Integer Giasanpham = 0;
                    String Hinhanhsanpham = "";
                    String Motasanpham = "";
                    int IDsanpham = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensanpham = jsonObject.getString("tensp");
                            Giasanpham = jsonObject.getInt("giasp");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            Motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void GetDuLieuLoaisp() {
        //đọc nội dung của đường dẫn url bằng thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //hỗ trợ việc đọc json
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongdanLoaisp, new Response.Listener<JSONArray>() {
            //response khi đọc json
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            //lấy dữ liệu động cho điện thoại và laptop
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new Loaisp(id, tenloaisp, hinhanhloaisp));
                            //adapter thực hiện các thay đổi tương ứng
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //fix cứng dữ liệu cho Contact và Info
                    mangloaisp.add(3, new Loaisp(0, "Contact", "http://ggfc.vn/Content/images/QC/BODY-CUC-SOC-GIA-CUC-SOC-500K-THANG.jpg"));
                    mangloaisp.add(4, new Loaisp(0, "Info", "http://ggfc.vn/Content/images/QC/BODY-CUC-SOC-GIA-CUC-SOC-500K-THANG.jpg"));
                }
            }
        }, new Response.ErrorListener() {
            //có lỗi sẽ trả về đây
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.showToast_Short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        //load image from url by picasso
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://2sao.vietnamnetjsc.vn/images/2018/04/29/21/15/st.jpg");
        mangquangcao.add("https://ngocdenroi.com/wp-content/uploads/2015/11/kiem-tien-online-voi-chuong-trinh-affiliate-cua-lazada.jpg");
        mangquangcao.add("http://ggfc.vn/Content/images/QC/BODY-CUC-SOC-GIA-CUC-SOC-500K-THANG.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        //set animation for viewflipper
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setAnimation(animation_slide_in);
        viewFlipper.setAnimation(animation_slide_out);
    }

    //set action bar
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewlipper);
        recyclerViewmanhinhchinh = (RecyclerView) findViewById(R.id.recyclerview);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listViewmanhinhchinh = (ListView) findViewById(R.id.listviewmanhinhchinh);
        //set adapter cho loại sản phẩm
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisp(0, "Main Menu", "http://ggfc.vn/Content/images/QC/BODY-CUC-SOC-GIA-CUC-SOC-500K-THANG.jpg"));
        loaispAdapter = new LoaispAdapter(mangloaisp, getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);
        //set adapter cho sản phẩm
        mangsanpham = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(), mangsanpham);
        recyclerViewmanhinhchinh.setHasFixedSize(true);
        //tạo recyclerview dưới dạng một gridview
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        //set adapter cho recyclerview

    }
}
