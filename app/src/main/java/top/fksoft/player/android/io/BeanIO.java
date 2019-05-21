package top.fksoft.player.android.io;

import android.database.Cursor;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.config.SongListBean;
import top.fksoft.player.android.config.SongListDataBean;

import java.util.List;

public class BeanIO {
    private volatile static BeanIO beanIO = null;

    HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();

    public HanyuPinyinOutputFormat getOutputFormat() {
        return outputFormat;
    }

    private BeanIO() {
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
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

    public void delete(SongListBean listBean) {
        String listSQLiteName = listBean.getListSQLiteName();
        LitePal.getDatabase().execSQL("DROP TABLE " + listSQLiteName);
        listBean.delete();
    }

    public void save(SongListBean listBean) {
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
    public int getSQLiteCount(Class<? extends LitePalSupport> clazz) {
        return LitePal.count(clazz);
    }
    public SongListBean getSongListBean(long sqLiteId) {
        return LitePal.find(SongListBean.class, sqLiteId);
    }

    public boolean songExists(SongBean bean){
        String hashCode = bean.getHashCode();
        SongBean first = LitePal.where("hashCode = ?", hashCode).findFirst(SongBean.class);
        return first!=null;
    }

}
