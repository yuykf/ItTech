package com.fungo.xiaokebang.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.utils.ThreadUtils;

import java.io.File;


/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/19.
 */
public class Constants {


    public static final String MY_SHARED_PREFERENCE = "xiaokebang_shared_preference";

    /**
     * Tab colors
     */
    public static final int[] TAB_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };



    /**
     * Intent params
     */
    public static final String ARG_PARAM1 = "param1";

    public static final String ARG_PARAM2 = "param2";

    /**
     * Refresh theme color
     */
    public static final int BLUE_THEME = R.color.colorPrimary;


    /**
     * Main Pager
     */
    public static final String SEARCH_TEXT = "search_text";

    public static final String MENU_BUILDER = "MenuBuilder";

    public static final String LOGIN_DATA = "login_data";

    public static final String BANNER_DATA = "banner_data";

    public static final String ARTICLE_DATA = "article_data";


    /**
     * Avoid double click time area
     */
    public static final long CLICK_TIME_AREA = 1000;

    public static final long DOUBLE_INTERVAL_TIME = 2000;


    public static final String ARTICLE_LINK = "article_link";

    public static final String ARTICLE_TITLE = "article_title";

    public static final String ARTICLE_ID = "article_id";

    public static final String IS_COLLECT = "is_collect";

    public static final String IS_COMMON_SITE = "is_common_site";

    public static final String IS_COLLECT_PAGE = "is_collect_page";

    public static final String CHAPTER_ID = "chapter_id";

    public static final String IS_SINGLE_CHAPTER = "is_single_chapter";

    public static final String CHAPTER_NAME = "is_chapter_name";

    public static final String SUPER_CHAPTER_NAME = "super_chapter_name";

    static final String DB_NAME = "predata.db";

    public static final String CURRENT_PAGE = "current_page";

    public static final String PROJECT_CURRENT_PAGE = "project_current_page";


    /**
     * Shared Preference key
     */
    public static final String DEVICETOKEN = "devicetoken";


    /**
     * Path
     */
    public static final String PATH_DATA = ItTechApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";


    /**
     * Tag fragment_creation classify
     */
    public static final int TYPE_MAIN_WORK = 0;

    public static final int TYPE_MAIN_MINE = 1;

    public static final int TYPE_MAIN_MAKE = 2;

    public static final int TYPE_NAVIGATION = 3;

    public static final int TYPE_PROJECT = 4;

    public static final int TYPE_COLLECT = 5;

    public static final int TYPE_SETTING = 6;


    /**
     * 文件存储相关常量
     */
    public static class SDCardConstants {

        private static final String TAG = "SDCardConstants";
        /**
         * 转码文件后缀
         */
        public final static String TRANSCODE_SUFFIX = ".mp4_transcode";

        /**
         * 裁剪文件后缀
         */
        public final static String CROP_SUFFIX = "-crop.mp4";

        /**
         * 合成文件后缀
         */
        public final static String COMPOSE_SUFFIX = "-compose.mp4";

        /**
         * 裁剪 & 录制 & 转码输出文件的目录
         * android Q 版本默认路径
         * /storage/emulated/0/Android/data/包名/files/Media/
         * android Q 以下版本默认"/sdcard/DCIM/Camera/"
         */
        public static String getDir(Context context) {
            String dir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dir = context.getExternalFilesDir("") + File.separator + "Media" + File.separator;
            } else {
                dir = Environment.getExternalStorageDirectory() + File.separator + "DCIM"
                        + File.separator + "Camera" + File.separator;
            }
            File file = new File(dir);
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            return dir;
        }

        /**
         * 获取外部缓存目录 版本默认"/storage/emulated/0/Android/data/包名/file/Cache/svideo"
         *
         * @param context Context
         * @return string path
         */
        public static String getCacheDir(Context context) {
            File cacheDir = new File(context.getExternalCacheDir(), "svideo");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return cacheDir.exists() ? cacheDir.getPath() : "";
        }

        /**
         * 清空外部缓存目录文件 "/storage/emulated/0/Android/data/包名/file/Cache/svideo"
         *
         * @param context Context
         */
        public static void clearCacheDir(Context context) {
            final File cacheDir = new File(context.getExternalCacheDir(), "svideo");
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    boolean b = deleteFile(cacheDir);
                    Log.i(TAG, "delete cache file " + b);
                }
            });
        }

        /**
         * 递归删除文件/目录
         * @param file File
         */
        private static boolean deleteFile(File file) {
            if (file == null || !file.exists()) {
                return true;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null) {
                    return true;
                }
                for (File f : files) {
                    deleteFile(f);
                }
            }
            return file.delete();
        }

    }


}
