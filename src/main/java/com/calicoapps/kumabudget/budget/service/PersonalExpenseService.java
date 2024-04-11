package com.calicoapps.kumabudget.budget.service;

import com.calicoapps.kumabudget.error.KumaException;
import com.calicoapps.kumabudget.budget.entity.Expense;
import com.calicoapps.kumabudget.budget.entity.personal.PersonalExpense;
import com.calicoapps.kumabudget.budget.repository.ExpenseRepository;
import com.calicoapps.kumabudget.family.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalExpenseService {

    public static final String ENTITY_NAME = "PersonalExpense";

    private final ExpenseRepository expenseRepository;

    public Optional<PersonalExpense> findPersonalExpenseById(Long id) {
        Optional<Expense> expenseOptional = expenseRepository.findById(id);
        Expense expense = KumaException.orElseThrow(expenseOptional, ENTITY_NAME, expenseOptional.toString());


        return
    }

    public PersonalExpense createPersonalExpense(PersonalExpense personalExpense) {
        return expenseRepository.save(personalExpense);
    }

    public List<PersonalExpense> getAllPersonalExpensesOfOnePerson(Person person) {

    }

}
