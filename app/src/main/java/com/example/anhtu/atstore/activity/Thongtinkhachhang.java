package com.example.anhtu.atstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anhtu.atstore.R;
import com.example.anhtu.atstore.ultil.CheckConnection;
import com.example.anhtu.atstore.ultil.Server;

import java.util.HashMap;
import java.util.Map;

public class Thongtinkhachhang extends AppCompatActivity {

    EditText edttenkhachhang, edtemail, edtsdt;
    Button btnxacnhan, btntrove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinkhachhang);
        AnhXa();
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            EventButton();
        } else {
            CheckConnection.showToast_Short(getApplicationContext(), "Check your connection");
        }
    }

    //bắt sự kiện click xác nhận
    private void EventButton() {
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edttenkhachhang.getText().toString().trim();
                final String sdt = edtsdt.getText().toString().trim();
                final String email = edtemail.getText().toString().trim();
                if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    //gửi lên server dưới dạng string request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdandonhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("madonhang", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("tenkhachhang", ten);
                            hashMap.put("sodienthoai", sdt);
                            hashMap.put("email", email);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else {
                    CheckConnection.showToast_Short(getApplicationContext(), "Check your data again");
                }
            }
        });
    }

    private void AnhXa() {
        edttenkhachhang = findViewById(R.id.edittexttenkhachhang);
        edtemail = findViewById(R.id.edittextemail);
        edtsdt = findViewById(R.id.edittextsodienthoai);
        btnxacnhan = findViewById(R.id.buttonxacnhan);
        btntrove = findViewById(R.id.buttontrove);
    }
}
