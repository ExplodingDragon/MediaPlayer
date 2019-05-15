package top.fksoft.player.android.config;

import android.support.annotation.NonNull;
import jdkUtils.data.StringUtils;
import org.litepal.crud.LitePalSupport;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.view.SortListView;

import java.io.File;

/**
 * <p>歌曲的实体类</p>
 */
public class SongBean extends LitePalSupport implements SortListView.ListBean {

    private String hashCode;//歌曲的唯一值，由文件路径决定·

    private String songName; //歌曲的名称

    private String songAuthor;//歌曲的歌手信息

    private String songPath;//歌曲的文件位置

    private long  songMillisecond = 0; //歌曲的毫秒数



    public SongBean(File songPath) {
        if (songPath == null)
            throw new NullPointerException("Null File Path");
        this.songPath = songPath.getAbsolutePath();
        hashCode = StringUtils.md5Encryption(this.songPath.toUpperCase().trim()).toUpperCase();
        if (hashCode == null)
            throw new NullPointerException("Can't Encode MD5");
    }

    public String getHashCode() {
        return hashCode;
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public String getSongName() {
        return songName;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public boolean exists(){
        File songPath = getSongPath();
        return songPath == null ? false : songPath.isFile();
    }

    public File getSongPath() {
        return songPath == null?null:new File(songPath);
    }

    public long getSongMillisecond() {
        return songMillisecond;
    }

    public boolean lyricExists(){
        File lyricPath = getSongLyricPath();
        return lyricPath == null?false:lyricPath.isFile();
    }
    public File getSongLyricPath() {
        return new File(FileIO.newInstance().getMusicLrcPath() ,this.hashCode);
    }

    public boolean pictureExists(){
        File picturePath = getSongPicturePath();
        return picturePath == null?false:picturePath.isFile();
    }

    public File getSongPicturePath() {
        return new File(FileIO.newInstance().getMusicPicturePath(),this.hashCode);
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public void setSongMillisecond(long songMillisecond) {
        this.songMillisecond = songMillisecond;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SongBean)) {
            return false;
        }
        return getHashCode().equals(((SongBean) obj).getHashCode());
    }

    @NonNull
    @Override
    public String titleCharArr() {
        return null;
    }
}
