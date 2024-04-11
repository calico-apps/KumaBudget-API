package com.calicoapps.kumabudget.budget.entity.personal;

import com.calicoapps.kumabudget.budget.entity.Expense;
import com.calicoapps.kumabudget.budget.entity.TransactionType;
import com.calicoapps.kumabudget.family.Person;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(TransactionType.PERSONAL_RECURRENT)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonalRecurrentExpense extends Expense {

    private Long interval;

    @ManyToOne
    private Person person;

}