package com.calicoapps.kumabudget.expense.entity;

import com.calicoapps.kumabudget.family.Family;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue(ExpenseType.FAMILY)
public abstract class FamilyExpense extends Expense {

    @ManyToOne
    private Family family;

}
