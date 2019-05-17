package top.fksoft.player.android.activity.musicSearchActivity;

import android.content.Context;
import top.fksoft.player.android.config.SongBean;

import java.util.List;

public interface MusicReader {
    void selectMusic(Context context, List<SearchSong> beans,String[] blackList);
}
