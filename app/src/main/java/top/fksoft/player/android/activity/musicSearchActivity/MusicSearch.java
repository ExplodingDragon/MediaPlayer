package top.fksoft.player.android.activity.musicSearchActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import jdkUtils.io.FileUtils;
import org.litepal.LitePal;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.File2Utils;

import java.io.File;
import java.util.*;

/**
 * <p>Noob Code！ Don't Learn.
 * </p>
 *
 * @author Dracoally
 * @// TODO: 2019/5/19
 */
public abstract class MusicSearch {
    private Context context;
    protected static String TAG = "MusicSearch";
    private HashSet<String> blackSet = new HashSet<>();
    private int size = blackSet.size();
    private Set<String> musicPath = new HashSet<>();
    private volatile boolean stop = false;
    private boolean breakAMinute = false;

    /**
     * <p>根据规则进行搜索
     * </p>
     * <p>
     * 根据规则进行搜索
     *
     * @param beans 将搜索到数据放入list中，此方法将会在子线程内执行！
     */
    protected abstract void selectMusic(List<SearchSong> beans);

    /**
     * <p>在程序排查过程中，发现重复文件会调用此方法释放占用的资源！</p>
     *
     * @param searchSong
     */
    protected abstract void delete(SearchSong searchSong);

    /**
     * <p>写入音频中的文件信息到临时目录
     * </p>
     *
     * @param searchSong 文件数据
     * @return ...
     */
    protected abstract boolean writeData(SearchSong searchSong);

    /**
     * <p>在循环遍历中可能会突然停止进行搜索
     * </p>
     * <p>
     * 通过此判断是否停止
     *
     * @return
     */
    protected boolean isStop() {
        return stop;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * <p>添加黑名单的方法
     * </p>
     * <p>
     * 添加一个黑名单路径或者绝对位置，不区分大小写
     *
     * @param blackList 黑名单
     */
    public void addBlackList(String... blackList) {
        if (blackSet == null || blackList.length == 0)
            return;
        for (int i = 0; i < blackList.length; i++) {
            String up = blackList[i].toUpperCase().trim();
            blackList[i] = up;
        }
        blackSet.addAll(Arrays.asList(blackList));
        size = blackSet.size();
    }

    /**
     * <p>判断是否为黑名单</p>
     * <p>
     * 将文件的路径传入，判断是为黑名单，不支持正则匹配！
     *
     * @param file 文件实体类
     * @return 是否为黑名单
     */
    public boolean isBlackList(@NonNull File file) {
        if (size == 0) {
            return false;
        }
        String path = file.getAbsolutePath().toUpperCase().trim();
        for (String blackList : blackSet) {
            if (path.startsWith(blackList)) {
                return true;
            }
        }
        return false;
    }

    public void setSongArray(List<SongBean> list) {
        musicPath.clear();
        for (SongBean songBean : list) {
            musicPath.add(songBean.getHashCode());
        }
    }

    /**
     * <p>对List进行搜索去重还有黑名单的</p>
     *
     * @return
     */
    public synchronized SearchSong[] getSelectList(boolean writeData) {
        stop = false;
        List<SearchSong> list = new LinkedList<>();
        selectMusic(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            SearchSong searchSong = list.get(i);
            SongBean beanData = searchSong.getBeanData();
            if (isBlackList(beanData.getSongPath()) ||
                    hasSong(beanData.getHashCode()) ||
                    (breakAMinute && beanData.getSongMillisecond() < 60 * 1000)
            ) {
                delete(searchSong);
                list.remove(i);
            } else {
                if (writeData)
                    writeData(list.get(i));
            }
        }

        return list.toArray(new SearchSong[list.size()]);
    }

    private boolean hasSong(String hashCode) {
        return musicPath.contains(hashCode);
    }

    @Nullable
    public static MusicSearch newInstance(Context context, Class<? extends MusicSearch> clazz) {
        MusicSearch musicSearch = null;
        try {
            musicSearch = (MusicSearch) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        musicSearch.setContext(context);
        musicSearch.setSongArray(LitePal.findAll(SongBean.class));
        musicSearch.addBlackList(File2Utils.file2List(FileIO.newInstance().getMusicSearchBlackList()).toArray(new String[0]));
        return musicSearch;
    }

    public void breakAMinute() {
        this.breakAMinute = true;
    }

    public boolean save(SearchSong searchSong){
        if (!searchSong.isSave()) {
            return false;
        }
        writeData(searchSong);
        SongBean beanData = searchSong.getBeanData();
        FileIO fileIO = FileIO.newInstance();
        if (searchSong.hasImage()) {
            File musicPicturePath = fileIO.getMusicPicturePath(beanData);
            FileUtils.copyFile(searchSong.getImagePath(), musicPicturePath);
        }
        if (searchSong.hasLyric()) {
            File musicLrcPath = fileIO.getMusicLrcPath(beanData);
            FileUtils.copyFile(searchSong.getLyricPath(), musicLrcPath);
        }
        return beanData.save();
    }

    public void clear(List<SearchSong> list){
        for (SearchSong searchSong : list) {
            delete(searchSong);
        }
        list.clear();
    }
}
