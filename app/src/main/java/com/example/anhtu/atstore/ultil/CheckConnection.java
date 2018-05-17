package com.example.anhtu.atstore.ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by AnhTu on 5/16/2018.
 */

//kiểm tra xem đã có kết nối internet chưa
public class CheckConnection {
    public static boolean haveNetworkConnection(Context context) {
        //context : xác định xem màn hình đang xét đến là màn hình nào
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //lấy thông tin network trong thiết bị
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        //kiểm tra nếu có kết nối wifi hoặc mobile thì trả về true
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showToast_Short(Context context, String thongbao) {
        Toast.makeText(context, thongbao, Toast.LENGTH_LONG);
    }
}
