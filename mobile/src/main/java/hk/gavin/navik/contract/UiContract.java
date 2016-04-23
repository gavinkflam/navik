package hk.gavin.navik.contract;

public class UiContract {

    public static final class RequestCode {
        public static final int DEFAULT = 0;
        public static final int STARTING_POINT_LOCATION = 1;
        public static final int STARTING_POINT_HISTORY = 2;
        public static final int DESTINATION_LOCATION = 3;
        public static final int DESTINATION_HISTORY = 4;
        public static final int SELECT_GPX_FILE = 5;
        public static final int NAVIGATION = 6;
    }

    public static final class ResultCode {
        public static final int DEFAULT = 0;
        public static final int CANCEL = 1;
        public static final int OK = 2;

    }

    public static final class DataKey {
        public static final String SUPER_STATE = "SuperState";

        public static final String TITLE = "title";
        public static final String LOCATION = "location";
        public static final String USE_CURRENT_LOCATION = "useCurrentLocation";

        public static final String DIRECTIONS = "directions";
    }

    public static final class FragmentTag {
        public static final String HOME = "home";
        public static final String SELECT_STARTING_POINT = "select_starting_point";
        public static final String SELECT_DESTINATION = "select_destination";
        public static final String HOME_MAP = "home_map";
        public static final String NAVIGATION_MAP = "navigation_map";
        public static final String ROUTE_DISPLAY = "route_display";
        public static final String NAVIGATION = "navigation";
        public static final String ROUTE_ANALYSIS = "route_analysis";
        public static final String SETTING = "setting";
    }
}
