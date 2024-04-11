package com.calicoapps.kumabudget.budgetmodel.entity;

import com.calicoapps.kumabudget.common.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "model_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseModel {

    @Id
    @GeneratedValue
    private Long id;

    private String label;

    @Column(nullable = false)
    private Double amount;

    @OneToMany(mappedBy = "expense")
    private List<ExpensePayerModel> payers = new ArrayList<>();

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate endDate;

    private Integer factor;


}
