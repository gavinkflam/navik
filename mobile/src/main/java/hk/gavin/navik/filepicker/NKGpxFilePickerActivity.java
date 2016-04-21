package hk.gavin.navik.filepicker;

import com.nononsenseapps.filepicker.AbstractFilePickerActivity;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;

import java.io.File;

public class NKGpxFilePickerActivity extends AbstractFilePickerActivity<File> {

    public NKGpxFilePickerActivity() {
        super();
    }

    @Override
    protected AbstractFilePickerFragment<File> getFragment(String startPath, int mode,
                                                           boolean allowMultiple, boolean allowCreateDir) {
        AbstractFilePickerFragment<File> fragment = new GpxFilePickerFragment();
        fragment.setArgs(startPath, mode, allowMultiple, allowCreateDir);
        return fragment;
    }
}
