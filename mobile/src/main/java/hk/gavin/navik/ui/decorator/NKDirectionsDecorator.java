package hk.gavin.navik.ui.decorator;

import hk.gavin.navik.core.directions.NKDirections;
import hk.gavin.navik.util.FormattingUtility;

public class NKDirectionsDecorator extends Decorator<NKDirections> {

    public NKDirectionsDecorator(NKDirections object) {
        super(object);
    }

    public String distance() {
        return FormattingUtility.formatDistanceReadable(object.distance);
    }

    public String estimatedTime() {
        return FormattingUtility.formatTime(object.distance / 1000f / 15f);
    }
}
