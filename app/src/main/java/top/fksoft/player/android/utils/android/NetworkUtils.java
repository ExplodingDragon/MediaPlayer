package top.fksoft.player.android.utils.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.InetAddress;

import static android.content.Context.WIFI_SERVICE;

public class NetworkUtils {
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
     * <p>判断Wifi是否连接</p>
     * @param context 上下文
     * @return 是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

}
