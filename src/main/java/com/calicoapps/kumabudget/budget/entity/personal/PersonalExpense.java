package com.calicoapps.kumabudget.budget.entity.personal;

import com.calicoapps.kumabudget.budget.entity.Expense;
import com.calicoapps.kumabudget.budget.entity.TransactionType;
import com.calicoapps.kumabudget.family.Person;
import com.calicoapps.kumabudget.util.JsonUtil;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(TransactionType.PERSONAL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonalExpense extends Expense {

    @ManyToOne
    private Person person;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}