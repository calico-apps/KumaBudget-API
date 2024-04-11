package com.calicoapps.kumabudget.expense.entity;

import com.calicoapps.kumabudget.family.FamilyMember;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue(ExpenseType.PERSONAL_RECURRENT)
public abstract class PersonalRecurrentExpense extends Expense {

    private Long interval;

    @ManyToOne
    private FamilyMember familyMember;

}