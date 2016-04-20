package hk.gavin.navik.ui.widget.event;

import hk.gavin.navik.ui.widget.LocationSelector;

public class SelectAsStartingPointEvent {

    public final LocationSelector selector;

    public SelectAsStartingPointEvent(LocationSelector selector) {
        this.selector = selector;
    }
}
