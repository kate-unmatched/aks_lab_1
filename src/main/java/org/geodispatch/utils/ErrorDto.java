package org.geodispatch.utils;

import java.util.Map;

public record ErrorDto(String error, String message, Map<String, String> fields) {
    public static ErrorDto of(String code, String msg) { return new ErrorDto(code, msg, null); }
    public static ErrorDto of(String code, String msg, Map<String, String> fields) { return new ErrorDto(code, msg, fields); }
}
