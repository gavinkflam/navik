package hk.gavin.navik.util;

import android.content.Context;

public class StorageUtility {

    public static final String mapResourcesDirSuffix = "Navik";

    public static String getInitialStoragePath(Context context) {
        String externalFilesDirPath;

        if (context != null) {
            // Use external storage if possible, or internal storage otherwise
            externalFilesDirPath = context.getExternalFilesDir(null).toString();
            if (externalFilesDirPath != null) {
                return externalFilesDirPath;
            }

            return context.getFilesDir().getPath();
        }
        return "";
    }

}
