package top.fksoft.player.android.config;

import org.litepal.crud.LitePalSupport;
import top.fksoft.player.android.io.FileIO;

import java.io.File;

/**
 * <p>歌单的实体类</p>
 *
 *
 *
 * @NextUpdate 歌单排序
 */
public class SongListBean extends LitePalSupport {

    private String listName;//歌单的名称

    private String imageHash;//图片的哈希值

    private String listSQLiteName;//数据表的名称

    private String imageAnotherHash;//默认图片的哈希值

    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    public  String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListSQLiteName() {
        return listSQLiteName;
    }

    public boolean pictureExists(){
        File imagePath = getImagePath();
        if (imagePath == null) {
            return false;
        }else {
            return imagePath.isFile() && imagePath.length() != 0;
        }
    }

    public File getImagePath() {
        if (imageHash != null){
            return new File(FileIO.newInstance().getMusicPicturePath(), imageHash);
        }else if (imageAnotherHash != null){
            return new File(FileIO.newInstance().getMusicPicturePath(), imageAnotherHash);
        }else {
            return null;
        }
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String hashCode) {
        this.imageHash = hashCode;
    }
    public void setImage2Hash(String hashCode) {
        this.imageAnotherHash = hashCode;
    }
    public void setListSqliteName(String listSqliteName) {
        this.listSQLiteName = listSqliteName;
    }
}
