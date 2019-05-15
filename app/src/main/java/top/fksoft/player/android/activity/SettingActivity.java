package top.fksoft.player.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.settingActivity.SoftPrefFragment;
import top.fksoft.player.android.utils.dao.BaseActivity;

public class SettingActivity extends BaseActivity {

    public static void start(Activity context) {
        context.startActivity(new Intent(context,SettingActivity.class));
    }

    @Override
    protected void initData() {
        setTitle(R.string.set);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new SoftPrefFragment()).commit();
    }

    @Override
    protected void initView() {
        bindToolbar(R.id.toolbar);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_set;
    }

    @Override
    public void onClick(View v) {

    }


}
