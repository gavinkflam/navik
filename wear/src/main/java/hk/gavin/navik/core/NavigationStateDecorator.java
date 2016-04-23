package hk.gavin.navik.core;

import android.graphics.Bitmap;
import hk.gavin.navik.core.navigation.NKNavigationState;

public class NavigationStateDecorator {

    public final NKNavigationState object;

    public NavigationStateDecorator(NKNavigationState object) {
        this.object = object;
    }

    public Bitmap visualAdviceImage() {
        return this.object.visualAdviceImage.getBitmap();
    }

    public TurnLevel turnLevel() {
        return TurnLevel.fromNavigationState(this.object);
    }
}
