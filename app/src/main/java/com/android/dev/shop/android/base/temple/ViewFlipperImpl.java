package com.android.dev.shop.android.base.temple;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.ViewFlipper;


import com.android.common.utils.KGLog;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

public class ViewFlipperImpl extends ViewFlipper  implements NestedScrollingChild {

	private static final java.lang.String TAG ="ViewFlipperImpl" ;
	private boolean isDetached;
	private NestedScrollingChildHelper mChildHelper;
	private final int mMinFlingVelocity;
	private final int mMaxFlingVelocity;
	private VelocityTracker mVelocityTracker;
	private int mScrollPointerId = INVALID_POINTER;

	private int mLastMotionY;
	private final int[] mScrollOffset = new int[2];
	private final int[] mScrollConsumed = new int[2];
	private int mNestedYOffset;

	public ViewFlipperImpl(Context context, AttributeSet attrs) {
		super(context, attrs);
		final ViewConfiguration vc = ViewConfiguration.get(getContext());
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

		mChildHelper = new NestedScrollingChildHelper(this);
		setNestedScrollingEnabled(true);
	}

	public ViewFlipperImpl(Context context) {
		this(context,null);
	}

	/**
	 * getContext().unregisterReceiver(mReceiver);
	 * ViewFlipper会注册一个receiver
	 * 但不知道为什么，有时它又不会解除receiver;
	 * 所以暴露一个公共方法，用来手动解除绑定
	 */
	@Override
	public void onDetachedFromWindow() {
//		if (isDetached)
//			return;
//		isDetached = true;
		try {
			super.onDetachedFromWindow();
		} catch (Exception e) {
			// 会出现没有注册就调用的情况，catch
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		boolean result = false;

		MotionEvent trackedEvent = MotionEvent.obtain(event);
		final int action = MotionEventCompat.getActionMasked(event);
		if (action == MotionEvent.ACTION_DOWN) {
			mNestedYOffset = 0;
		}

		int y = (int) event.getY();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		event.offsetLocation(0, mNestedYOffset);
		final int actionIndex = MotionEventCompat.getActionIndex(event);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = y;
				mScrollPointerId = event.getPointerId(0);
				startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                result = super.onTouchEvent(event);
				result = true;
				KGLog.e(TAG, "ACTION_DOWN  result:" + result);
				break;
			case MotionEvent.ACTION_MOVE:
				KGLog.e(TAG, "ACTION_MOVE");
				int deltaY = mLastMotionY - y;

				if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
					deltaY -= mScrollConsumed[1];
					trackedEvent.offsetLocation(0, mScrollOffset[1]);
					mNestedYOffset += mScrollOffset[1];
				}

				int oldY = getScrollY();
				mLastMotionY = y - mScrollOffset[1];
				if (deltaY < 0) {
					int newScrollY = Math.max(0, oldY + deltaY);
					deltaY -= newScrollY - oldY;
					if (dispatchNestedScroll(0, newScrollY - deltaY, 0, deltaY, mScrollOffset)) {
						mLastMotionY -= mScrollOffset[1];
						trackedEvent.offsetLocation(0, mScrollOffset[1]);
						mNestedYOffset += mScrollOffset[1];
					}
				}

				trackedEvent.recycle();
				result = super.onTouchEvent(trackedEvent);
				break;
			case MotionEvent.ACTION_UP:
				KGLog.e(TAG, "ACTION_UP");
				mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
//                mVelocityTracker.computeCurrentVelocity(1000); //设置units的值为1000，意思为一秒时间内运动了多少个像素
				final float xvel = -VelocityTrackerCompat.getXVelocity(mVelocityTracker, mScrollPointerId);
				final float yvel = -VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
				KGLog.e(TAG, "xvel  :" + xvel + "   yvel :" + yvel);
				resetTouch();
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				KGLog.e(TAG, "ACTION_POINTER_DOWN");
				mScrollPointerId = event.getPointerId(actionIndex);
				break;
			case MotionEvent.ACTION_CANCEL:
				KGLog.e(TAG, "ACTION_CANCEL");
				stopNestedScroll();
				result = super.onTouchEvent(event);
				break;
		}
		return result;
	}

	private void resetTouch() {
		if (mVelocityTracker != null) {
			mVelocityTracker.clear();
		}
		stopNestedScroll();
	}

	public boolean fling(int velocityX, int velocityY) {
		if (!dispatchNestedPreFling(velocityX, velocityY)) {
			dispatchNestedFling(velocityX, velocityY, true);
			return true;
		}
		return false;
	}


	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		KGLog.e(TAG, "setNestedScrollingEnabled");
		mChildHelper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		KGLog.e(TAG, "isNestedScrollingEnabled");
		return mChildHelper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes) {
		KGLog.e(TAG, "startNestedScroll");
		return mChildHelper.startNestedScroll(axes);
//        return true;
	}

	@Override
	public void stopNestedScroll() {
		KGLog.e(TAG, "stopNestedScroll");
		mChildHelper.stopNestedScroll();
	}

	@Override
	public boolean hasNestedScrollingParent() {
		KGLog.e(TAG, "hasNestedScrollingParent");
		return mChildHelper.hasNestedScrollingParent();
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
		KGLog.e(TAG, "dispatchNestedScroll");
		return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
		KGLog.e(TAG, "dispatchNestedPreScroll");
		return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		KGLog.e(TAG, "dispatchNestedFling");
		return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		KGLog.e(TAG, "dispatchNestedPreFling");
		return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
	}



}
