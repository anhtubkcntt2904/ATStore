package com.example.anhtu.atstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.adapter.DienthoaiAdapter;
import com.example.anhtu.atstore.model.Sanpham;
import com.example.anhtu.atstore.ultil.CheckConnection;
import com.example.anhtu.atstore.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbardt;
    ListView lvdt;
    DienthoaiAdapter dienthoaiAdapter;
    ArrayList<Sanpham> mangdt;
    int iddt = 0;
    int page = 1;
    //sủ dụng progress bar, cho hiển thị ở dưới list view
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        AnhXa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            //lấy id loại sản phẩm cần hiển thị được truyền từ main activity
            GetIdloaisp();
            //bắt sự kiện cho tool bar trong màn hình hiển thị của điện thoại activity
            ActionToolBar();
            //lấy dữ liệu về dựa vào id loại sản phẩm
            GetData(page);
            //bắt sự kiện cho progress bar load thêm dữ liệu
            LoadMoreData();
        } else {
            CheckConnection.showToast_Short(getApplicationContext(), "please check your connection");
            finish();
        }
    }

    private void LoadMoreData() {
        //bắt sự kiện khi click vào các item
        //gửi dữ liệu sang màn hình hiển thị chi tiết sản phẩm
        lvdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                //truyền dưới dạng một Object
                //có thể truyền dạng String
                intent.putExtra("thongtinsanpham", mangdt.get(position));
                startActivity(intent);
            }
        });
        //bắt sự kiện khi scroll list view load thêm sản phẩm điện thoại
        lvdt.setOnScrollListener(new AbsListView.OnScrollListener() {
            //vuốt list view đến một vị trí nào đó
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //khi đang vuốt list view
            @Override
            public void onScroll(AbsListView view, int FirstItem, int VisibleItem, int TotalItem) {
                //vị trí đầu tiên + số lượng có thể nhìn thấy được mà bằng tổng số item => đang ở vị trí cuối cùng
                //và tổng số item != 0
                //không load dữ liệu liên tục
                //chưa load hết dữ liệu
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && !isLoading && !limitadata) {
                    //bắt đầu thread để load dữ liệu
                    isLoading = true;
                    Thread thread = new Thread();
                    thread.start();
                }
            }
        });
    }

    //lấy data để hiển thị lên màn hình
    private void GetData(int Page) {
        //biến để đưa request lên server
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdandienthoai + String.valueOf(Page);
        //đọc về hết tất cả dữ liệu dưới dạng một string
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            //dữ liệu trả về được xử lí trong đây
            @Override
            public void onResponse(String response) {
                //tạo biến để hứng giá trị
                int id = 0;
                String Tendt = "";
                int Giadt = 0;
                String Hinhanhdt = "";
                String Mota = "";
                int Idspdt = 0;
                //response trả về dưới dạng một chuỗi json
                //khi không có dữ liệu thì trả về [] nên check != 2
                if (response != null && response.length() != 2) {
                    lvdt.removeFooterView(footerview);
                    //tạo biến để đọc chuỗi json
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendt = jsonObject.getString("tensp");
                            Giadt = jsonObject.getInt("giasp");
                            Hinhanhdt = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("motasp");
                            Idspdt = jsonObject.getInt("idsanpham");
                            mangdt.add(new Sanpham(id, Tendt, Giadt, Hinhanhdt, Mota, Idspdt));
                            dienthoaiAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitadata = true;
                    lvdt.removeFooterView(footerview);
                    CheckConnection.showToast_Short(getApplicationContext(), "No more data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //đẩy dữ liệu lên server dưới dạng một hashmap
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //hash map với key được gửi lên server để lấy dữ liệu về
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(iddt));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void ActionToolBar() {
        setSupportActionBar(toolbardt);
        //tạo cho action bar một nút home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //bắt sự kiện khi click action bar của màn hình hiển thị điện thoại
        toolbardt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        //get id loại sản phẩm được truyển qua từ mainactivity, nếu không tìm thấy thì trả về giá trị mặc định
        iddt = getIntent().getIntExtra("idloaisanpham", -1);
        Log.d("giatriloaisanpham", iddt + "");
    }

    private void AnhXa() {
        toolbardt = findViewById(R.id.toolbardienthoai);
        lvdt = findViewById(R.id.listviewdienthoai);
        mangdt = new ArrayList<>();
        dienthoaiAdapter = new DienthoaiAdapter(getApplicationContext(), mangdt);
        lvdt.setAdapter(dienthoaiAdapter);
        //gán một layout vào màn hình
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //gán layout cho biến View
        footerview = inflater.inflate(R.layout.progressbar, null);
        mHandler = new mHandler();
    }

    public class mHandler extends Handler {
        //function quản lí các progress gửi lên
        @Override
        public void handleMessage(Message msg) {
            //bắt các msg gửi lên
            switch (msg.what) {
                case 0:
                    //adđ vào thanh progress bar
                    lvdt.addFooterView(footerview);
                    break;
                case 1:
                    //cập nhật và đổ dữ liệu list view lên
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        //thực hiện chạy các luồng bên trong và gửi msg đến Handler để xử lí
        @Override
        public void run() {
            //lần đầu gửi msg sang cho bên mHandler để add footer progress bar
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //muốn liên kết tiếp thì sử dụng obtain messgae
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
