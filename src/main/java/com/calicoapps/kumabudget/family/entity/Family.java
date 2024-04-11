package com.calicoapps.kumabudget.family.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_families")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Family {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "family")
    private List<Person> members;

}