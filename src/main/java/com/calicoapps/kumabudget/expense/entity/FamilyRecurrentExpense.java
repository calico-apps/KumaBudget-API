package com.calicoapps.kumabudget.expense.entity;

import com.calicoapps.kumabudget.family.Family;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue(ExpenseType.FAMILY_RECURRENT)
public class FamilyRecurrentExpense extends Expense {

    private Long interval;

    @ManyToOne
    private Family family;

}
