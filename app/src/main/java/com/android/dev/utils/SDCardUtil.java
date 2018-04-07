package com.android.dev.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * 描述: SDCard工具类
 *
 * @author gongzhenjie
 * @since 2013-7-11 下午4:25:27
 */
public class SDCardUtil {

    /**
     * sdcard
     */
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString();

    /**
     * 应用目录
     */
    public static final String APP_FOLDER = ROOT_DIR + "/shopping/";

    /**
     * 缓存目录
     */
    public static final String CACHE_FOLDER = APP_FOLDER + "cache/";

    /**
     * imageloader缓存文件夹
     */
    public static final String IMAGE_LOADER_FOLDER=APP_FOLDER + "imageloader/";

    /**
     * 下载目录
     */
    public static final String DOWNLOAD_FOLDER = APP_FOLDER + "download/";

    /**
     * 精品应用安装包下载目录
     */
    public static final String NNAPK_DOWNLOAD_FOLDER = DOWNLOAD_FOLDER + "nnapk";

    /**
     * 酷狗应用推荐目录
     */
    public static final String  MARKET_FOLDER = APP_FOLDER + "market/";

    /**
     * 图片文件缓存目录
     */
    public static final String IMAGE_CACHE_FOLDER = CACHE_FOLDER + ".image/";

    /**
     * 分享图片目录
     */
    public static final String  SHARE_FOLDER = APP_FOLDER + "shareImage/";

    public static final String MUSIC_SCAN_CACHE_FOLDER = CACHE_FOLDER + ".music_scan/";

    public static final String PROGRAM_DOWNLOAD_FOLDER = DOWNLOAD_FOLDER + "program/";

    private SDCardUtil() {

    }

    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public static boolean hasSDCard() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * SDCard剩余大小
     *
     * @return 字节
     */
    public static long getAvailableSize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize;
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 格式化 转化为.MB格式
     *
     * @param context
     * @param size
     * @return
     */
    public static String formatSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 检测储存卡是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}