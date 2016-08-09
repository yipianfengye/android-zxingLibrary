package com.uuzuche.lib_zxing;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * Created by aaron on 16/8/9.
 */

public class ZApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化尺寸工具类
         */
        initDisplayOpinion();
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
