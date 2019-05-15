package top.fksoft.player.android.activity.settingActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import top.fksoft.player.android.R;

import static android.content.Context.MODE_PRIVATE;

public class SoftPrefFragment extends PreferenceFragmentCompat {

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("soft_set", MODE_PRIVATE);
    }
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getPreferenceManager().setSharedPreferencesName("soft_set");
        addPreferencesFromResource(R.xml.pref_soft_setting);
    }
    public interface Key{
        String UserAuthorization = "UserAuthorization";
        String ShowStatus= "ShowStatus";
        String ShowNavigation = "ShowNavigation";
        String LastWallpaperDay = "LastWallpaperDay";
    }
}
