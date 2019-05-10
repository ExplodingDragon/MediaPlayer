package top.fksoft.player.android.utils.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.File;

public class BitmapUtils {


    /**
     * <p>得到Bitmap圆角对象</p>
     * @param bitmap 原始Bitmap
     * @param roundPx 圆角
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPx){

        return getRoundedCornerBitmap(bitmap,roundPx,roundPx,false,0,Color.TRANSPARENT);
    }


    /**
     * 圆角图片
     * @param bitmap 位图
     * @param rx x方向上的圆角半径
     * @param ry y方向上的圆角半径
     * @param bl 是否需要描边
     * @param bl 画笔粗细
     * @param bl 颜色代码
     * @return bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int rx,int ry,boolean bl,int edge,int color) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);//创建画布
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, rx, ry, paint);//绘制圆角矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取相交裁剪
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if(bl) {
            if (color == 0) color = 0xFFFEA248;//默认橘黄色
            paint.setColor(color);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);//描边
            paint.setStrokeWidth(edge);
            canvas.drawRoundRect(rectF, rx, ry, paint);
        }
        return output;
    }

    /**
     * <p>裁切bitmap图片
     * </p>
     * 裁切图片方法，以中心点为标准，
     *
     * @param bitmap bitmap对象
     * @return 裁切后对象
     */
    public static Bitmap cropBitmap(Bitmap bitmap,double outWidth ,double outHeight) {
        double proportion = outWidth/outHeight;
        int srcWidth = bitmap.getWidth(); // 得到图片的宽，高
        int srcHeight = bitmap.getHeight();
        int inSampleSize = 1;
        if (srcWidth > outWidth || srcHeight > outHeight) {
            final int halfHeight = srcHeight / 2;
            final int halfWidth = srcWidth / 2;
            while ((halfHeight / inSampleSize) > outHeight && (halfWidth / inSampleSize) > outWidth) {
                inSampleSize *= 0.5;
            }
        }
        int width = srcWidth * inSampleSize;
        int height = srcHeight * inSampleSize;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        if (scaledBitmap != bitmap && !scaledBitmap.isRecycled()){
            bitmap.recycle();
        }
        return getBitmap(scaledBitmap, proportion);

    }
    /**
     * <p>裁切bitmap图片
     * </p>
     * 裁切图片方法，以中心点为标准，
     *
     * @param bitmap bitmap对象
     * @return 裁切后对象
     */
    public static Bitmap cropBitmap2(Bitmap bitmap,double proportion) {
        return getBitmap(bitmap, proportion);

    }

    private static Bitmap getBitmap(Bitmap bitmap, double proportion) {
        double w = bitmap.getWidth(); // 得到图片的宽，高
        double h = bitmap.getHeight();
        double k = w > h ? h:w;
        int l = (int) (k / proportion);
        return Bitmap.createBitmap(bitmap, ((int)(w/2  - (k/2))), ((int)(h/2  - (l/2))), (int)k, l, null, false);
    }


    /**
     * <p>等比缩放图片大小
     * </p>
     * @param source bitmap对象
     * @param scale 缩放比例
     * @return 缩放后对象
     */
    public static Bitmap createBitmap(Bitmap source,float scale){
        int width = Math.round(source.getWidth() * scale);
        int height = Math.round(source.getHeight() * scale);
        Bitmap outputBitmap = Bitmap.createScaledBitmap(source, width, height, false);
        if (source != outputBitmap && !source.isRecycled()){
            source.recycle();
        }
        return outputBitmap;
    }

    /**
     * <p>将bitmap裁切成圆形</p>
     * @param src 源bitmap
     * @return bitmap对象
     */
    public static Bitmap getOvalBitmap(Bitmap src,int width){
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        return output;
    }



    /**
     * <p>从文件中加载Bitmap
     * </p>
     *
     * @param path 文件位置
     * @param width 相对宽度
     * @param height 相对高度
     * @return 加载的Bitmap对象
     */
    public static Bitmap decodeBitmapFromFile(File path,int width,int height){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(path.getAbsolutePath(), options);
        return createScaleBitmap(src, width, height);
    }

    /**
     * <p>从Resource下打开Bitmap</p>
     * @param res Resource 对象
     * @param resId Res id
     * @param width 相对宽度
     * @param height 相对高度
     * @return
     */
    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(res, resId, options); // 第一次解码，目的是：读取图片长宽
        options.inSampleSize = calculateInSampleSize(options, width, height); // 调用上面定义的方法计算inSampleSize值
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 产生一个稍大的缩略图
        return createScaleBitmap(src, width, height); // 通过得到的bitmap进一步产生目标大小的缩略图
    }



    /**
     * <p>高斯模糊
     * </p>
     * @param context context;
     * @param source 源
     * @param radius  卷积半径
     * @param scale 强度
     * @return 高斯模糊
     */
    public static Bitmap gaussianBlur(Context context, Bitmap source, int radius, float scale){
        Bitmap inputBmp = createBitmap(source,scale);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RenderScript renderScript = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
            final Allocation output = Allocation.createTyped(renderScript, input.getType());
            ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            scriptIntrinsicBlur.setInput(input);
            scriptIntrinsicBlur.setRadius(radius);
            scriptIntrinsicBlur.forEach(output);
            output.copyTo(inputBmp);
            renderScript.destroy();
            return inputBmp;
        }else {
            return fastBlur(inputBmp, radius);
        }
    }
    private static Bitmap fastBlur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        Bitmap dst = cropBitmap(src,dstWidth,dstHeight);
        return dst;
    }
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}