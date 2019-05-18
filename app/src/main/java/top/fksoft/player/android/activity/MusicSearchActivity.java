package top.fksoft.player.android.activity;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.musicSearchActivity.MusicSearch;
import top.fksoft.player.android.activity.musicSearchActivity.SearchSong;
import top.fksoft.player.android.activity.musicSearchActivity.SystemSearch;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.io.BeanIO;
import top.fksoft.player.android.utils.dao.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicSearchActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private CheckBox engine;
    private CheckBox lessThan60;
    private CheckBox traverFolder;
    private Button search;
    private TextView blackList;
    private ListView selectList;
    private TextView logcat;
    private ConstraintLayout searchConfig;
    private ConstraintLayout loadView;

    private List<SearchSong> songs = new ArrayList<>();
    private Handler handler = new Handler();
    private CheckBox chooseAll;
    private TextView saveAll;
    private ListAdapter arrayAdapter;

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
        engine.setOnCheckedChangeListener(this::onCheckedChanged);
        lessThan60.setOnCheckedChangeListener(this::onCheckedChanged);
        traverFolder.setOnCheckedChangeListener(this::onCheckedChanged);
        search.setOnClickListener(this::onClick);
        searchConfig.setVisibility(View.VISIBLE);
        chooseAll = findViewById(R.id.chooseAll);
        saveAll = findViewById(R.id.saveAll);
        arrayAdapter = new ListAdapter(getContext());
        selectList.setAdapter(arrayAdapter);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_music_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                searchConfig.setVisibility(View.GONE);
//                createSearch();
                arrayAdapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (searchConfig.getVisibility() == View.GONE) {
            searchConfig.setVisibility(View.VISIBLE);
            clear();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.traverFolder:
                if (isChecked) {
                    engine.setChecked(true);
                    engine.setEnabled(false);
                } else {
                    engine.setEnabled(true);
                }
                break;
        }
    }


    private void clear() {
        for (SearchSong song : songs) {
            if (song.hasImage()) {
                song.getImagePath().delete();
            }
            if (song.hasLyric()) {
                song.getLyricPath().delete();
            }
        }
        songs.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class ListAdapter extends ArrayAdapter<SearchSong> {
        public ListAdapter(Context context) {
            super(context,R.layout.music_choose_item,songs);
        }

        class ViewHolder{
            TextView mMusicTitle;
            LinearLayout mLayout;
            CheckBox mIndex;
            TextView mAuthor;
            ImageButton mMenu;

            public ViewHolder(View convertView) {
               mMusicTitle = convertView.findViewById(R.id.musicTitle);
               mLayout = convertView.findViewById(R.id.layout);
               mIndex = convertView.findViewById(R.id.index);
               mAuthor = convertView.findViewById(R.id.author);
               mMenu = convertView.findViewById(R.id.menu);
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SearchSong searchSong = songs.get(position);
            SongBean beanData = searchSong.getBeanData();
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_choose_item,parent,false);
                convertView.setTag(new ViewHolder(convertView));
            }
            ViewHolder viewTag = (ViewHolder) convertView.getTag();
            viewTag.mMusicTitle.setText(beanData.getSongName());
            viewTag.mAuthor.setText(beanData.getSongAuthor() + " - " + beanData.getSongAlbum());
            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            loadView.setVisibility(View.VISIBLE);
            new Thread(() -> {
                songs.clear();
                List<SearchSong> tempSongs = new ArrayList<>();
                MusicSearch searchSong = null;
                if (traverFolder.isChecked()) {
                    searchSong = new SystemSearch();
                } else {
                    searchSong = new SystemSearch();//Warn
                }
                searchSong.selectMusic(getContext(), tempSongs, new String[0], true);
                for (SearchSong song : tempSongs) {
                    if (!BeanIO.newInstance().songExists(song.getBeanData())) {
                        songs.add(song);
                    }
                }
                tempSongs.clear();
                handler.post(()->{
                    loadView.setVisibility(View.GONE);
                    super.notifyDataSetChanged();
                });
            }).start();
        }
    }

}
