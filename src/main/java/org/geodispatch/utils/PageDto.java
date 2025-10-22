package org.geodispatch.utils;

import java.util.List;

public record PageDto<T>(List<T> items, long total, int offset, int limit) { }
