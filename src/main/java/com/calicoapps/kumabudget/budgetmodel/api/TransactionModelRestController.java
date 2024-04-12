package com.calicoapps.kumabudget.budgetmodel.api;

import com.calicoapps.kumabudget.budgetmodel.entity.TransactionModel;
import com.calicoapps.kumabudget.budgetmodel.service.TransactionModelDataService;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.family.entity.Person;
import com.calicoapps.kumabudget.family.service.PersonDataService;
import com.calicoapps.kumabudget.monitor.LoggingHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constants.API_URL + "model/transactions")
@RequiredArgsConstructor
public class TransactionModelRestController {

    private final TransactionModelDataService transactionModelDataService;
    private final PersonDataService personDataService;

    @PostMapping()
    public TransactionModel createTransactionModel(
            @RequestBody TransactionModel transactionModel,
            HttpServletRequest request) {
        log.debug(LoggingHelper.buildRequestBodyLogLine(request.getMethod(), request.getRequestURI(), transactionModel));
        return transactionModelDataService.save(transactionModel);
    }

    @PutMapping()
    public TransactionModel updateTransactionModel(
            @RequestBody TransactionModel transactionModel,
            HttpServletRequest request) {
        log.debug(LoggingHelper.buildRequestBodyLogLine(request.getMethod(), request.getRequestURI(), transactionModel));
        return transactionModelDataService.save(transactionModel);
    }

    @GetMapping()
    public List<TransactionModel> getAllTransactionsModelsOfOnePerson(
            @RequestParam long personId,
            HttpServletRequest request) {
        log.debug(LoggingHelper.buildRequestIdLogLine(request.getMethod(), request.getRequestURI(), "" + personId));
        Person person = personDataService.findById(personId);
        return transactionModelDataService.getAllOfOnePerson(person);
    }

}
