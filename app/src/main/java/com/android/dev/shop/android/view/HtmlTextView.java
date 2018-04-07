package com.android.dev.shop.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.common.volley.toolbox.ImageLoader;
import com.android.dev.utils.URLDrawable;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Administrator on 2018-03-25.
 */

public class HtmlTextView extends AppCompatTextView {
    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }






    public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            String sources="";
            final URLDrawable urlDrawable = new URLDrawable();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(sources, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    urlDrawable.bitmap= loadedImage;
                    urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                    mTextView.invalidate();
                    mTextView.setText(mTextView.getText()); // 解决图文重叠
                }
            });
            return urlDrawable;
        }
    }






}
