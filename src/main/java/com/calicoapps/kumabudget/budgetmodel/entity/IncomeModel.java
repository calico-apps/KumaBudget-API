package com.calicoapps.kumabudget.budgetmodel.entity;

import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.family.entity.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.time.LocalDate;

@Entity
@Table(name = "model_incomes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeModel {

    @Id
    @GeneratedValue
    private Long id;

    private String label;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Person person;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate endDate;

    private Integer factor;

}
