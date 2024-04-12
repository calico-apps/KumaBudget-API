package com.calicoapps.kumabudget.budgetmodel.entity;

import java.time.LocalDate;

public enum Frequency {

    DAY {
        @Override
        public LocalDate nextOccurrence(LocalDate from, int interval) {
            return from.plusDays(interval);
        }
    },
    WEEK {
        @Override
        public LocalDate nextOccurrence(LocalDate from, int interval) {
            return from.plusWeeks(interval);
        }
    },
    MONTH {
        @Override
        public LocalDate nextOccurrence(LocalDate from, int interval) {
            return from.plusMonths(interval);
        }
    },
    YEAR {
        @Override
        public LocalDate nextOccurrence(LocalDate from, int interval) {
            return from.plusYears(interval);
        }
    };

    public abstract LocalDate nextOccurrence(LocalDate from, int interval);

}
