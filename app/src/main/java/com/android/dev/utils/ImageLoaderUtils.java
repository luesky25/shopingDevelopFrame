package com.android.dev.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderUtils {

	public final static String LOG_TAG = "ImageLoaderUtils";
	public final static int TYPE_PIG = 1;

	public final static void destroy() {

		if (ImageLoader.getInstance().isInited()) {

			ImageLoader.getInstance().clearMemoryCache();

			ImageLoader.getInstance().destroy();
		} else {

			Log.e(LOG_TAG, "destroy ImageLoader can't be inited");
		}
	}

	public final static void pause() {

		if (ImageLoader.getInstance().isInited()) {

			ImageLoader.getInstance().pause();
		} else {

			Log.e(LOG_TAG, "pause ImageLoader can't be inited");
		}
	}

	public final static void stop() {

		if (ImageLoader.getInstance().isInited()) {

			ImageLoader.getInstance().stop();
		} else {

			Log.e(LOG_TAG, "stop ImageLoader can't be inited");

		}
	}

	public final static void resume() {

		if (ImageLoader.getInstance().isInited()) {

			ImageLoader.getInstance().resume();
		} else {

			Log.e(LOG_TAG, "resume ImageLoader can't be inited");
		}
	}

	public final static void cancel(ImageView imageView) {

		if (ImageLoader.getInstance().isInited()) {

			ImageLoader.getInstance().cancelDisplayTask(imageView);
		} else {

			Log.e(LOG_TAG, "resume ImageLoader can't be inited");
		}
	}

	public final static void initDefaultImageLoader(final Context context) {

		if (ImageLoader.getInstance().isInited()) {
			return;
		}

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.resetViewBeforeLoading(true)
				.build();

		final Context appContext = context.getApplicationContext();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				appContext)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				//.denyCacheImageMultipleSizesInMemory()
				
				//.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		        //.memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(9) // default
				.threadPoolSize(3)
				.diskCacheFileCount(250)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(options)
				.diskCache(new UnlimitedDiskCache(new File(SDCardUtil.IMAGE_LOADER_FOLDER)))
				.imageDownloader(new BaseImageDownloader(appContext){
					@Override
					protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
						return null;
					}
				})
				.build();

		ImageLoader.getInstance().init(config);
	}

	public final static void clearMemoryCache() {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (imageLoader.isInited()) {

			imageLoader.clearMemoryCache();
		}
	}

	public final static void loadSimpleImage(String uri, ImageView imageView,
			DisplayImageOptions options, Context context) {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {

			if (context == null) {

				context = imageView.getContext();
			}

			initDefaultImageLoader(context);
		}
		imageLoader.displayImage(uri, imageView, options);
	}
	
	public final static void loadCacheImage(String uri,DisplayImageOptions options, Context context,ImageLoadingListener l) {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {
			initDefaultImageLoader(context);
		}

		imageLoader.loadImage(uri, options, l);
	}

	public final static void loadSimpleImage(String uri, ImageView imageView,
			DisplayImageOptions options, Context context,
			ImageLoadingListener listener) {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {

			if (context == null) {

				context = imageView.getContext();
			}

			initDefaultImageLoader(context);
		}

		if (listener != null) {
			imageLoader.displayImage(uri, imageView, options, listener);
		} else {
			imageLoader.displayImage(uri, imageView, options);
		}
	}

	public final static void loadSimpleImage2(String uri,
			DisplayImageOptions options, Context context,
			ImageLoadingListener listener) {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {
			initDefaultImageLoader(context);
		}

		imageLoader.loadImage(uri, options, listener);
	}

	public static ImageLoader getInstance(Context mContext) {
		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {
			initDefaultImageLoader(mContext);
		}

		return ImageLoader.getInstance();
	}

	public final static void loadSimpleImage(String uri, ImageView imageView,
			DisplayImageOptions options, Context context,
			ImageLoadingListener listener,
			ImageLoadingProgressListener progressListener) {

		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {

			if (context == null) {

				context = imageView.getContext();
			}

			initDefaultImageLoader(context);
		}

		imageLoader.displayImage(uri, imageView, options, listener,
				progressListener);

	}

	public static void loadSimpleImage(String uri, ImageSize imagesize,
			DisplayImageOptions options, Context context,
			ImageLoadingListener listener) {
		ImageLoader imageLoader = ImageLoader.getInstance();

		if (!imageLoader.isInited()) {
			initDefaultImageLoader(context);
		}
		imageLoader.loadImage(uri, imagesize, options, listener);
	}

	public static ImageView getDisplayImageView(Bitmap bitmap,
			ImageAware imageAware) {

		ImageView imageView = null;

		boolean isOk = imageAware != null && bitmap != null
				&& !bitmap.isRecycled();

		if (isOk) {

			View wrappedView = imageAware.getWrappedView();

			if (wrappedView instanceof ImageView) {

				imageView = (ImageView) wrappedView;
			}
		}

		return imageView;
	}

	public static DiskCache getDisccache() {
		return ImageLoader.getInstance().getDiskCache();
	}
}
