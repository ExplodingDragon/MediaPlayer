package top.fksoft.player.android.activity.localActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.fksoft.player.android.R;
import top.fksoft.player.android.activity.LocalActivity;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.utils.view.SortListView;

import java.util.List;


public class LocalFragment extends LocalActivity.LocalBaseFragment {

    private ListView listView;
    private ListAdapter listAdapter;


    @Override
    protected int initLayout() {
        return R.layout.activity_local_local;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        listAdapter = new ListAdapter(getContext(), getContext().getMusicList());
        listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void updateListData() {
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void onclick(int position) {

    }


    class ListAdapter extends SortListView.SortListAdapter<SongBean>{
        private List<SongBean> objects ;
        public ListAdapter(@NonNull Context context, @NonNull List<SongBean> objects) {
            super(context, R.layout.music_item, objects);
            this.objects = objects;
        }
        private class ViewHolder {
            TextView down;
            TextView musicTitle;
            TextView index;
            TextView author;
            ImageButton menu;

            public ViewHolder(View convertView) {
                this.down = convertView.findViewById(R.id.down);
                this.musicTitle = convertView.findViewById(R.id.musicTitle);
                this.index = convertView.findViewById(R.id.index);
                this.author = convertView.findViewById(R.id.author);
                this.menu = convertView.findViewById(R.id.menu);
            }
        }
        @NonNull
        @Override
        public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_item, parent, false);
                ViewHolder tag = new ViewHolder(convertView);
                convertView.setTag(tag);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.index.setText((position + 1 )+ "");
            SongBean songBean = objects.get(position);
            viewHolder.musicTitle.setText(songBean.getSongName());
            viewHolder.author.setText(songBean.getSongAuthor() + " - " + songBean.getSongAlbum());
            viewHolder.down.setOnClickListener(v->onclick(position));
            return convertView;
        }


    }



}