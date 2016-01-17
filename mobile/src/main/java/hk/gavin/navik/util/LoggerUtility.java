package hk.gavin.navik.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class LoggerUtility {

    public static void initializeLogger(Application application) {
        boolean isDebuggable = (0 != (application.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        Logger.init("NK")
                .logLevel(isDebuggable ? LogLevel.FULL : LogLevel.NONE)
                .hideThreadInfo();
    }
}
