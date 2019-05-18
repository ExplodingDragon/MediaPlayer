package top.fksoft.player.android.activity.musicSearchActivity;

import android.support.annotation.NonNull;
import top.fksoft.player.android.config.SongBean;

import java.io.File;

public class SearchSong {
    private final SongBean beanData; //歌曲的元信息
    private File imagePath = null; //歌曲图片的默认位置
    private File lyricPath = null;//歌词的默认位置

    public SearchSong(@NonNull SongBean beanData) {
        this.beanData = beanData;
    }

    public SongBean getBeanData() {
        return beanData;
    }

    public File getImagePath() {
        return imagePath;
    }

    public File getLyricPath() {
        return lyricPath;
    }

    public void setImagePath(File imagePath) {
        this.imagePath = imagePath;
    }

    public void setLyricPath(File lyricPath) {
        this.lyricPath = lyricPath;
    }

    public boolean hasImage(){
        return this.imagePath !=null;
    }

    public boolean hasLyric(){
        return this.lyricPath !=null;
    }

    @Override
    public boolean equals(Object obj) {
        return beanData.equals(((SearchSong)obj).getBeanData());
    }

}
