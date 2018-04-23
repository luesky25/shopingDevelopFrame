package com.android.dev.shop.android.base.base.parser;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;

import com.android.dev.shop.android.base.BaseWorkerOnClickFragment;
import com.android.dev.shop.android.base.BaseWorkerShowFragmentActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018-03-30.
 */

public abstract class BaseHtmlParserActivity extends BaseWorkerShowFragmentActivity {
    @Override
    protected void handleBackgroundMessage(Message msg) {

    }

    protected void initParser(String html){
        Message msg = Message.obtain();
        /**
         * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned
         * fromHtml (String source, Html.ImageGetterimageGetter,
         * Html.TagHandler
         * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
         * (String source)方法中返回图片的Drawable对象才可以。
         */
        Html.ImageGetter imageGetter = new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                // TODO Auto-generated method stub
                URL url;
                Drawable drawable = null;
                try {
                    url = new URL(source);
                    drawable = Drawable.createFromStream(
                            url.openStream(), null);
                    if(drawable!=null){
                        drawable.setBounds(0, 0,
                                drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight());
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return drawable;
            }
        };
        CharSequence test = Html.fromHtml(html, imageGetter, null);
        msg.what = 0x101;
        msg.obj = test;
        handler.sendMessage(msg);
    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0x101) {
//                bar.setVisibility(View.GONE);
//                tv.setText((CharSequence) msg.obj);
                    updateHtmlUi((CharSequence) msg.obj);
            }
            super.handleMessage(msg);
        }
    };

    public abstract void updateHtmlUi(CharSequence sequence);

}
