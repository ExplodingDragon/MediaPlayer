package top.fksoft.player.android.config;

import android.support.annotation.NonNull;
import jdkUtils.data.StringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
import top.fksoft.player.android.io.BeanIO;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.LogcatUtils;
import top.fksoft.player.android.utils.view.SortListView;

import java.io.File;

/**
 * <p>歌曲的实体类</p>
 */
public class SongBean extends LitePalSupport implements SortListView.ListBean {
    private static final String TAG = "SongBean";

    @Column(unique = true)
    private String hashCode;//歌曲的唯一值，由文件路径决定·

    @Column(nullable = false)
    private String songName; //歌曲的名称

    @Column(nullable = false)
    private String songNameChars;//歌曲的char字符串

    private String songAuthor;//歌曲的歌手信息

    private String songAlbum;//歌曲的专辑

    @Column(nullable = false)
    private String songPath;//歌曲的文件位置

    private long songMillisecond = 0; //歌曲的毫秒数


    public SongBean(@NonNull File songPath) {
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

    public boolean exists() {
        File songPath = getSongPath();
        return songPath == null ? false : songPath.isFile();
    }

    public File getSongPath() {
        return songPath == null ? null : new File(songPath);
    }

    public long getSongMillisecond() {
        return songMillisecond;
    }

    public boolean lyricExists() {
        File lyricPath = getSongLyricPath();
        return lyricPath == null ? false : lyricPath.isFile();
    }

    public File getSongLyricPath() {
        return new File(FileIO.newInstance().getMusicLrcPath(), this.hashCode);
    }

    public boolean pictureExists() {
        File picturePath = getSongPicturePath();
        return picturePath == null ? false : picturePath.isFile();
    }

    public File getSongPicturePath() {
        return new File(FileIO.newInstance().getMusicPicturePath(), this.hashCode);
    }

    public void setSongName(@NonNull String songName) {
        this.songName = songName;
        String chars = "#Unknown";
        try {
            chars = PinyinHelper.toHanYuPinyinString(songName, BeanIO.newInstance().getOutputFormat(), "", true)
                    .replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        } catch (Exception e) {
            LogcatUtils.w(TAG, "Warn!!!", e);
        }
        this.songNameChars = chars;
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
        return songNameChars;
    }

}
