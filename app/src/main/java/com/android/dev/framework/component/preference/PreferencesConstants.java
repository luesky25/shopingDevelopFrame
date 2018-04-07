
package com.android.dev.framework.component.preference;

import android.net.Uri;

public interface PreferencesConstants {

    String AUTHORITY = "ShopPreferences";

    String CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + "/";

    public interface SettingKeyMap {

        Uri CONTENT_URI = Uri.parse(CONTENT_AUTHORITY_SLASH + "setting_key_map");

        String KEY = "key";

        String VALUE = "value";

        String FILEKEY = "filekey";
    }
    
    boolean DEBUG = false;
}
