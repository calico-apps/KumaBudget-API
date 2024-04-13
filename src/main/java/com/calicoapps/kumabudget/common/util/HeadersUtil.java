package com.calicoapps.kumabudget.common.util;

import com.calicoapps.kumabudget.common.Device;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class HeadersUtil {

    public static Optional<String> getValidTokenFromRequestHeaders(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        } else {
            String token = authHeader.substring(7);
            return Optional.of(token);
        }
    }

    public static Optional<Device> getValidDeviceFromRequestHeaders(HttpServletRequest request) {
        final String deviceHeader = request.getHeader("Device");
        if (deviceHeader == null) {
            return Optional.empty();
        } else {
            Device device = Device.valueOf(deviceHeader.toUpperCase());
            return Optional.of(device);
        }
    }

}
