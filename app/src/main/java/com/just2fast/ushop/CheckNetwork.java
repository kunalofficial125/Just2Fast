package com.just2fast.ushop;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetwork {
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void isNetworkNotAvailableResult(Context context){
        Dialog cNet = new Dialog(context);
        cNet.setCancelable(false);
        cNet.setContentView(R.layout.net_dialog);
        Log.d("net","Internet not Available");
        cNet.show();
    }
}
