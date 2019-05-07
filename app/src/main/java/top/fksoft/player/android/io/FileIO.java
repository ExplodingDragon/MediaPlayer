package top.fksoft.player.android.io;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import jdkUtils.io.FileUtils;
import top.fksoft.player.android.SoftApplication;
import top.fksoft.player.android.fragment.SoftPrefFragment;
import top.fksoft.player.android.utils.android.LogcatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileIO {
    private static final String TAG = "FileIO";
    private volatile static FileIO fileIO = null;
    private final Context context;
    private final File temp;//临时目录
    private final File musicRootPath;//音频数据的位置
    private final File musicPicturePath;//音频的图片位置
    private final File musicLrcPath;//音频的歌词位置
    private final File wallpaper;//桌面壁纸的绝对位置
    private final SharedPreferences preferences;//配置文件

    private FileIO(Context context) {
        this.context = context;
        preferences = SoftPrefFragment.getSharedPreferences(context);
        this.temp = new File(context.getExternalCacheDir(), "Temp");
        File externalFilesDir = context.getExternalFilesDir(null);
        this.musicRootPath = new File(externalFilesDir, "MusicCache");
        this.musicPicturePath = new File(musicRootPath, "Picture");
        this.musicLrcPath = new File(musicRootPath, "Lyric");
        this.wallpaper = new File(externalFilesDir, "Wallpaper.png");
    }

    public static FileIO newInstance() {
        if (fileIO == null)
            synchronized (FileIO.class) {
                if (fileIO == null)
                    fileIO = new FileIO(SoftApplication.getContext());
            }
        return fileIO;
    }

    public boolean initEnv() {//初始化操作的文件夹
        File dirs[] = new File[]{temp, musicRootPath, musicPicturePath, musicLrcPath};
        for (File dir : dirs) {
            if (dir.isFile()) {
                if (!dir.delete()) {
                    return false;
                }
                dir.mkdirs();
            } else if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        if (wallpaper.isDirectory()) {
            FileUtils.delete(wallpaper);
        }
        return true;
    }//初始化操作的文件夹


    public boolean writeWallpaper() {
        String last = preferences.getString(SoftPrefFragment.Key.LastWallpaperDay, "0000-00-00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        if (last.equals(today) && wallpaper.isFile()) {
            return true;
        }
        boolean result;
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        Bitmap bitmap = ((BitmapDrawable) wallpaperManager.getDrawable()).getBitmap();
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(wallpaper);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            result = true;
            preferences.edit().putString(SoftPrefFragment.Key.LastWallpaperDay, today).commit();
        } catch (Exception e) {
            LogcatUtils.w(TAG, "writeWallpaper: ", e);
            result = false;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (Exception e) {
            }
        }
        return result;
    }


    public File getWallpaper() {
        return wallpaper;
    } //得到Wallpaper位置

    public File getTemp() {
        return temp;
    }

    public File getMusicRootPath() {
        return musicRootPath;
    }

    public File getMusicPicturePath() {
        return musicPicturePath;
    }

    public File getMusicLrcPath() {
        return musicLrcPath;
    }
}
