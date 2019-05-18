package top.fksoft.player.android.activity.musicSearchActivity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import jdkUtils.data.StringUtils;
import top.fksoft.player.android.config.SongBean;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.LogcatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SystemSearch implements MusicSearch {
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    @Override
    public void selectMusic(Context context, List<SearchSong> beans, String[] blackLists,boolean createTag) {
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (query == null) {
            return;
        }
        while (query.moveToNext()) {
            String path = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));//文件位置
            for (String blackList : blackLists) {
                if (path.startsWith(blackList)) {
                    continue;
                }
            }
            SongBean beanData = new SongBean(new File(path));
            SearchSong searchSong = new SearchSong(beanData);
            if (!createTag){
                beans.add(searchSong);
                return;
            }

            String name = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));//歌曲名称
            String singer = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));//歌手
            String album = query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));//专辑
            long albumId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            long songId = query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            int duration = query.getInt(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            int nameLastIndex = name.lastIndexOf(".");
            if (nameLastIndex != -1) {
                name = name.substring(0,nameLastIndex);
            }
            nameLastIndex = name.lastIndexOf(" - ");
            if (nameLastIndex != -1) {
                name = name.substring(nameLastIndex + 3);
            }
            beanData.setSongName(name);
            beanData.setSongAuthor(singer);
            beanData.setSongAlbum(album);
            beanData.setSongMillisecond(duration);
            try {
                ParcelFileDescriptor fd = null;
                if (albumId < 0) {
                    Uri uri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
                    fd = context.getContentResolver().openFileDescriptor(uri, "r");
                } else {
                    Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);
                    fd= context.getContentResolver().openFileDescriptor(uri, "r");
                }
                ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(fd);
                File temp = FileIO.newInstance().getTemp();
                File file = new File(temp, "IMG_" + StringUtils.md5Encryption(path));
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while (-1 != (len = inputStream.read(bytes,0, bytes.length))){
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
                }
                outputStream.close();
                searchSong.setImagePath(file);
            } catch (Exception e) { //歌单专辑保存
                LogcatUtils.w(TAG,"MediaImageSave",e);
            }
            beans.add(searchSong);
        }
    }
}
