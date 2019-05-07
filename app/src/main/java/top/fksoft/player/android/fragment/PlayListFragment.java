package top.fksoft.player.android.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import top.fksoft.player.android.R;
import top.fksoft.player.android.utils.dao.MainFragment;

public class PlayListFragment extends MainFragment {
    private ListView musicCategories;
    private ImageView extendedImage;
    private TextView songList;
    private ImageView addSongList;
    private ImageView songListConfig;
    private ListView songArrListView;

    private ListBean[] bean;
    private HeadItemAdapter headItemAdapter;

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
        addSongList = findViewById(R.id.addSongList);
        songListConfig = findViewById(R.id.songListConfig);
        songArrListView = findViewById(R.id.songArrListView);
        bean = new ListBean[]{
                new ListBean(R.mipmap.playlist_local, getString(R.string.playListHeadLocalSong, 0)),
                new ListBean(R.mipmap.playlist_history, getString(R.string.playListHeadHistory, 0)),
                new ListBean(R.mipmap.playlist_like, getString(R.string.playListHeadLike, 0))
        };
        headItemAdapter = new HeadItemAdapter(getContext(), R.layout.playlist_item_song, bean);
        musicCategories.setAdapter(headItemAdapter);
        headItemAdapter.notifyDataSetChanged();

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
