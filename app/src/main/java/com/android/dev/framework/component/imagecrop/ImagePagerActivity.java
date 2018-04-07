
package com.android.dev.framework.component.imagecrop;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.utils.KGLog;
import com.android.dev.shop.R;
import com.android.dev.shop.android.base.BaseBackgroundActivity;
import com.android.dev.shop.dialog.LoadingDialog;
import com.android.dev.shop.widget.HackyViewPager;
import com.android.dev.utils.ImageLoaderUtils;
import com.android.dev.utils.ToolUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.WeakHashMap;

public class ImagePagerActivity extends BaseBackgroundActivity {
    private static final String STATE_POSITION = "STATE_POSITION";

    public static final String EXTRA_IMAGE_INDEX = "image_index";

    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private static int loadFailImg = -1, loadingImg = -1;

    private HackyViewPager mPager;

    private int pagerPosition, imgCount;

    private TextView indicator;

    private static boolean isShowPage = true;

    private ImageButton photoLoad;

    private WeakHashMap<Integer, ImageView> imgHashMap = new WeakHashMap<Integer, ImageView>();

    private WeakHashMap<Integer, Bitmap> bitmaps = new WeakHashMap<Integer, Bitmap>();

    private Bitmap bit;
    private int pageId;

    private Handler handler;

    private final int MSG_SAVE_DATA = 1;// 保存图片到本地

    private static final int FINISHLOADPHOTO = 1;

    private ArrayList<String> urlList;

    private LoadingDialog loadingDialog;
    private int screenHeight, screenWidth;
    private RelativeLayout loading_view;
    private ProgressBar loading_bar;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        screenHeight = ToolUtils.getHeight(this);
        screenWidth = ToolUtils.getWidth(this);
        loadingDialog = new LoadingDialog(this);
        Intent intent = getIntent();
        pagerPosition = intent.getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urlList = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
        imgCount = urlList.size();
        initActivityAnimation();
        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(urlList);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);
        photoLoad = (ImageButton) findViewById(R.id.photoLoad);
        loading_view= (RelativeLayout) findViewById(R.id.loading_view);
        loading_bar= (ProgressBar) findViewById(R.id.loading_bar);
        CharSequence text = getString(R.string.viewpager_indicator, 1, imgCount);
        pageId = 1;
        indicator.setText(text);
        if (!isShowPage) {
            indicator.setVisibility(View.GONE);
            photoLoad.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
            photoLoad.setVisibility(View.VISIBLE);
            photoLoad.setEnabled(false);
            setLoading(View.VISIBLE);
        }
        if (urlList.size() <= 0) {
            indicator.setVisibility(View.GONE);
        }
        if (photoLoad.getVisibility() == View.VISIBLE) {
            photoLoad.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (bit == null) {
                        return;
                    }
                    boolean sdCardExist = Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED);
                    // 判断sd卡是否存在
                    if (!sdCardExist)
                        return;
                    String imageURL = urlList.get(pageId - 1);
                    String imageName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
                    //如果load的是本地，使用的path是本地的
                    
                    //如果load网络上的图片，名字选用的是网络的原来的名字
                    String path = Environment.getExternalStorageDirectory().toString()
                            + "/download/" + imageName + ".png";
                    if (new File(path).exists()) {
                        Toast toast = Toast.makeText(ImagePagerActivity.this, "已保存到/sd/download/",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    
                    photoLoad.setTag(path);

                    loadingDialog.show();
                    Message message = mBackgroundHandler.obtainMessage();
                    message.what = MSG_SAVE_DATA;
                    message.obj = bit;
                    mBackgroundHandler.sendMessage(message);
                    // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    // Uri.parse("file://"+ new
                    // File(Environment.getExternalStorageDirectory().toString()+"/xiaozhan/download/image"))));

                }
            });
        }
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 % imgCount + 1,
                        imgCount);
                indicator.setText(text);
                pageId = arg0 % imgCount + 1;
                if (bitmaps.get(pageId) == null) { // 说明本页图片还没加载好
                    photoLoad.setEnabled(false);
                    setLoading(View.VISIBLE);
                    KGLog.e("infox", "未加载");
                } else {
                    photoLoad.setEnabled(true);
                    setLoading(View.GONE);
                    KGLog.e("infox", "已加载");
                }
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(2000 * imgCount + pagerPosition);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handUIMessage(msg);
            }
        };
        if(getIntent().getBooleanExtra("hide", false)){
        	photoLoad.setVisibility(View.GONE);
        }
    }

    /**
     * @param loadFailImg 指定加载失败时显示的图片，默认值 -1
     * @param loadingImg 加载中显示的图片，默认值 -1
     * @param isShow 是否显示页面码
     */
    public static void initLoadingParams(int loadFailImg, int loadingImg, boolean isShow) {
        ImagePagerActivity.loadFailImg = loadFailImg;
        ImagePagerActivity.loadingImg = loadingImg;
        isShowPage = isShow;
    }

    private void setLoading(int visibility){
        loading_view.setVisibility(visibility);
    }
    
    private void handUIMessage(Message msg) {
        switch (msg.arg1) {
            case FINISHLOADPHOTO:
                if (bitmaps.get(pageId) == null) { // 说明本页图片还没加载好
                    photoLoad.setEnabled(false);
                    setLoading(View.VISIBLE);
                } else {
                    photoLoad.setEnabled(true);
                    setLoading(View.GONE);
                }
                photoLoad.setEnabled(true);
                setLoading(View.GONE);
                break;

            default:
                break;
        }
    }

    private void initActivityAnimation() {

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]

        {
            android.R.attr.windowAnimationStyle
        });
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(
                windowAnimationStyleResId,
                new int[]

                {
                        android.R.attr.activityCloseEnterAnimation,
                        android.R.attr.activityCloseExitAnimation
                });
//        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
//        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(ArrayList<String> fileList) {
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            // return fileList == null ? 0 : fileList.size()+1;
            if (imgCount > 1) {
                return Integer.MAX_VALUE;
            } else {
                return imgCount;
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (imgCount > 3) {
                position = position % imgCount;

                container.removeView(imgHashMap.get(position));
            } else if (imgCount == 2) {
                container.removeView((View) object);
            }

        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            int position = arg1 % imgCount;
            ImageView iv = null;
            String url = fileList.get(position);
            if (imgCount != 2) {
                iv = imgHashMap.get(position);
            } else {
                iv = imgHashMap.get(arg1);
            }

            if (iv == null) {
                iv = getImageView(url);
                loadImage(ToolUtils.getDynamicPhoto(url, screenWidth, screenHeight), iv);
                if (imgCount != 2) {
                    imgHashMap.put(position, iv);
                } else {
                    imgHashMap.put(arg1, iv);
                }

            } else if (iv.getTag() != url) {
                loadImage(ToolUtils.getDynamicPhoto(url, screenWidth, screenHeight), iv);
            }
            arg0.removeView(iv);
            arg0.addView(iv);
            return iv;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        private ImageView getImageView(String url) {
            ImageView iv = new ImageView(ImagePagerActivity.this);
            LayoutParams params = new LayoutParams

            (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            return iv;
        }

        int i = 1;

        private void loadImage(final String imgUrl, ImageView iv) {
            ImageView startImageView = ((ImageView) iv);
            startImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePagerActivity.this.finish();
                }
            });
            KGLog.e("infox", "图片：" +imgUrl);
            iv.setImageResource(R.drawable.transparent);
            final String imgUrls = imgUrl;
            final ImageView ivs = iv;
//            GlideUtils.loadImageListerner(imgUrls, ivs, new RequestListener<String,Bitmap>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                    ivs.setScaleType(ScaleType.CENTER_INSIDE);
//                    if (loadFailImg != -1) {
//                        ivs.setImageResource(loadFailImg);
//                    } else {
//                        ivs.setImageResource(R.drawable.img_loadfail);
//                    }
//                    setLoading(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    bit =  resource;
//                    ImageView failImageView = ((ImageView) ivs);
//                    failImageView.setTag(imgUrls);
//                    failImageView.setOnClickListener(null);
//                    if (Build.VERSION.SDK_INT > 8) {
//                        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(failImageView);
//                        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//                            @Override
//                            public void onPhotoTap(View arg0, float arg1, float arg2) {
//                                ImagePagerActivity.this.finish();
//                            }
//                        });
//                    }
//                    // mAttacher.update();
//
//                    Message msg = Message.obtain();
//                    msg.arg1 = FINISHLOADPHOTO;
//                    handler.sendMessage(msg);
//                    if (imgUrls != null) {
//                        for (int i = 0; i < fileList.size(); i++) {
//                            //String url2 = ImageLoaderUtils.getInstance(ImagePagerActivity.this).getUrlTo5sing("", false);
//                            String url2 = ToolUtils.getDynamicPhoto(fileList.get(i), screenWidth, screenHeight);
//                            if (url2.equals(imgUrls)) {
//                                bitmaps.put(i + 1, resource);
//                                break;
//                            }
//                        }
//                    } else {
//                        KGLog.e("infox", "URLNULL");
//                    }
//                    return false;
//                }
//                
//            });
            
        
            
            ImageLoaderUtils.getInstance(ImagePagerActivity.this).displayImage(imgUrl, iv, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ImageView failImageView = ((ImageView) view);

                    KGLog.e("infox", "图片：" + imageUri);
                    failImageView.setScaleType(ScaleType.CENTER_INSIDE);
                    if (loadFailImg != -1) {
                        failImageView.setImageResource(loadFailImg);
                    } else {
                        failImageView.setImageResource(R.drawable.img_loadfail);
                    }
                    setLoading(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ImageView successImageView = ((ImageView) view);
                    bit = loadedImage;
                    successImageView.setTag(imageUri);
                    successImageView.setOnClickListener(null);
                    if (Build.VERSION.SDK_INT > 8) {
                        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(successImageView);
                        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                            @Override
                            public void onPhotoTap(View arg0, float arg1, float arg2) {
                                ImagePagerActivity.this.finish();
                            }
                        });
                    }
                    // mAttacher.update();

                    Message msg = Message.obtain();
                    msg.arg1 = FINISHLOADPHOTO;
                    handler.sendMessage(msg);
                    if (imageUri != null) {
                        for (int i = 0; i < fileList.size(); i++) {
                           //String url2 = ImageLoaderUtils.getInstance(ImagePagerActivity.this).getUrlTo5sing("", false);
                            String url2 = ToolUtils.getDynamicPhoto(fileList.get(i), screenWidth, screenHeight);
                            if (url2.equals(imageUri)) {
                                bitmaps.put(i + 1, loadedImage);
                                break;
                            }
                        }
                    } else {
                        KGLog.e("infox", "URLNULL");
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });

        }
    }

    /*
     * public static boolean saveImgToGallery(String fileName) { boolean
     * sdCardExist = Environment.getExternalStorageState().equals(
     * android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在 if (!sdCardExist)
     * return false; try { // String url =
     * MediaStore.Images.Media.insertImage(cr, bmp, // fileName, // ""); //
     * app.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri //
     * .parse("file://" // + Environment.getExternalStorageDirectory()))); //
     * debug ContentValues values = new ContentValues(); values.put("datetaken",
     * new Date().toString()); values.put("mime_type", "image/png");
     * values.put("_data", fileName); // values.put("title",
     * this.a.getString(2131230720)); // values.put("_display_name",
     * (String)localObject1); // values.put("orientation", ""); //
     * values.put("_size", Integer.valueOf(0)); Application app =
     * KGRingApplication.getThis(); ContentResolver cr = app.getContentResolver();
     * cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); } catch
     * (Exception e) { e.printStackTrace(); } return true; }
     */
    @Override
    protected void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_SAVE_DATA:
                loadingDialog.dismiss();
                Toast toast = Toast.makeText(ImagePagerActivity.this, "已保存到/sd/download/",
                        Toast.LENGTH_SHORT);
                toast.show();
        }

    }

    @Override
    protected void handleBackgroundMessage(Message msg) {
        switch (msg.what) {
            case MSG_SAVE_DATA:
                String path = (String) photoLoad.getTag();
                Bitmap bitmap = (Bitmap) msg.obj;
                if (path != null && bitmap != null) {
                    BitmapUtil.saveMyBitmap(path, bitmap);
                    try {
                        // 保存图片后,直接把路径等相关信息直接插入数据库 就可以立即在相册看到保存的图片了
                        ContentValues values = new ContentValues();
                        values.put("datetaken", new Date().toString());
                        values.put("mime_type", "image/png");
                        values.put("_data", path);
                        ContentResolver cr = ImagePagerActivity.this.getContentResolver();
                        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mUiHandler.sendEmptyMessage(MSG_SAVE_DATA);
                }
                break;

            default:
                break;
        }
    }

}
