package top.fksoft.player.android.utils.dao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import top.fksoft.player.android.R;
import top.fksoft.player.android.fragment.SoftPrefFragment;
import top.fksoft.player.android.utils.android.DisplayUtils;
import top.fksoft.player.android.utils.android.ThemeUtils;

import java.util.ArrayList;

import static top.fksoft.player.android.fragment.SoftPrefFragment.Key.ShowNavigation;
import static top.fksoft.player.android.fragment.SoftPrefFragment.Key.ShowStatus;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_CODE = 1;
    private SharedPreferences preferences;
    private View rootLayout;
    private TextView statusBar;
    private LinearLayout rootViewContainer;
    private TextView navigationBar;
    private boolean ignoreStatusBar = false,ignoreNavigationBar = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = SoftPrefFragment.getSharedPreferences(getContext());
        Object o = initLayout();
        if (o instanceof View){
            rootLayout = (View) o;
        }else{
            rootLayout = View.inflate(getContext(),(Integer) o,null);
        }
        rootViewContainer = new LinearLayout(getContext());
        rootViewContainer.setFitsSystemWindows(false);
        rootViewContainer.setOrientation(LinearLayout.VERTICAL);
        statusBar = new TextView(getContext());
        navigationBar = new TextView(getContext());
        statusBar.setVisibility(View.GONE);
        navigationBar.setVisibility(View.GONE);
        //指定背景颜色
        int darkColorPrimary = ThemeUtils.getColorAccent(getContext());
        statusBar.setBackgroundColor(darkColorPrimary);
        navigationBar.setBackgroundColor(darkColorPrimary);
        //指定背景颜色 end
        rootViewContainer.addView(statusBar,0);
        rootViewContainer.addView(rootLayout,1);
        rootViewContainer.addView(navigationBar,2);
        setContentView(rootViewContainer);

        //设置导航栏状态栏高度
        int displayWidth = DisplayUtils.getDisplayWidth(getContext());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
        params.height = DisplayUtils.getStatusBarHeight(getContext());
        params.width = displayWidth;
        statusBar.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) navigationBar.getLayoutParams();
        params.height = DisplayUtils.getNavigationBarHeight(getContext());
        params.width = displayWidth;
        navigationBar.setLayoutParams(params);
        //设置导航栏状态栏高度 end;
        //处理内部布局的高度
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rootLayout.getLayoutParams();
        layoutParams.weight = 1;
        layoutParams.height = 0;
        rootLayout.setLayoutParams(layoutParams);
        //处理内部布局的高度 End
        initView();//初始化控件绑定
        initData();//初始化数据处理
        ThemeUtils.immersive(getContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!ignoreStatusBar && !preferences.getBoolean(ShowStatus,false)){
                statusBar.setVisibility(View.VISIBLE);
            }else {
                statusBar.setVisibility(View.GONE);
            }
            if (!ignoreNavigationBar && !preferences.getBoolean(ShowNavigation, false)) {
                navigationBar.setVisibility(View.VISIBLE);
            }else {
                navigationBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * <p>重新指定状态栏和导航栏的遮罩控件，适应其他布局</p>
     * @param statusBar 状态栏
     * @param navigationBar 导航栏v
     */
    public void bindStatus2Navigation(TextView statusBar,TextView navigationBar){
        this.statusBar = statusBar;
        this.navigationBar = navigationBar;
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (rootLayout == null){
            return super.findViewById(id);
        }else {
            return rootLayout.findViewById(id);
        }
    }

    public int requestPermissions(String[] permissions){
        if (permissions == null) {
            return -1;
        }
        ArrayList<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        int size = permissionList.size();
        if (size !=0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), REQUEST_PERMISSION_CODE);
        }else {
            permissionSuccessful(0);
        }
        permissionList.clear();
        return size;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            ArrayList<String> arrayList = new ArrayList<>(permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    arrayList.add(permissions[i]);
                }
            }
            if (arrayList.size() == 0) {
                permissionSuccessful(permissions.length);
            } else {
                permissionError(arrayList.toArray(new String[0]));
            }
        }
    }

    /**
     * <p>授权失败的回调方法
     * </p>
     * @param failArray 未允许的权限
     */
    public void permissionError(String[] failArray){
        Toast.makeText(getContext(), R.string.allowPermissionFail,Toast.LENGTH_SHORT).show();
        startActivity(getAppDetailSettingIntent());
        finish();
    }

    /**
     * <p>授权成功后调用的方法</p>
     * @param length 成功的个数
     */
    public void permissionSuccessful(int length){
        if (length!=0)
        Toast.makeText(getContext(), R.string.allowPermissionSuccessful,Toast.LENGTH_SHORT).show();
    }

    //Get and Set


    public Activity getContext(){
        return this;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    //Get and Set #End

    protected abstract void initData();
    protected abstract void initView();
    protected abstract Object initLayout();

    /**
     * <p>Android 跳转到设置界面
     * </p>
     * @return 设置的Intent
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    /**
     * 忽略通知栏遮罩（强制不显示）
     */
    public void ignoreStatusBar(){
        ignoreStatusBar = true;
    }

    /**
     * 忽略导航栏遮罩（强制不显示）
     */
    public void ignoreNavigationBar(){
        ignoreNavigationBar = true;
    }

}
