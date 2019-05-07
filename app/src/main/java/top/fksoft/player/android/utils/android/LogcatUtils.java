package top.fksoft.player.android.utils.android;

import android.util.Log;

public class LogcatUtils {

    public static void e(String TAG,String message,Throwable e){
        Log.e(TAG, message, e);
    }
    public static void e(String TAG,String message){
        Log.e(TAG, message);
    }
    public static void w(String TAG,String message,Throwable e){
        Log.w(TAG, message, e);
    }
    public static void w(String TAG,String message){
        Log.w(TAG, message);
    }
}
