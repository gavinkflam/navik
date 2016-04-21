package hk.gavin.navik.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.nononsenseapps.filepicker.FilePickerActivity;
import hk.gavin.navik.filepicker.NKGpxFilePickerActivity;

public class FilePickerUtility {

    public static Intent pickGpxFileIntent(Context context) {
        Intent intent = new Intent(context, NKGpxFilePickerActivity.class);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        return intent;
    }
}
