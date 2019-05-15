package top.fksoft.player.android.activity.localActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.LocalActivity;
import top.fksoft.player.android.utils.dao.BaseFragment;
import top.fksoft.player.android.utils.view.SortListView;


public class LocalFragment extends LocalActivity.LocalBaseFragment {

    private SortListView sortView;
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
        sortView = findViewById(R.id.sortView);
    }

    @Override
    public void onClick(View v) {

    }

}