
package com.android.dev.framework.component.preference;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


/**
 * 配置文件操作类
 * 
 */
public class PreferencesProviderUtil {

    /**
     * 添加
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean put(Context context, String fileName, String key, Object value) {
        ContentValues values = new ContentValues();
        values.put(PreferencesConstants.SettingKeyMap.FILEKEY, fileName);
        values.put(PreferencesConstants.SettingKeyMap.KEY, key);
        values.put(PreferencesConstants.SettingKeyMap.VALUE, String.valueOf(value));
        values.put("TYPE", value != null ? value.getClass().toString() : "NULL");
        try {
            context.getContentResolver().insert(PreferencesConstants.SettingKeyMap.CONTENT_URI, values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量添加
     * 
     * @return
     * @throws Exception
     */
    public static boolean putValues(Context context, String fileName, EditorValues edValues) {
        ContentValues[] values = new ContentValues[edValues.keySet().size()];
        int i = 0;
        for (String key : edValues.keySet()) {
            Object valueobj = edValues.get(key) == null ? "" : edValues.get(key);
            ContentValues value = new ContentValues();
            value.put(PreferencesConstants.SettingKeyMap.FILEKEY, fileName);
            value.put(PreferencesConstants.SettingKeyMap.KEY, key);
            value.put(PreferencesConstants.SettingKeyMap.VALUE, String.valueOf(valueobj));
            value.put("TYPE", valueobj != null ? valueobj.getClass().toString() : "NULL");

            values[i] = value;
            i++;
        }
        try {
            return context.getContentResolver().bulkInsert(PreferencesConstants.SettingKeyMap.CONTENT_URI, values) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取
     * 
     * @param context
     * @param key
     * @return
     */
    private static String get(Context context, String fileName, String key, String defaultValue,
                              String type) {
        // if (KGLog.isDebug() && (fileName == null || key == null || context ==
        // null)) {
        // throw new
        // NullPointerException("fileName or key or context is Null:fileName="
        // + fileName + ",key=" + key + ",context=" + context);
        // }
        Cursor c = context.getContentResolver().query(PreferencesConstants.SettingKeyMap.CONTENT_URI, new String[] {
            PreferencesConstants.SettingKeyMap.VALUE,
        }, "GET", new String[] {
                type, fileName, key, defaultValue
        }, null);
        if (c != null) {
            try {
                if (c.moveToNext()) {
                    return c.getString(0);
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    public static int getInt(Context context, String fileName, String key, int defaultValue) {
        try {
            String value = get(context, fileName, key, String.valueOf(defaultValue),
                    String.valueOf(Integer.class));
            if (value != null) {
                return Integer.valueOf(value);
            }
        } catch (Exception e) {
            Log.e("", "KGSettingUtil--" + e.getMessage() + key + " ; " + fileName);
            return defaultValue;
        }
        return defaultValue;

    }

    public static long getLong(Context context, String fileName, String key, long defaultValue) {
        try {
            String value = get(context, fileName, key, String.valueOf(defaultValue),
                    String.valueOf(Long.class));
            if (value != null) {
                return Long.valueOf(value);
            }
        } catch (Exception e) {
            Log.e("", "KGSettingUtil--" + e.getMessage());
            return defaultValue;
        }
        return defaultValue;
    }

    public static boolean getBoolean(Context context, String fileName, String key,
                                     boolean defaultValue) {
        try {
            String value = get(context, fileName, key, String.valueOf(defaultValue),
                    String.valueOf(Boolean.class));
            if (value != null) {
                return Boolean.valueOf(value);
            }
        } catch (Exception e) {
            Log.e("", "KGSettingUtil--" + e.getMessage());
            e.printStackTrace();
            return defaultValue;
        }
        return defaultValue;
    }

    public static String getString(Context context, String fileName, String key, String defaultValue) {
        try {
            String value = get(context, fileName, key, defaultValue, String.valueOf(String.class));
            if (value != null) {
                return value;
            }
        } catch (Exception e) {
            Log.e("", "KGSettingUtil--" + e.getMessage());
            return defaultValue;
        }
        return defaultValue;
    }

    public static float getFloat(Context context, String fileName, String key, float defaultValue) {
        try {
            String value = get(context, fileName, key, String.valueOf(defaultValue),
                    String.valueOf(Float.class));
            if (value != null) {
                return Float.valueOf(value);
            }
        } catch (Exception e) {
            Log.e("", "KGSettingUtil--" + e.getMessage());
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * 删除
     * 
     * @param context
     * @param key
     * @return
     */
    public static boolean remove(Context context, String fileName, String key) {
        try {
            int count = context.getContentResolver().delete(PreferencesConstants.SettingKeyMap.CONTENT_URI, "REMOVE",
                    new String[] {
                            key, fileName
                    });
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除
     * 
     * @param context
     * @param key
     * @return
     */
    public static boolean clear(Context context, String fileName) {
        try {
            int count = context.getContentResolver().delete(PreferencesConstants.SettingKeyMap.CONTENT_URI, null,
                    new String[] {
                        fileName
                    });
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean contains(Context context, String fileName, String key) {
        Cursor c = context.getContentResolver().query(PreferencesConstants.SettingKeyMap.CONTENT_URI, new String[] {
            PreferencesConstants.SettingKeyMap.VALUE,
        }, "CONTAINS", new String[] {
                fileName, key
        }, null);
        if (c != null) {
            c.close();
            return true;
        }
        return false;
    }

}
