package top.fksoft.player.android.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import top.fksoft.player.android.R;
import top.fksoft.player.android.fragment.SoftPrefFragment;
import top.fksoft.player.android.utils.AndroidUtils;
import top.fksoft.player.android.utils.dao.Animation2Listener;
import top.fksoft.player.android.utils.dao.BaseActivity;

public class StartActivity extends BaseActivity {
    final String[] permission = new String[]
            {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };

    private ImageView imgLogo;
    private TextView message;



    protected void init() {
        boolean authorAllow = getPreferences().getBoolean(SoftPrefFragment.Key.UserAuthorization, false);
        if (!authorAllow) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.authorizationTitle)
                    .setItems(R.array.authorizationMessageArray,null)
                    .setCancelable(false)
                    .setPositiveButton(R.string.allow, (dialog, which) -> {
                        getPreferences().edit().putBoolean(SoftPrefFragment.Key.UserAuthorization,true).commit();
                        requestPermissions(permission);
                    })
                    .setNegativeButton(R.string.disAllow, (dialog, which) -> finish())
                    .setOnCancelListener(dialog -> requestPermissions(permission))
                    .show();
        }else {
            requestPermissions(permission);
        }
    }

    @Override
    public void permissionSuccessful(int length) {
        super.permissionSuccessful(length);
        startActivity(new Intent(getContext(),MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void initData() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation2Listener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                init();
            }
        });
        message.setAnimation(animation);
    }
    @Override
    protected void initView() {
        ignoreNavigationBar();
        ignoreStatusBar();
        AndroidUtils.fullscreen(getContext());
        imgLogo = findViewById(R.id.img_logo);
        message = findViewById(R.id.message);
    }
    @Override
    protected Object initLayout() {
        return R.layout.activity_start;
    }
}
