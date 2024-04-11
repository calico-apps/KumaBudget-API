package com.calicoapps.kumabudget.expense.entity;

import com.calicoapps.kumabudget.family.FamilyMember;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue(ExpenseType.PERSONAL)
public abstract class PersonalExpense extends Expense {

    @ManyToOne
    private FamilyMember familyMember;

}