package hk.gavin.navik.core.navigation;

public interface NKNavigationListener {

    void onNavigationStart();
    void onNavigationStop();
    void onNavigationStateUpdate(NKNavigationState navigationState);
}
