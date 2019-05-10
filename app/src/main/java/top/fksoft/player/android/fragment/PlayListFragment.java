package top.fksoft.player.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.ConfigSongListActivity;
import top.fksoft.player.android.config.SongListBean;
import top.fksoft.player.android.io.BeanIO;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.DisplayUtils;
import top.fksoft.player.android.utils.dao.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends MainFragment {
    private ListView musicCategories;
    private ImageView extendedImage;
    private TextView songList;
    private ListView songArrListView;

    private ListBean[] bean;
    private HeadItemAdapter headItemAdapter;
    private AnimationDrawable frameAnim = null;


    private List<SongListBean> songListBeanArrayList = new ArrayList<>();
    private SongListAdapter songListAdapter = null;

    private boolean showConfig = false;

    @Override
    public int initTitle() {
        return R.string.playList;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_playlist;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        musicCategories = findViewById(R.id.musicCategories);
        extendedImage = findViewById(R.id.extendedImage);
        songList = findViewById(R.id.songList);
        songArrListView = findViewById(R.id.songArrListView);
        bean = new ListBean[]{
                new ListBean(R.mipmap.playlist_local, getString(R.string.playListHeadLocalSong, 0)),
                new ListBean(R.mipmap.playlist_history, getString(R.string.playListHeadHistory, 0)),
                new ListBean(R.mipmap.playlist_like, getString(R.string.playListHeadLike, 0))
        };
        headItemAdapter = new HeadItemAdapter(getContext(), R.layout.playlist_item_song, bean);
        musicCategories.setAdapter(headItemAdapter);
        headItemAdapter.notifyDataSetChanged();
        songList.setText(getString(R.string.songList, 0));
        extendedImage.setOnClickListener(this::onClick);
        songList.setOnClickListener(this::onClick);
        findViewById(R.id.addSongList).setOnClickListener(this::onClick);
        findViewById(R.id.songListConfig).setOnClickListener(this::onClick);

        songListAdapter = new SongListAdapter();
        songArrListView.setAdapter(songListAdapter);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.extendedImage:
            case R.id.songList:
                if (songArrListView.getVisibility() == View.VISIBLE) {
                    songArrListView.setVisibility(View.GONE);
                    frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.arrow_close);
                } else {
                    songArrListView.setVisibility(View.VISIBLE);
                    frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.arrow_open);
                }
                extendedImage.setImageDrawable(frameAnim);
                frameAnim.start();
                break;
            case R.id.addSongList://点击添加按钮
                Intent intent = new Intent(getContext(), ConfigSongListActivity.class);
                intent.putExtra(ConfigSongListActivity.IntentKey_NEW, true);
                startActivity(intent);

                break;
            case R.id.songListConfig://点击歌单管理按钮
                showConfig = !showConfig;
                songListAdapter.notifyDataSetChanged();
                break;

        }
    }

    private void listItemConfig(int position) {
        Intent intent = new Intent(getContext(), ConfigSongListActivity.class);
        intent.putExtra(ConfigSongListActivity.IntentKey_NEW, false);
        intent.putExtra(ConfigSongListActivity.IntentKey_LIST_ID, songListBeanArrayList.get(position).getBaseObjId());
        startActivity(intent);
    }

    private void listItemClicked(int position) {//歌单列表被点击

    }

    private class SongListAdapter extends ArrayAdapter<SongListBean> {
        public SongListAdapter() {
            super(PlayListFragment.this.getContext(), R.layout.playlist_list_item, songListBeanArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_list_item, parent, false);
            }
            SongListBean bean = songListBeanArrayList.get(position);
            ImageView listImage = convertView.findViewById(R.id.list_image);
            LinearLayout onClick = convertView.findViewById(R.id.onClick);
            TextView listTitle = convertView.findViewById(R.id.list_text);
            TextView listTextSize = convertView.findViewById(R.id.list_text_size);
            ImageView itemConfig = convertView.findViewById(R.id.item_delete);
            int px = DisplayUtils.dip2px(getContext(), 60);
            listImage.setImageResource(0);
            if (bean.pictureExists()) {//指定歌单的图片
                listImage.setImageBitmap(FileIO.newInstance().getMusicPictureBitmap(bean.getImageHash(), px, 20));
            } else {
                listImage.setImageResource(R.drawable.img_draw);
            }
            listTitle.setText(bean.getListName());
            listTextSize.setText(getString(R.string.songListItemSize, BeanIO.newInstance().getSQLiteCount(bean.getListSQLiteName())));
            //指定歌单数据的数目
            onClick.setOnClickListener(v -> {
                if (showConfig) {
                    showConfig = false;
                    songListAdapter.notifyDataSetChanged();
                    return;
                }
                listItemClicked(position);
            });//歌单单击事件
            itemConfig.setOnClickListener(v -> listItemConfig(position));//歌单管理事件
            if (showConfig) {
                itemConfig.setVisibility(View.VISIBLE);
            } else {
                itemConfig.setVisibility(View.GONE);
            }
            return convertView;
        }

        public void notifyData() {//刷新数据
            songListBeanArrayList.clear();
            songListBeanArrayList.addAll(BeanIO.newInstance().getSongListArray());
            super.notifyDataSetChanged();
            songList.setText(getString(R.string.songList, songListBeanArrayList.size()));

        }
    } //歌单适配器

    @Override
    public void onStart() {
        super.onStart();
        songListAdapter.notifyData();//刷新数据
    }

    public class HeadItemAdapter extends ArrayAdapter<ListBean> {
        private ListBean[] item;

        public HeadItemAdapter(@NonNull Context context, int resource, ListBean[] item) {
            super(context, resource, item);
            this.item = item;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.playlist_item_song, parent, false);
            ImageView playlistItem1Image = inflate.findViewById(R.id.playListItemImage);
            TextView playlistItem1Text = inflate.findViewById(R.id.playListItemText);
            ListBean listBean = item[position];
            playlistItem1Image.setImageResource(listBean.getResId());
            playlistItem1Text.setText(listBean.getTitle());
            return inflate;
        }

    } //菜单适配器

    public static class ListBean {
        private int resId;
        private String title;

        public ListBean(int resId, String title) {
            this.resId = resId;
            this.title = title;
        }

        public int getResId() {
            return resId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    } //菜单实体类
}
