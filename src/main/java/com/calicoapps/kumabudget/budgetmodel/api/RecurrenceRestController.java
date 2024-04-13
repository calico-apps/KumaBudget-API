package com.calicoapps.kumabudget.budgetmodel.api;

import com.calicoapps.kumabudget.budgetmodel.entity.Frequency;
import com.calicoapps.kumabudget.budgetmodel.entity.RecurrenceType;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.monitor.LoggingHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.API_URL + "model/recurrences")
@RequiredArgsConstructor
public class RecurrenceRestController {

    @GetMapping("/types")
    @Operation(summary = "All the types of recurrence of a transaction (fixed date, ...)")
    public RecurrenceType[] getAllRecurrenceTypes(
            HttpServletRequest request) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        return RecurrenceType.values();
    }

    @GetMapping("/frequencies")
    @Operation(summary = "All types of frequency of a transaction (every x weeks, x months, ...)")
    public Frequency[] getAllFrequencies(
            HttpServletRequest request) {
        log.debug(LoggingHelper.buildRequestSimpleLogLine(request.getMethod(), request.getRequestURI()));
        return Frequency.values();
    }

}
