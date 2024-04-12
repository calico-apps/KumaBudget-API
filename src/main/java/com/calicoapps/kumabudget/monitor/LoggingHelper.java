package com.calicoapps.kumabudget.monitor;

import com.calicoapps.kumabudget.common.util.JsonUtil;

public class LoggingHelper {

    public static final String PERF_PREFIX = "[PERF]";
    private static final String REQUEST_PREFIX = "[REQUEST]";

    public static String buildRequestSimpleLogLine(String method, String requestURI) {
        return REQUEST_PREFIX + " " + method + " " + requestURI;
    }

    public static String buildRequestIdLogLine(String method, String requestURI, String id) {
        return REQUEST_PREFIX + " " + method + " " + requestURI + "id: " + id;
    }

    public static String buildRequestBodyLogLine(String method, String requestURI, Object body) {
        return REQUEST_PREFIX + " " + method + " " + requestURI + "body:\n" + JsonUtil.toJson(body);
    }

}
