package com.calicoapps.kumabudget.auth.api;

import com.calicoapps.kumabudget.auth.dto.ChangePasswordRequest;
import com.calicoapps.kumabudget.auth.dto.CredentialsRequest;
import com.calicoapps.kumabudget.auth.service.CredentialsService;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.Device;
import com.calicoapps.kumabudget.common.util.HeadersUtil;
import com.calicoapps.kumabudget.monitor.LoggingHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.API_URL + "credentials")
@RequiredArgsConstructor
public class CredentialsRestController {

    private final CredentialsService credentialsService;

    // todo
    @PutMapping("/update/password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get();
        return ResponseEntity.ok("OK");
    }

    // todo
    @PutMapping("/update/email")
    public ResponseEntity<String> changeEmail(
            @RequestBody CredentialsRequest credentialsRequest,
            HttpServletRequest request
    ) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        Device device = HeadersUtil.getValidDeviceFromRequestHeaders(request).get();
        return ResponseEntity.ok("OK");
    }

}
