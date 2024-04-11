package com.calicoapps.kumabudget.budget.service;

import com.calicoapps.kumabudget.budget.entity.Expense;
import com.calicoapps.kumabudget.budget.entity.ExpensePayer;
import com.calicoapps.kumabudget.budget.repository.ExpensePayerRepository;
import com.calicoapps.kumabudget.budget.repository.ExpenseRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.family.entity.Family;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseDataService extends DataService<Expense, Long> {

    public static final String ENTITY_NAME = "Expense";

    private final ExpenseRepository repository;
    private final ExpensePayerRepository payerRepository;

    public ExpenseDataService(ExpenseRepository repository, ExpensePayerRepository payerRepository) {
        super(ENTITY_NAME);
        this.repository = repository;
        this.payerRepository = payerRepository;
    }

    @Override
    protected JpaRepository<Expense, Long> getRepository() {
        return repository;
    }

    public Expense save(Expense expense, List<ExpensePayer> payers) {
        int totalPercentage = 0;
        for (ExpensePayer payer : payers) {
            totalPercentage += payer.getPercentage();
        }

        if (totalPercentage < 99 || totalPercentage > 101) {
            throw new KumaException(ErrorCode.BAD_REQUEST, "Sum of all payers % is not 100%: " + totalPercentage);
        }

        payers = payerRepository.saveAll(payers);
        expense.setPayers(payers);
        return repository.save(expense);
    }

    public List<Expense> getAllOfOnePerson(Person person) {
        List<ExpensePayer> payers = payerRepository.findByPerson(person);
        return extractAllExpensesFromPayers(payers);
    }

    public List<Expense> getAllOfAFamily(Family family) {
        List<ExpensePayer> payers = payerRepository.findByPersonsInList(family.getMembers());
        return extractAllExpensesFromPayers(payers);
    }

    private List<Expense> extractAllExpensesFromPayers(List<ExpensePayer> payers) {
        Set<Expense> expenses = payers.stream().map(payer -> payer.getExpense()).collect(Collectors.toSet());
        return expenses.stream()
                .sorted(Comparator.comparing(Expense::getDate))
                .collect(Collectors.toList());
    }

}
