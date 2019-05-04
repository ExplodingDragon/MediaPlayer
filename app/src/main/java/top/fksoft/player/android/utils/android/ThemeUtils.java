package top.fksoft.player.android.utils.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import top.fksoft.player.android.R;

public class ThemeUtils {
    /**
     *<p>获取ColorPrimary的颜色值</p>
     * @param activity 需要获取的活动
     * @return 十六进制颜色值
     */
    public static int getColorPrimary(Activity activity){
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
    /**
     *<p>获取DarkColorPrimary的颜色值</p>
     * @param activity 需要获取的活动
     * @return 十六进制颜色值
     */
    public static int getDarkColorPrimary(Activity activity){
        TypedValue typedValue = new  TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }
    /**
     *<p>获取ColorAccent的颜色值</p>
     * @param activity 需要获取的活动
     * @return 十六进制颜色值
     */
    public static int getColorAccent(Activity activity){
        TypedValue typedValue = new  TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
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

}