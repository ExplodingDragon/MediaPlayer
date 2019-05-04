package top.fksoft.player.android.utils.android;

import android.app.Activity;
import android.util.TypedValue;
import top.fksoft.player.android.R;

public class ThemeUtils {
    public static int getColorPrimary(Activity activity){
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static int getDarkColorPrimary(Activity activity){
        TypedValue typedValue = new  TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    public static int getColorAccent(Activity activity){
        TypedValue typedValue = new  TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }
}