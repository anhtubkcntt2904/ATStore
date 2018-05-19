package com.example.anhtu.atstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            AnhXa();
            GetIdloaisp();
            ActionToolBar();
            GetData(page);
        }else{
            CheckConnection.showToast_Short(getApplicationContext(), "please check your connection");
            finish();
        }
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
}
