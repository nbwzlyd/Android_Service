package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by lyd10892 on 2016/5/17.
 */
public class Utils {


    public static void startActivity(AppCompatActivity activity, Class cls){
        Intent intent = new Intent();
        intent.setClass(activity,cls);
        activity.startActivity(intent);
    }
    public static void startActivity(AppCompatActivity activity, Class cls, Bundle bundle){
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity,cls);
        activity.startActivity(intent);
    }

    public static<T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 1:大图 720p 2：中图 480p 6：高清图 1080p
     *
     * @param activity
     * @return
     */
    public static int getDisplayMetrics(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int width = mDisplayMetrics.widthPixels;
        int imageSizeType = 1;
        if (width <= 540) {
            imageSizeType = 2;
        } else if (width >= 1080) {
            imageSizeType = 6;
        }
        return imageSizeType;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px 转sp
     *
     * @param the size of text in pixels
     * @return
     * @author yj6299 Aaron
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);// 小数点四舍五入取整
    }
}
