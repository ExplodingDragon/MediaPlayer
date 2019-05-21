package top.fksoft.player.android.activity.musicSearchActivity;

import android.support.annotation.NonNull;
import top.fksoft.player.android.config.SongBean;

import java.io.File;

public class SearchSong {
    private final SongBean beanData; //歌曲的元信息
    private File imagePath = null; //歌曲图片的默认位置
    private File lyricPath = null;//歌词的默认位置
    private boolean save = false;
    private String TAG = null;

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
        return this.imagePath !=null && imagePath.exists();
    }

    public boolean hasLyric(){
        return this.lyricPath !=null && lyricPath.exists();
    }

    @Override
    public boolean equals(Object obj) {
        return beanData.equals(((SearchSong)obj).getBeanData());
    }

    public void save(){
        save = true;
    }

    public boolean isSave() {
        return save;
    }

    public void unSave(){
        save = false;
    }

    @Override
    public int hashCode() {
        return beanData.hashCode();
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }


}
