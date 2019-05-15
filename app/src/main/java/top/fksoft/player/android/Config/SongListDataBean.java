package top.fksoft.player.android.config;


public class SongListDataBean {
    private long id;
    private String musicHash;
    private long musicObjId;

    public SongListDataBean(int id, SongBean bean) {
        this.id = id;
        this.musicHash = bean.getHashCode();
        this.musicObjId = bean.getBaseObjId();
    }

    public long getId() {
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

    public long getMusicObjId() {
        return musicObjId;
    }

    public static String createTable(String tableName) {
        String s = "create table if not exists " + tableName + "(" +
                "_id integer primary key autoincrement," +
                "_musicHash text," +
                "_objId integer" +
                ")";
        return s;
    }
}
