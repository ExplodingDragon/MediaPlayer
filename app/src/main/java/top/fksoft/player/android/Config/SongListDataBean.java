package top.fksoft.player.android.config;


public class SongListDataBean {
    private String id;
    private String musicHash;
    private String musicObjId;

    public SongListDataBean(String id, String musicHash, String musicObjId) {
        this.id = id;
        this.musicHash = musicHash;
        this.musicObjId = musicObjId;
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

    public String getMusicObjId() {
        return musicObjId;
    }

    public static String createTable(String tableName){
        String s = "create table if not exists" + tableName + "(" +
                "_id integer primary key autoincrement," +
                "_musicHash text" +
                ")";
        return s;
    }
}
