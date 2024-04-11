package com.calicoapps.kumabudget.budget.entity.family;

import com.calicoapps.kumabudget.budget.entity.Expense;
import com.calicoapps.kumabudget.budget.entity.TransactionType;
import com.calicoapps.kumabudget.family.Family;
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
@DiscriminatorValue(TransactionType.FAMILY)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class FamilyExpense extends Expense {

    @ManyToOne
    private Family family;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
