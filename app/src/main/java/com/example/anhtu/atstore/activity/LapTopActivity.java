package com.example.anhtu.atstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.anhtu.atstore.adapter.LaptopAdapter;
import com.example.anhtu.atstore.model.Sanpham;
import com.example.anhtu.atstore.ultil.CheckConnection;
import com.example.anhtu.atstore.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LapTopActivity extends AppCompatActivity {
    //giá trị bên trong layout
    Toolbar toolbarlaptop;
    ListView lvlaptop;
    LaptopAdapter laptopAdapter;
    ArrayList<Sanpham> manglaptop;
    //khi chọn sản phẩm sẽ truyền id sản phẩm qua
    int idlaptop = 0;
    int page = 1;
    //sủ dụng progress bar, cho hiển thị ở dưới list view
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            AnhXa();
            GetIdloaisp();
            ActionToolBar();
            GetData(page);
            LoadMoreData();
        }else{
            CheckConnection.showToast_Short(getApplicationContext(), "please check your connection");
            finish();
        }
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

    private void GetIdloaisp() {
        //get id loại sản phẩm được truyển qua từ mainactivity, nếu không tìm thấy thì trả về giá trị mặc định
        idlaptop = getIntent().getIntExtra("idloaisanpham", -1);
    }

    private void AnhXa() {
        toolbarlaptop = findViewById(R.id.toolbarlaptop);
        lvlaptop = findViewById(R.id.listviewlaptop);
        manglaptop = new ArrayList<>();
        laptopAdapter = new LaptopAdapter(getApplicationContext(), manglaptop);
        lvlaptop.setAdapter(laptopAdapter);
        //gán một layout vào màn hình
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //gán layout cho biến View
        footerview = inflater.inflate(R.layout.progressbar, null);
        mHandler = new mHandler();
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbarlaptop);
        //tạo cho action bar một nút home để back về màn hình cũ
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //bắt sự kiện khi click action bar của màn hình hiển thị điện thoại
        toolbarlaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //lấy data để hiển thị lên màn hình
    private void GetData(int Page) {
        //biến để đưa request lên server
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdandienthoai+String.valueOf(Page);
        //đọc về hết tất cả dữ liệu dưới dạng một string
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            //dữ liệu trả về được xử lí trong đây
            @Override
            public void onResponse(String response) {
                //tạo biến để hứng giá trị
                int id = 0;
                String Tenlaptop = "";
                int Gialaptop = 0;
                String Hinhanhlaptop = "";
                String Motalaptop = "";
                int Idsplaptop = 0;
                //response trả về dưới dạng một chuỗi json
                //khi không có dữ liệu thì trả về [] nên check != 2
                if (response != null && response.length() != 2) {
                    //khi có dữ liệu đổ về thì tắt progress bar
                    lvlaptop.removeFooterView(footerview);
                    //tạo biến để đọc chuỗi json
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenlaptop = jsonObject.getString("tensp");
                            Gialaptop = jsonObject.getInt("giasp");
                            Hinhanhlaptop = jsonObject.getString("hinhanhsp");
                            Motalaptop = jsonObject.getString("motasp");
                            Idsplaptop = jsonObject.getInt("idsanpham");
                            manglaptop.add(new Sanpham(id, Tenlaptop, Gialaptop, Hinhanhlaptop, Motalaptop, Idsplaptop));
                            //cập nhật lại dữ liệu
                            laptopAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //khi hết dữ liệu thì remove progress bar và toast thông báo
                } else {
                    limitadata = true;
                    lvlaptop.removeFooterView(footerview);
                    CheckConnection.showToast_Short(getApplicationContext(), "No more data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //đẩy dữ liệu lên server dưới dạng một hashmap
            //gửi idsanpham đã lấy được từ mainactivity lên server để lấy thông tin sản phẩm
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //hash map với key được gửi lên server để lấy dữ liệu về
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(idlaptop));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreData() {
        //bắt sự kiện khi click vào các item
        //gửi dữ liệu sang màn hình hiển thị chi tiết sản phẩm
        lvlaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                //truyền dưới dạng một Object
                //có thể truyền dạng String
                intent.putExtra("thongtinsanpham", manglaptop.get(position));
                startActivity(intent);
            }
        });
        //bắt sự kiện khi scroll list view load thêm sản phẩm điện thoại
        lvlaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            //vuốt list view đến một vị trí nào đó
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //khi đang vuốt list view
            @Override
            public void onScroll(AbsListView view, int FirstItem, int VisibleItem, int TotalItem) {
                //bắt vị trí cuối cùng
                //vị trí đầu tiên + số lượng có thể nhìn thấy được mà bằng tổng số item => đang ở vị trí cuối cùng
                //và tổng số item != 0
                //không load dữ liệu liên tục
                //chưa load hết dữ liệu
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && !limitadata == false) {
                    //bắt đầu thread để load dữ liệu
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    public class mHandler extends Handler {
        //function quản lí các progress gửi lên
        @Override
        public void handleMessage(Message msg) {
            //bắt các msg gửi lên
            switch (msg.what) {
                case 0:
                    //adđ vào thanh progress bar
                    lvlaptop.addFooterView(footerview);
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
