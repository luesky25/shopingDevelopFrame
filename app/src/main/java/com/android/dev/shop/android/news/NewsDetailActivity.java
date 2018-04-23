package com.android.dev.shop.android.news;

import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.dev.shop.R;
import com.android.dev.shop.android.base.base.parser.BaseHtmlParserActivity;

/**
 * Created by Administrator on 2018-03-30.
 */

public class NewsDetailActivity extends BaseHtmlParserActivity {

    public static final int BACK_PARSER_HTML = 0x001;
    private TextView tv;
    private ProgressBar bar;
    String html = "<html><head><title>TextView使用HTML</title></head><body><p><strong>强调</strong></p><p><em>斜体</em></p>"
            + "<p><a href=\"http://www.jb51.net\">超链接HTML入门</a>学习HTML!</p><p><font color=\"#aabb00\">颜色1"
            + "</p><p><font color=\"#00bbaa\">颜色2</p><h1>标题1</h1><h3>标题2</h3><h6>标题3</h6><p>大于>小于<</p><p>"
            + "下面是网络图片</p><img src=\"http://img.ringop.kugou.com/v2/ring_image/bf194e2277edbbe9432ea1097340af6e.jpeg\"/></body>"
            + "下面是网络图片</p><img src=\"http://img.ringop.kugou.com/v2/ring_image/bf194e2277edbbe9432ea1097340af6e.jpeg\"/></body></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        initViews();
    }

    @Override
    protected void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what){
            case BACK_PARSER_HTML:
                initParser(html);

                break;
        }
    }



    private void initViews(){
        tv = (TextView) this.findViewById(R.id.tv);
        bar = (ProgressBar) this.findViewById(R.id.id_bar);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
       Message message = mBackgroundHandler.obtainMessage();
       mBackgroundHandler.sendEmptyMessage(BACK_PARSER_HTML);
    }

    @Override
    public void updateHtmlUi(CharSequence sequence) {
                bar.setVisibility(View.GONE);
                tv.setText(sequence);
    }
}
