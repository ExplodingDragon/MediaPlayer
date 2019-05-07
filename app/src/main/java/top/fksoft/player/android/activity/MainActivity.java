package top.fksoft.player.android.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import top.fksoft.player.android.R;
import top.fksoft.player.android.fragment.PlayListFragment;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.BitmapUtils;
import top.fksoft.player.android.utils.android.DisplayUtils;
import top.fksoft.player.android.utils.android.ThemeUtils;
import top.fksoft.player.android.utils.dao.BaseActivity;
import top.fksoft.player.android.utils.dao.MainFragment;

import java.io.File;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView menuView;
    private View menuHeader;
    private LinearLayout headerLayout;
    private TextView serverStatus;
    private Bitmap wallpaperBitmap = null;

    private MainFragment fragment = null;
    private MainFragment[] fragments = new MainFragment[]{
            new PlayListFragment(),

    };


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        menuView = findViewById(R.id.menuView);
        menuHeader = menuView.getHeaderView(0);
        headerLayout = menuHeader.findViewById(R.id.menuHeaderLayout);
        serverStatus = headerLayout.findViewById(R.id.serverStatus);
        //FindView
        serverStatus.setText(getString(R.string.serverStatus,getString(R.string.noWorking)));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        menuView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        bindStatus2Navigation(findViewById(R.id.statusBar),findViewById(R.id.navigationBar));
        updateWallpaper();
        setFragment(fragments[0]);
    }



    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.menu_play_list:
                    setFragment(fragments[0]);
                    break;

        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wallpaperBitmap!=null) {
            headerLayout.setBackgroundColor(0);
            if (!wallpaperBitmap.isRecycled()) {
                wallpaperBitmap.recycle();
            }
        }
    }

    private void updateWallpaper() {
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    headerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    headerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int width = DisplayUtils.px2dip(getContext(),headerLayout.getWidth());
                int height = DisplayUtils.px2dip(getContext(),headerLayout.getHeight());
                File wallpaper = FileIO.newInstance().getWallpaper();
                if (wallpaper.isFile()){
                    wallpaperBitmap = BitmapUtils.decodeBitmapFromFile(wallpaper, width, height);
                    BitmapDrawable background = new BitmapDrawable(getResources(), wallpaperBitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        headerLayout.setBackground(background);
                    }else {
                        headerLayout.setBackgroundDrawable(background);
                    }
                }else {
                    headerLayout.setBackgroundColor(ThemeUtils.getColorAccent(getContext()));
                }

            }
        };
        headerLayout.getViewTreeObserver().addOnGlobalLayoutListener(listener);

    }//更新菜单上壁纸

    @Override
    protected Object initLayout() {
        return R.layout.activity_main;
    }

    private void setFragment(MainFragment fragment) {
        if (fragment == null)
            return;
        this.fragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        setTitle(fragment.initTitle());
    } //指定碎片布局

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    } //修复菜单如果打开
}
