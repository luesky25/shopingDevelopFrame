package com.android.dev.shop.android.model;

/**
 * 搜索关键字的在匹配字符串的位置信息，包括首位置和末位置
 *
 * @author Administrator
 */
public class KeyWordPos {
    private int start;

    private StringBuffer hightStringBuffer;

    private int end;

    public KeyWordPos(int start, int end) {
        this.end = end;
        this.start = start;
    }

    public KeyWordPos(int start, int end, StringBuffer hightStringBuffer) {
        this.end = end;
        this.start = start;
        this.hightStringBuffer = hightStringBuffer;
    }

    public void setEndPos(int end) {
        this.end = end;
    }

    public void setStartPos(int start) {
        this.start = start;
    }

    public void setHightString(StringBuffer hightStringBuffer) {
        this.hightStringBuffer = hightStringBuffer;
    }

    public int getEndPos() {
        return this.end;
    }

    public int getStartPos() {
        return this.start;
    }

    public StringBuffer getHightString() {
        return this.hightStringBuffer;
    }
}
