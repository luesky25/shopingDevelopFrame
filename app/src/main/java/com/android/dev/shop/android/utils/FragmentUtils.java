package com.android.dev.shop.android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.android.dev.framework.component.base.BaseFragment;

import java.util.List;

public class FragmentUtils {
	
	/**
	 * 启动对应的Fragment
	 * @param manager  fragment管理器
	 * @param root     加载节点
	 * @param fragment 需要启动的fragment
	 */
	public static void replaceFragmentWithStack(FragmentManager manager, int root, Fragment fragment) {
		runOnLeave(manager);
		manager.beginTransaction()
			.replace(root, fragment, fragment.getClass().getSimpleName())
			.addToBackStack("BackStack" + fragment.getClass().getSimpleName())
			.commitAllowingStateLoss();
	}
	
	public static void replaceFragmentWithStack(FragmentManager manager, int root, Fragment fragment, int animIn, int animOut) {
		runOnLeave(manager);
		manager.beginTransaction()
			.setCustomAnimations(animIn, animOut)
			.replace(root, fragment, fragment.getClass().getSimpleName())
			.addToBackStack("BackStack" + fragment.getClass().getSimpleName())
			.commitAllowingStateLoss();
	}
	
	/**
	 * 启动对应的Fragment
	 * @param manager  fragment管理器
	 * @param root     加载节点
	 * @param fragment 需要启动的fragment
	 */
	public static void replaceFragment(FragmentManager manager, int root, Fragment fragment) {
		runOnLeave(manager);
		manager.beginTransaction()
			.replace(root, fragment, fragment.getClass().getSimpleName())
			.commitAllowingStateLoss();
	}
	
	/**
	 * 启动对应的Fragment
	 * @param manager  fragment管理器
	 * @param root     加载节点
	 * @param fragment 需要启动的fragment
	 * @param animIn   fragment进入时候的动画
	 * @param animOut  fragment退出（当前）时候的动画
	 */
	public static void replaceFragment(FragmentManager manager, int root, Fragment fragment,int animIn,int animOut) {
		runOnLeave(manager);
		FragmentTransaction ft = manager.beginTransaction();
		//setCustomAnimations()必须位于replace()之前,否则没有效果
		ft.setCustomAnimations(animIn, animOut);
		ft.replace(root, fragment, fragment.getClass().getSimpleName());
		ft.commitAllowingStateLoss();
	}	
	
	public static String getFragmentTag(Class clazz) {
		return clazz.getSimpleName();
	}
	
	public static void popBackStackAndNotify(FragmentManager manager) {
		runOnLeave(manager);
		try {
			boolean reslut = manager.popBackStackImmediate();
			//即将重新出现的fragment调用onReload接口
			if (reslut) {
				runOnReload(manager);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void popBackStackAndNotify(FragmentManager manager, String fragmentTab) {
		runOnLeave(manager);
		try {
			boolean reslut = manager.popBackStackImmediate();
			if (reslut) {
				runOnReload(manager);
			}
			//刷新用户指定的fragment的reReload接口
			Fragment item = manager.findFragmentByTag(fragmentTab);
			if (item != null && item instanceof BaseFragment)
				((BaseFragment)item).onReload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 离开当前的fragment调用onLeave接口
	 * @param manager
	 */
	private static void runOnLeave(FragmentManager manager) {
		List<Fragment> fragments = manager.getFragments();
		if (fragments != null && fragments.size() >= 1) {
			Fragment item = null;
			for (int i = fragments.size() - 1; i >= 0; i--) {
				item = fragments.get(i);
				if (item != null)
					break;
			}
			if (item != null && item instanceof BaseFragment)
				((BaseFragment)item).onLeave();
		}
	}
	
	/**
	 * 即将重新出现的fragment调用onReload接口
	 * @param manager
	 */
	private static void runOnReload(FragmentManager manager) {
		List<Fragment> fragments = manager.getFragments();
		if (fragments != null && fragments.size() >= 1) {
			Fragment item = null;
			for (int i = fragments.size() - 1; i >= 0; i--) {
				item = fragments.get(i);
				if (item != null)
					break;
			}
			if (item != null && item instanceof BaseFragment)
				((BaseFragment)item).onReload();
		}
	}
	
}
