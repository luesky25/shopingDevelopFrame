
package com.android.dev.framework.component.db;

import android.provider.BaseColumns;

/**
 * 描述:基本字段
 *
 * @author ldx
 * @since 2013-7-23 下午12:14:43
 */
public interface AppBaseColumns extends BaseColumns {

    /**
     * 创建时间
     */
    public static final String CREATE_AT = "createAt";

    /**
     * 修改时间
     */
    public static final String MODIFIED_AT = "modifiedAt";

}
