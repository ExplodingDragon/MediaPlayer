package top.fksoft.player.android.utils;

import android.util.Log;
import jdkUtils.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class File2Utils extends FileUtils {
    private static final String TAG = "File2Utils";

    public static List<String> file2List(File file) {
        if (!file.isFile() || !file.canRead()) {
            return new ArrayList<>();
        }
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while (null != (line = reader.readLine())) {
                arrayList.add(line);
            }
        } catch (Exception e) {
            arrayList.clear();
            Log.w(TAG, "file2List: ",e);
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }catch (Exception e){}
        }

        return arrayList;
    }
    public static boolean list2File ( List<String> list,File file ){
        if (file.isDirectory()) {
            delete(file);
        }
        boolean result = false;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(file), true);
            for (String line : list) {
                printWriter.println(line);
            }
            result = true;
        }catch (Exception e){
            result = false;
        }finally {
            try{
                if (printWriter != null) {
                    printWriter.close();
                }
            }catch (Exception e){}
        }
        return result;
    }
}
