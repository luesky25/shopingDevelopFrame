package com.sing.myrecycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linfaxin.recyclerview.R;
import com.linfaxin.recyclerview.headfoot.RefreshView;

/**
 * Created by linfaxin on 15/8/31.
 * Default impl of RefreshView
 */
public class RefreshViewFor5sing extends RefreshView {

    private LinearLayout mContainer;
    private ProgressBar mArrowImageView;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;
    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;
    private TextView mRefreshTime;

    private final int ROTATE_ANIM_DURATION = 180;

    public RefreshViewFor5sing(Context context) {
        super(context);
        init(context);
    }

    public RefreshViewFor5sing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RefreshViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.recyclerview_header,
                null);
        this.addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ProgressBar) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

        mRefreshTime = (TextView) findViewById(R.id.xlistview_header_time);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    protected void onStateChange(STATE newState, STATE oldState) {
        switch (newState) {
            case NORMAL:
                mHintTextView.setText(R.string.refresh_state_normal);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
//                if (oldState == STATE.READY) {
//                    mArrowImageView.startAnimation(mRotateDownAnim);
//                }
//                if (oldState == STATE.LOADING) {
//                    mArrowImageView.clearAnimation();
//                }
                break;
            case READY:
                mHintTextView.setText(R.string.refresh_state_ready);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
//                if (oldState != STATE.READY) {
//                    mArrowImageView.clearAnimation();
//                    mArrowImageView.startAnimation(mRotateUpAnim);
//                }
                break;
            case LOADING:
                Log.d("recycle","loading");
                mHintTextView.setText(R.string.refresh_state_loading);
//                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setRefreshTime(String time){
        mRefreshTime.setText(time);
    }
}
