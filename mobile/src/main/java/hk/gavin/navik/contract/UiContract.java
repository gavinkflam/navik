package hk.gavin.navik.contract;

public class UiContract {

    public static final class RequestCode {
        public static final int STARTING_POINT_LOCATION = 1;
        public static final int STARTING_POINT_HISTORY = 1;
        public static final int DESTINATION_LOCATION = 3;
        public static final int DESTINATION_HISTORY = 3;
    }

    public static final class ResultCode {
        public static final int CANCEL = 0;
        public static final int OK = 1;

    }

    public static final class DataKey {
        public static final String TITLE = "title";
        public static final String LOCATION = "location";
    }

}
