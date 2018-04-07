package com.android.common.permissiions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Created by anckyxia on 2016/10/24.
 * 用于6.0以上机型的权限处理
 *  使用拍照权限处理
 */

public class UseCameraPermissionUtils {

    final static int PERMISSION_USE_CAMERA_REQUEST = 200;
    final static int NUM_USE_CAMERA_PERMISSIONS = 1;

    static PermissionTipsDialog mTipsDialog;
    static PermissionTipsDialog mErrTipsDialog;

    /**
     * 判断当前有无足够的基本权限
     * @param context
     * @return
     */
    public static boolean haveEnoughUseCameraPermissions(Activity context){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return  true;
    }

    /**
     * 刚打开酷狗铃声App，要求赋予的基本权限
     * @param context
     */
    public static void requestUseCameraPermission(final Activity context) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            if (!haveEnoughUseCameraPermissions(context)){
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)
                       ) {
                    // 弹出权限对话框进行询问
                    if (mTipsDialog == null) {
                        mTipsDialog = new PermissionTipsDialog(context);
                    }
                    mTipsDialog.setTitle("权限");
                    mTipsDialog.setContentString("温馨提醒:\n\n"
                            + "修改头像需要使用相机权限\n\n"
                            + "开启权限后酷狗铃声才能正常运作");
                    mTipsDialog.setPositiveButtonClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTipsDialog.dismiss();
                            mTipsDialog = null;
                            actuallyRequestBasicPermissions(context);
                        }
                    });
                    mTipsDialog.setNegativeButtonClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTipsDialog.dismiss();
                            mTipsDialog = null;
                            // 再弹操作不当错误提醒
                            if (mErrTipsDialog == null){
                                mErrTipsDialog = new PermissionTipsDialog(context);
                            }
                            mErrTipsDialog.setTitle("错误");
                            mErrTipsDialog.setContentString("不赋予相关权限，酷狗铃声无法正常修改头像");
                            mErrTipsDialog.setCancelable(false);
                            mErrTipsDialog.setNegativeButtonClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mErrTipsDialog.dismiss();
                                    mErrTipsDialog = null;
                                    context.finish();
                                }
                            });
                            mErrTipsDialog.setPositiveButtonClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mErrTipsDialog.dismiss();
                                    mErrTipsDialog = null;
                                    context.finish();
                                }
                            });
                            mErrTipsDialog.show();
                        }
                    });
                    mTipsDialog.setCancelable(false);
                    mTipsDialog.show();

                } else {
                    actuallyRequestBasicPermissions(context);
                }
            }
        }
    }

    /**
     * 系统弹窗显示需要赋值的权限
     */
    public static void actuallyRequestBasicPermissions(final Activity context)
    {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.CAMERA
                },
                PERMISSION_USE_CAMERA_REQUEST);
    }

    public static  boolean onRequestPermissionsResult(final Activity context, int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_USE_CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                boolean good = true;
                if(permissions.length != NUM_USE_CAMERA_PERMISSIONS || grantResults.length != NUM_USE_CAMERA_PERMISSIONS)
                {
                    good = false;
                }

                for(int i = 0; i<grantResults.length && good; i++)
                {
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                    {
                        good = false;
                    }
                }
                if(!good) {

                    // permission denied, boo! Disable the app.
                    //TODO: only disable if files are inaccessible
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                        // 再弹操作不当错误提醒
                        if (mErrTipsDialog == null){
                            mErrTipsDialog = new PermissionTipsDialog(context);
                        }
                        mErrTipsDialog.setTitle("错误");
                        mErrTipsDialog.setContentString("不赋予相关权限，酷狗铃声无法正常修改头像");
                        mErrTipsDialog.setCancelable(false);
                        mErrTipsDialog.setNegativeButtonClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mErrTipsDialog.dismiss();
                                mErrTipsDialog = null;

                            }
                        });
                        mErrTipsDialog.setPositiveButtonClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mErrTipsDialog.dismiss();
                                mErrTipsDialog = null;

                            }
                        });
                        mErrTipsDialog.show();
                    }
                    return false;
                }else{
                    return true;
                }

            }
        }
        return  true;
    }
}
