package me.boot.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * PageUtils
 *
 * @since 2023/12/24
 **/
public abstract class PageUtils {

    /**
     * Generates a paginated sublist of a given collection based on the provided page and size.
     *
     * @param  page     the page number to retrieve (starting from 1)
     * @param  size     the size of each page
     * @param  data     the collection of data to paginate
     * @return          the paginated sublist of data
     */
    public static <T> List<T> paging(int page, int size, @NonNull Collection<T> data) {
        int fromIndex = Math.multiplyExact(page, size);
        if (fromIndex > data.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(Math.addExact(fromIndex, size), data.size());
        return new ArrayList<>(data).subList(fromIndex, toIndex);
    }
}
