package hk.gavin.navik.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtility {

    public static <T> List<T> sampleIfExceedLimit(List<T> list, int limit) {
        int n = (int) Math.ceil((double) list.size() / limit);
        List<T> result;

        if (n == 1) {
            result = list;
        }
        else {
            result = new ArrayList<>();
            for (int i = 0; i < list.size(); i += n) {
                result.add(list.get(i));
            }
        }
        return result;
    }
}
