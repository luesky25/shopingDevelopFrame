
package com.android.dev.framework.component.preference;

import android.os.Bundle;

import java.util.Set;

/**
 * 
 * @author liusensen
 */
public class EditorValues {

    private Bundle mBundle;

    public EditorValues() {
        mBundle = new Bundle();
    }

    public void putString(String key, String value) {
        mBundle.putString(key, value);
    }

    public void putLong(String key, long value) {
        mBundle.putLong(key, value);
    }

    public void putInt(String key, int value) {
        mBundle.putInt(key, value);
    }

    public void putBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
    }

    public void putFloat(String key, float value) {
        mBundle.putFloat(key, value);
    }

    public void putBundle(Bundle bundle) {
        mBundle.putAll(bundle);
    }

    public Object get(String key) {
        return mBundle.get(key);
    }

    public Bundle toBundle() {
        return mBundle;
    }

    public Set<String> keySet() {
        return mBundle.keySet();
    }
}
