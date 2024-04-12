package com.calicoapps.kumabudget.budgetmodel.entity;

import com.calicoapps.kumabudget.common.Constants;
import com.calicoapps.kumabudget.common.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Entity
@Table(name = "model_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {

    @Id
    @GeneratedValue
    private Long id;

    private String label;

    @OneToMany(mappedBy = "transaction")
    private List<ParticipationModel> participations;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecurrenceType recurrenceType;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    private DayOfWeek fixedDayOfWeek;

    private int fixedDayOfMonth;

    @Enumerated(EnumType.STRING)
    private Month fixedMonth;

}
