package top.fksoft.player.android.activity.musicSearchActivity;

import android.content.Context;

import java.util.List;

public interface MusicSearch {
    String TAG = "MusicSearch";
    void selectMusic(Context context, List<SearchSong> beans,String[] blackList,boolean createTag);
}
