package com.calicoapps.kumabudget.budgetmodel.service;

import com.calicoapps.kumabudget.budgetmodel.entity.ExpenseModel;
import com.calicoapps.kumabudget.budgetmodel.entity.ExpensePayerModel;
import com.calicoapps.kumabudget.budgetmodel.repository.ExpenseModelRepository;
import com.calicoapps.kumabudget.budgetmodel.repository.ExpensePayerModelRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseModelDataService extends DataService<ExpenseModel, Long> {

    public static final String ENTITY_NAME = "ExpenseModel";

    private final ExpenseModelRepository repository;
    private final ExpensePayerModelRepository payerRepository;

    public ExpenseModelDataService(ExpenseModelRepository repository, ExpensePayerModelRepository payerRepository) {
        super(ENTITY_NAME);
        this.repository = repository;
        this.payerRepository = payerRepository;
    }

    @Override
    protected JpaRepository<ExpenseModel, Long> getRepository() {
        return repository;
    }

    public ExpenseModel save(ExpenseModel expense, List<ExpensePayerModel> payers) {
        int totalPercentage = 0;
        for (ExpensePayerModel payer : payers) {
            totalPercentage += payer.getPercentage();
        }

        if (totalPercentage < 99 || totalPercentage > 101) {
            throw new KumaException(ErrorCode.BAD_REQUEST, "Sum of all payers % is not 100%: " + totalPercentage);
        }

        payers = payerRepository.saveAll(payers);
        expense.setPayers(payers);
        return repository.save(expense);
    }

    public List<ExpenseModel> getAllOfOnePerson(Person person) {
        List<ExpensePayerModel> payers = payerRepository.findByPerson(person);
        return extractAllExpensesFromPayers(payers);
    }

    private List<ExpenseModel> extractAllExpensesFromPayers(List<ExpensePayerModel> payers) {
        Set<ExpenseModel> expenses = payers.stream().map(payer -> payer.getExpense()).collect(Collectors.toSet());
        return expenses.stream()
                .sorted(Comparator.comparing(ExpenseModel::getStartDate))
                .collect(Collectors.toList());
    }

}
