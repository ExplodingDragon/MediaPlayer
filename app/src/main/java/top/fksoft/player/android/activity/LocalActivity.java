package top.fksoft.player.android.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.litepal.LitePal;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.localActivity.AlbumFragment;
import top.fksoft.player.android.activity.localActivity.FolderFragment;
import top.fksoft.player.android.activity.localActivity.LocalFragment;
import top.fksoft.player.android.activity.localActivity.SingerFragment;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.utils.dao.BaseActivity;
import top.fksoft.player.android.utils.dao.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends BaseActivity {


    private TabLayout tabs;
    private ViewPager container;
    private LocalBaseFragment fragments[] =
            new LocalBaseFragment []{
                    new LocalFragment(),
                    new SingerFragment(),
                    new AlbumFragment(),
                    new FolderFragment()
    };
    private List<SongBean> list = new ArrayList<>();

    @Override
    protected void initData() {
        list.clear();
        list.addAll(LitePal.findAll(SongBean.class));
        for (LocalBaseFragment fragment : fragments) {
            fragment.updateListData();
        }
    }

    @Override
    protected void initView() {
        tabs = findViewById(R.id.tabs);
        container = findViewById(R.id.container);
        bindToolbar(R.id.toolbar);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(container));
        setTitle(R.string.localMusic);
        container.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        container.setOffscreenPageLimit(4);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_local;
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.importMusic) {
            startActivity(new Intent(getContext(),MusicSearchActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public List<SongBean> getMusicList(){
        return list;
    }

    public static abstract class  LocalBaseFragment extends BaseFragment<LocalActivity>{
        public List<SongBean> getMusicList(){
            return getContext().getMusicList();
        }
        public  void updateListData(){}
    }
}
