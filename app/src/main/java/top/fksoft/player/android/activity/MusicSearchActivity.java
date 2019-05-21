package top.fksoft.player.android.activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import jdkUtils.io.FileUtils;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.musicSearchActivity.MusicSearch;
import top.fksoft.player.android.activity.musicSearchActivity.SearchSong;
import top.fksoft.player.android.activity.musicSearchActivity.SystemSearch;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.DisplayUtils;
import top.fksoft.player.android.utils.dao.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MusicSearchActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ConstraintLayout searchConfig;
    private CheckBox engine;
    private CheckBox lessThan60;
    private CheckBox traverFolder;
    private Button search;
    private TextView blackList;
    private ListView selectList;
    private CheckBox chooseAll;
    private TextView saveAll;
    private ConstraintLayout loadView;
    private TextView logcat;

    private Handler handler = new Handler();
    private List<SearchSong> list = new ArrayList<>();
    private ListAdapter listAdapter;
    private volatile  boolean acceptDown = true;
    private
    MusicSearch musicSearch;
    private FileIO fileIO = FileIO.newInstance();

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        listAdapter = new ListAdapter();
        bindToolbar(R.id.toolbar);
        setTitle(R.string.searchMusic);
        searchConfig = findViewById(R.id.searchConfig);
        engine = findViewById(R.id.engine);
        lessThan60 = findViewById(R.id.lessThan60);
        traverFolder = findViewById(R.id.traverFolder);
        search = findViewById(R.id.search);
        blackList = findViewById(R.id.blackList);
        selectList = findViewById(R.id.selectList);
        chooseAll = findViewById(R.id.chooseAll);
        saveAll = findViewById(R.id.saveAll);
        loadView = findViewById(R.id.loadView);
        logcat = findViewById(R.id.logcat);
        saveAll.setOnClickListener(this::onClick);
        engine.setOnCheckedChangeListener(this::onCheckedChanged);
        lessThan60.setOnCheckedChangeListener(this::onCheckedChanged);
        traverFolder.setOnCheckedChangeListener(this::onCheckedChanged);
        chooseAll.setOnCheckedChangeListener(this::onCheckedChanged);
        blackList.setOnClickListener(this::onClick);
        search.setOnClickListener(this::onClick);
        searchConfig.setVisibility(View.VISIBLE);
        selectList.setAdapter(listAdapter);


    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_music_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                search();
                break;
            case R.id.saveAll:
                save();
                break;
            case R.id.blackList:
                EditText editText = new EditText(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = layoutParams.rightMargin = DisplayUtils.dip2px(getContext(),10);
                layoutParams.topMargin = layoutParams.bottomMargin= DisplayUtils.dip2px(getContext(),5);
                editText.setLayoutParams(layoutParams);
                File musicSearchBlackList = fileIO.getMusicSearchBlackList();
                String text = FileUtils.fileToString(musicSearchBlackList) ;
                text = text == null ?"":text;
                editText.setText(text);
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.blackList)
                        .setView(editText)
                        .setPositiveButton(R.string.save, (dialog, which) -> {
                            FileUtils.fileToString(musicSearchBlackList);
                            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.exit,null)
                        .show();
                break;

        }
    }

    private void save() {
        loadView.setVisibility(View.VISIBLE);
        logcat.setText(R.string.save);
        new Thread(()->{
            for (SearchSong searchSong : list) {
                    musicSearch.save(searchSong);
            }
            handler.post(()->finish());
        }).start();
    }

    private void search() {
        searchConfig.setVisibility(View.GONE);
        loadView.setVisibility(View.VISIBLE);
        new Thread(() -> {
            if (traverFolder.isChecked()) {//
                musicSearch = MusicSearch.newInstance(getContext(), SystemSearch.class);
            } else {
                musicSearch = MusicSearch.newInstance(getContext(), SystemSearch.class);
            }
            if (lessThan60.isChecked()) {
                musicSearch.breakAMinute();
            }
            SearchSong[] selectList = musicSearch.getSelectList(false);
            musicSearch.clear(list);
            list.addAll(Arrays.asList(selectList));
            list.size();
            handler.post(() -> {
                listAdapter.notifyDataSetChanged();
                loadView.setVisibility(View.GONE);
            });
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicSearch != null) {
            musicSearch.clear(list);
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
            case R.id.chooseAll:
                if (!acceptDown)break;
                for (SearchSong searchSong : list) {
                    if (isChecked) {
                        searchSong.save();
                    } else {
                        searchSong.unSave();
                    }
                }
                listAdapter.notifyDataSetChanged();
                break;
        }
    }

    class ListAdapter extends ArrayAdapter<SearchSong> {

        public ListAdapter() {
            super(MusicSearchActivity.this, R.layout.music_choose_item, list);
        }

        private class ViewHolder {
            View down;
            TextView musicTitle;
            CheckBox index;
            TextView author;
            ImageButton menu;

            public ViewHolder(View convertView) {
                this.down = convertView.findViewById(R.id.down);
                this.musicTitle = convertView.findViewById(R.id.musicTitle);
                this.index = convertView.findViewById(R.id.index);
                this.author = convertView.findViewById(R.id.author);
                this.menu = convertView.findViewById(R.id.menu);
                down.setOnClickListener(v -> index.setChecked(!index.isChecked()));
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_choose_item, parent, false);
                ViewHolder tag = new ViewHolder(convertView);
                convertView.setTag(tag);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            SearchSong searchSong = list.get(position);
            SongBean beanData = searchSong.getBeanData();
            viewHolder.musicTitle.setText(beanData.getSongName());
            viewHolder.author.setText(beanData.getSongAuthor() + " - " + beanData.getSongAlbum());
            viewHolder.index.setOnCheckedChangeListener(null);
            viewHolder.index.setChecked(searchSong.isSave());
            viewHolder.index.setOnCheckedChangeListener((buttonView, isChecked) -> choose(position,isChecked));
            return convertView;
        }

        private void choose(int index, boolean isChecked) {
            if (isChecked) {
                list.get(index).save();
            } else {
                list.get(index).unSave();
            }
            boolean b = true;
            for (SearchSong searchSong : list) {
                if (!searchSong.isSave()) {
                    b = false;
                    break;
                }
            }
            acceptDown = false;
            if (b) {
                chooseAll.setChecked(true);
            } else {
                chooseAll.setChecked(false);
            }
            acceptDown = true;
        }
    }


}
