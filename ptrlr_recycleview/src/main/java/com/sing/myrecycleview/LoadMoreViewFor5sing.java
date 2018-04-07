package com.sing.myrecycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linfaxin.recyclerview.R;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;

/**
 * 由 lampart 于 16/2/29 创建
 */
public class LoadMoreViewFor5sing extends LoadMoreView {

    private Context mContext;
    private View mContentView;
    private View mProgressBar;
    private View mLoadingGroup;
    private TextView mHintView;
    private boolean hintWhenNoMore=false;


    public LoadMoreViewFor5sing(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreViewFor5sing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreViewFor5sing(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.recyclerview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        mLoadingGroup = moreView.findViewById(R.id.loading_root);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setMinimumHeight((int) (48 * getContext().getResources().getDisplayMetrics().density));
    }

    @Override
    protected void onStateChange(STATE state, STATE oldState) {
        switch (state) {
            case NORMAL:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(R.string.loadmore_state_normal);
                mLoadingGroup.setVisibility(GONE);
                hideMore();
                break;
            case LOADING:
                mHintView.setVisibility(GONE);
                mLoadingGroup.setVisibility(VISIBLE);
                showMore();
                break;
            case READY:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(R.string.loadmore_state_ready);
                mLoadingGroup.setVisibility(GONE);
                showMore();
                break;
            case NO_MORE:
                if (hintWhenNoMore) {
                    mHintView.setVisibility(GONE);
                    hideMore();
                } else {
                    mHintView.setVisibility(VISIBLE);
                    mHintView.setText(R.string.loadmore_state_no_more);
                }
                mLoadingGroup.setVisibility(GONE);
                break;
            case LOAD_FAIL:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(R.string.loadmore_state_fail);
                mLoadingGroup.setVisibility(GONE);
                showMore();
                break;
            case EMPTY_RELOAD:
                mHintView.setVisibility(VISIBLE);
                mHintView.setText(R.string.loadmore_state_ready);
                mLoadingGroup.setVisibility(GONE);
                showMore();
                break;

        }
    }

    public void hideMore(){
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(),
                recyclerView.getPaddingRight(), 0);
    }
    public void showMore(){
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(),
                recyclerView.getPaddingRight(), getMeasuredHeight());
    }

    public void setNoMoreHideWhenNoMoreData(boolean hide){
        hintWhenNoMore=hide;
    }

//    public void setTextShow(String text) {
//        textView.setText(text);
//    }
}
