package org.example.taskmanagementapi.utils;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PageUtils {
    public static <T,R> List<R> convertPage(Page<T> page, Function<T,R> mapper) {
        return page.stream()
                .map(mapper)
                .toList();
    }
}
