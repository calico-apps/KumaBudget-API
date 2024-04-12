package com.calicoapps.kumabudget.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StreamUtil {

    public static <T> List<T> fromIterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public static String transformInputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

}