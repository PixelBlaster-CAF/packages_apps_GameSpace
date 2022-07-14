package com.blaster.gamespace.utils;

import android.util.BoostFramework;
public class PerformanceModeManager {
    private static final String PACKAGE_NAME = "com.blaster.gamespace";
    private static final int PERFORMANCE_MODE_BOOST_ID = 4241;
    private static final String PROP_PERFORMANCE_MODE_SUPPORT = "vendor.perf.performancemode.support";
    private static final String TAG = PerformanceModeManager.class.getSimpleName();
    private static final PerformanceModeManager ourInstance = new PerformanceModeManager();
    private BoostFramework mBoostFramework = new BoostFramework();

    public static PerformanceModeManager getInstance() {
        return ourInstance;
    }

    private PerformanceModeManager() {
    }

    public int turnOnPerformanceMode() {
        int handle = this.mBoostFramework.perfHint(PERFORMANCE_MODE_BOOST_ID, PACKAGE_NAME, Integer.MAX_VALUE, -1);
        String str = TAG;
        return handle;
    }

    public int turnOffPerformanceMode(int handle) {
        int ret = -1;
        String str = TAG;
        if (-1 != handle) {
            ret = this.mBoostFramework.perfLockReleaseHandler(handle);
        }
        return ret;
    }

    public boolean isPerformanceModeSupport() {
        String support = this.mBoostFramework.perfGetProp(PROP_PERFORMANCE_MODE_SUPPORT, "0");
        return "1".equalsIgnoreCase(support) || "true".equalsIgnoreCase(support);
    }
}