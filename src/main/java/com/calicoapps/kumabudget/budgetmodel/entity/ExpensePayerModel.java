package com.calicoapps.kumabudget.budgetmodel.entity;

import com.calicoapps.kumabudget.common.JsonUtil;
import com.calicoapps.kumabudget.family.entity.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "model_payers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpensePayerModel {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ExpenseModel expense;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Person person;

    @Column(nullable = false)
    private Integer percentage;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
