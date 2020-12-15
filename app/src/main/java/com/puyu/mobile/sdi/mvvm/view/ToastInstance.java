package com.puyu.mobile.sdi.mvvm.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastInstance {
    Context mContext;
    Toast mToast = null;
    static ToastInstance instance;

    private ToastInstance(Context c) {
        mContext = c;
    }

    private void doShowText(String str) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private void doShowText(int resId) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
//
//    protected void doShowLoading1() {
//        if (null != mToast) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(mContext, mContext.getString(R.string.loading), Toast.LENGTH_LONG);
//        mToast.setGravity(Gravity.CENTER, 0, 0);
//        LayoutInflater lif = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = lif.inflate(R.layout.myloading_1, null);
//        ViewGroup toastView = (ViewGroup) mToast.getView();
//        toastView.addView(v, 0);
//        mToast.show();
//    }

    public static void CreateInstance(Context c) {
        if (instance == null) {
            instance = new ToastInstance(c);
        }
    }

    public static void Hide() {
        if (null != instance) {
            if (null != instance.mToast) {
                instance.mToast.cancel();
            }
        }
    }

    public static void ShowText(String str) {
        if (null != instance) {
            instance.doShowText(str);
//			instance.doShowLoading1();
        }
    }

    public static void ShowText(int resId) {
        if (null != instance) {
            instance.doShowText(resId);
        }
    }

//    public static void ShowLoading1() {
//        if (null != instance) {
//            instance.doShowLoading1();
//        }
//    }

}
