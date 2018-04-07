package com.pulltozoomview;

import android.widget.ListView;

/**
 * Created by johnli on 2016/3/8.
 */
public interface IScrollerImpl  {
    public void fling(int initialVelocity);
    public void setLockResume(boolean lockResume);
    public void initScroller(ListView listview);

}
