package top.fksoft.player.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;

import static android.content.Context.WIFI_SERVICE;

public class AndroidUtils {

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }
    /**
     * <p>得到导航栏高度
     * </p>
     *
     * 通过反射得到导航栏的高度
     *
     * @param context  程序上下文
     * @return 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }


    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }
    /**
     * <p>得到Wifi下Ipv4 UDP广播地址</p>
     * @param context 上下文
     * @return udp广播地址
     * @throws IOException 网络异常
     */
    public static InetAddress getIpV4BroadcastAddress(Context context) throws IOException {
        WifiManager myWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpInfo = myWifiManager.getDhcpInfo();
        if (dhcpInfo == null) {
            return null;
        }
        if (isWifiConnected(context)) {
            int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask)
                    | ~dhcpInfo.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++)
                quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            return InetAddress.getByAddress(quads);
        }else {
            return null;
        }
    }


    /**
     * <p>UI 沉浸，对于Android 5.0 以上有效</p>
     * @param activity 需要沉浸的Activity
     */
   public static void immersive(Activity activity){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           View decorView = activity.getWindow().getDecorView();
           int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                   | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
           decorView.setSystemUiVisibility(option);
           activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
           activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
       }

   }

    /**
     * <p>隐藏导航栏和状态栏，实现完全全屏 支持 4.4 及以上</p>
     * @param activity 需要全屏的Activity
     */
    public static void fullscreen(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * <p>判断Wifi是否连接</p>
     * @param context 上下文
     * @return 是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    /**
     * dp转 px
     * @param context 上下文
     * @param dipValue dip值
     * @return px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
