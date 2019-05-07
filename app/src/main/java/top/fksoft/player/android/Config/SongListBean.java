package top.fksoft.player.android.config;

import org.litepal.crud.LitePalSupport;
import top.fksoft.player.android.io.FileIO;

import java.io.File;

/**
 * <p>歌单的实体类</p>
 */
public class SongListBean extends LitePalSupport {

    private String listName;

    private String imagePath;

    private String listSQLiteName;


    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListSqliteName() {
        return listSQLiteName;
    }

    public boolean pictureExists(){
        File imagePath = getImagePath();
        if (imagePath == null) {
            return false;
        }else {
            return imagePath.isFile();
        }
    }

    public File getImagePath() {
        if (imagePath == null) {
            return null;
        }else {
            return new File(FileIO.newInstance().getMusicPicturePath(),imagePath);
        }
    }

    public void setImagePath(String hashCode) {
        this.imagePath = hashCode;
    }

    public void setListSqliteName(String listSqliteName) {
        this.listSQLiteName = listSqliteName;
    }
}
