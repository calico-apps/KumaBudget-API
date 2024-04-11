package com.calicoapps.kumabudget.budget.entity;

import com.calicoapps.kumabudget.budgetmodel.entity.ExpenseModel;
import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "budget_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "expense")
    private List<ExpensePayer> payers = new ArrayList<>();

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate date;

    @ManyToOne
    private ExpenseModel model;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
