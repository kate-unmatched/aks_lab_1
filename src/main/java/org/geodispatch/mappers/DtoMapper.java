package org.geodispatch.mappers;

import org.geodispatch.dtos.*;
import org.geodispatch.entity.*;
import org.geodispatch.utils.PageDto;

import java.util.List;
import java.util.function.Function;

public final class DtoMapper {
    private DtoMapper() {}

    public static <S,T> List<T> mapList(List<S> source, Function<S,T> mapper) {
        return source == null ? List.of() : source.stream().map(mapper).toList();
    }

    public static <E,D> PageDto<D> toPage(List<E> items, long total, int offset, int limit,
                                          Function<E,D> mapper) {
        return new PageDto<>(mapList(items, mapper), total, offset, limit);
    }
}
