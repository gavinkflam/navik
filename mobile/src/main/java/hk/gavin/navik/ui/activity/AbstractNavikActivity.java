package hk.gavin.navik.ui.activity;

import hk.gavin.navik.injection.AbstractActivityComponent;

public interface AbstractNavikActivity<T extends AbstractActivityComponent> {

    T component();
}
