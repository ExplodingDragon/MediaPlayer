package top.fksoft.player.android.activity;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import top.fksoft.player.android.R;
import top.fksoft.player.android.utils.dao.BaseActivity;

public class MusicSearchActivity extends BaseActivity {

    private CheckBox engine;
    private CheckBox lessThan60;
    private CheckBox traverFolder;
    private Button search;
    private TextView blackList;
    private ListView selectList;
    private TextView logcat;
    private ConstraintLayout searchConfig;
    private ConstraintLayout loadView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        bindToolbar(R.id.toolbar);
        setTitle(R.string.searchMusic);
        searchConfig = findViewById(R.id.searchConfig);
        engine = findViewById(R.id.engine);
        lessThan60 = findViewById(R.id.lessThan60);
        traverFolder = findViewById(R.id.traverFolder);
        search = findViewById(R.id.search);
        blackList = findViewById(R.id.blackList);
        selectList = findViewById(R.id.selectList);
        loadView = findViewById(R.id.loadView);
        logcat = findViewById(R.id.logcat);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_music_search;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
