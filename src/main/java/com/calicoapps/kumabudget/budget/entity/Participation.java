package com.calicoapps.kumabudget.budget.entity;

import com.calicoapps.kumabudget.common.util.JsonUtil;
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
@Table(name = "budget_participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Transaction transaction;

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
