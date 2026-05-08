package net.neoforged.neoforge.common.util;

import java.util.ArrayList;
import java.util.List;

public final class ConcatenatedListView {
    private ConcatenatedListView() {
    }

    @SafeVarargs
    public static <T> List<T> of(List<? extends T>... lists) {
        ArrayList<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
}