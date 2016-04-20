package hk.gavin.navik.core.navigation.event;

import hk.gavin.navik.core.navigation.NKNavigationState;

public class NavigationStateUpdateEvent {

    public final NKNavigationState navigationState;

    public NavigationStateUpdateEvent(NKNavigationState navigationState) {
        this.navigationState = navigationState;
    }
}
