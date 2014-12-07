package com.isaplings.travelfriend.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetwork {


    private static final String TAG = CheckNetwork.class.getSimpleName();



    public static boolean isInternetAvailable(Context context)
    {

        Log.v(TAG," MYGPS : Internet Availablity Checking ");
        
        
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
             Log.v(TAG," MYGPS : no internet connection");
             return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.v(TAG," MYGPS :  internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG,"  MYGPS :  internet is not connected");
                return false;
            }

        }
    }
}


//http://stackoverflow.com/questions/2326767/how-do-you-check-the-internet-connection-in-android
//
//
//private boolean isNetworkAvailable()
//{
// ConnectivityManager conxMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
//
// NetworkInfo mobileNwInfo = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
// NetworkInfo wifiNwInfo   = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//return ((mobileNwInfo== null ? false : mobileNwInfo.isAvailable()) || (wifiNwInfo == null ? false : wifiNwInfo.isAvailable()));
//
//}
//
//
//For Data availability if network available
//
//private boolean isDataAvailable()
//{
//  ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//
// NetworkInfo mobileNwInfo = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
// NetworkInfo wifiNwInfo   = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//return ((mobileNwInfo== null? false : mobileNwInfo.isConnected() )|| (wifiNwInfo == null? false : wifiNwInfo.isConnected()));
//}