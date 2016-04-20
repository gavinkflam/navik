package hk.gavin.navik.ui.widget.event;

import hk.gavin.navik.ui.widget.LocationSelector;

public class SelectLocationOnMapEvent {

    public final LocationSelector selector;

    public SelectLocationOnMapEvent(LocationSelector selector) {
        this.selector = selector;
    }
}
