package top.fksoft.player.android.io;

import android.database.Cursor;
import org.litepal.LitePal;
import top.fksoft.player.android.config.SongListBean;
import top.fksoft.player.android.config.SongListDataBean;

import java.util.List;

public class BeanIO {
    private volatile static BeanIO beanIO = null;

    private BeanIO() {
    }
    public static BeanIO newInstance() {
        if (beanIO == null) {
            synchronized (BeanIO.class){
                if (beanIO == null) {
                    beanIO = new BeanIO();
                }
            }
        }
        return beanIO;
    }

    public static void delete(SongListBean listBean) {
        String listSQLiteName = listBean.getListSQLiteName();
        LitePal.getDatabase().execSQL("DROP TABLE " + listSQLiteName);
        listBean.delete();
    }

    public static void save(SongListBean listBean) {
        String listSQLiteName = listBean.getListSQLiteName();
        if (listSQLiteName == null){
            String listSqliteName = "listSQLite_" + System.currentTimeMillis();
            listBean.setListSqliteName(listSqliteName);
            LitePal.getDatabase().execSQL(SongListDataBean.createTable(listSqliteName));
        }
        listBean.save();
    }

    public List<SongListBean> getSongListArray(){
        return LitePal.findAll(SongListBean.class);
    }

    public int getSQLiteCount(String listSQLiteName) {
        Cursor sqLite = LitePal.findBySQL("select * from " + listSQLiteName);
        int count = sqLite.getCount();
        sqLite.close();

        return count;
    }

    public SongListBean getSongListBean(long sqLiteId) {
        return LitePal.find(SongListBean.class, sqLiteId);

    }
}
