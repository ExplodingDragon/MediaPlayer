package top.fksoft.player.android.activity.localActivity;

import android.view.View;
import android.widget.ListView;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.LocalActivity;


public class LocalFragment extends LocalActivity.LocalBaseFragment {

    private ListView listView;
    HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();


    @Override
    protected int initLayout() {
        return R.layout.activity_local_local;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onClick(View v) {

    }

}