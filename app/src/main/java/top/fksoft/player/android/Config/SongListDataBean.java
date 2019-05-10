package top.fksoft.player.android.config;

import org.litepal.LitePal;

public class SongListDataBean {
    private String id;
    private String musicHash;

    public SongListDataBean(String id, String musicHash) {
        this.id = id;
        this.musicHash = musicHash;
    }

    public String getId() {
        return id;
    }

    public String getMusicHash() {
        return musicHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SongListBean)) {
            return false;
        }
        return musicHash.equals(((SongListDataBean) obj).getMusicHash());
    }

    public static String createTable(String tableName){
        String s = "create table " + tableName + "(" +
                "_id integer primary key autoincrement," +
                "_musicHash text" +
                ")";
        return s;
    }
}
