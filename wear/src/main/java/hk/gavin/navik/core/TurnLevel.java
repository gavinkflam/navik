package hk.gavin.navik.core;

import hk.gavin.navik.core.navigation.NKNavigationState;

public enum TurnLevel {
    Immediate, Soon, Safe;

    public static TurnLevel fromNavigationState(NKNavigationState navigationState) {
        if (navigationState.distanceToNextAdvice > 249) {
            return Safe;
        }
        else if (navigationState.distanceToNextAdvice > 99) {
            return Soon;
        }
        return Immediate;
    }
}
