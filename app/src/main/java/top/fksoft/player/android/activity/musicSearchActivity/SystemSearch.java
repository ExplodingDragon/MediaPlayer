package top.fksoft.player.android.activity.musicSearchActivity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import jdkUtils.data.StringUtils;
import jdkUtils.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.LogcatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SystemSearch extends MusicSearch {
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    @Override
    protected void selectMusic(List<SearchSong> beans) {
        Cursor query = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (query == null) {
            return;
        }
        while (query.moveToNext() && !isStop()) {
            String path = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));//文件位置
            SongBean beanData = new SongBean(new File(path));
            SearchSong searchSong = new SearchSong(beanData);
            String name = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));//歌曲名称
            String singer = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));//歌手
            String album = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));//专辑
            long albumId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            long songId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            int duration = query.getInt(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            name = formatName(name);
            beanData.setSongName(name);
            beanData.setSongAuthor(singer);
            beanData.setSongAlbum(album);
            beanData.setSongMillisecond(duration);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("albumId",albumId);
                jsonObject.put("songId",songId);
            } catch (JSONException e) {
                LogcatUtils.w(TAG, "selectMusic: ", e);
            }
            searchSong.setTAG(jsonObject.toString());
            beans.add(searchSong);
        }
    }

    private String formatName(String name) {
        int nameLastIndex = name.lastIndexOf(".");
        if (nameLastIndex != -1) {
            name = name.substring(0,nameLastIndex);
        }
        nameLastIndex = name.lastIndexOf(" - ");
        if (nameLastIndex != -1) {
            name = name.substring(nameLastIndex + 3);
        }
        return name;
    }

    @Override
    protected void delete(SearchSong searchSong) {
        if (searchSong.hasImage()) {
            FileUtils.delete(searchSong.getImagePath());
        }
        if (searchSong.hasLyric()){
            FileUtils.delete(searchSong.getLyricPath());
        }

    }

    @Override
    protected boolean writeData(SearchSong searchSong) {
        String tag = searchSong.getTAG();
        if (tag == null)
        return false;
        try {
            JSONObject jsonObject = new JSONObject(tag);
            long albumId = jsonObject.getLong("albumId");
            long songId = jsonObject.getLong("songId");
            ParcelFileDescriptor fd = null;
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
                fd = getContext().getContentResolver().openFileDescriptor(uri, "r");
            } else {
                Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                fd= getContext().getContentResolver().openFileDescriptor(uri, "r");
            }
            ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(fd);
            File temp = FileIO.newInstance().getTemp();
            File file = new File(temp, "IMG_" + StringUtils.md5Encryption(searchSong.getBeanData().getSongPath().getAbsolutePath()));
            if (file.isDirectory()){
                FileUtils.delete(file);
            }
            if (file.isFile())
                return true;

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(bytes,0, bytes.length))){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            searchSong.setImagePath(file);
        }catch (Exception e){
            Log.w(TAG, "writeData: ",e);
            return false;
        }
        return true;
    }


}
