package hk.gavin.navik.ui.widget.event;

import hk.gavin.navik.ui.widget.LocationSelector;

public class SelectCurrentLocationEvent {

    public final LocationSelector selector;

    public SelectCurrentLocationEvent(LocationSelector selector) {
        this.selector = selector;
    }
}
