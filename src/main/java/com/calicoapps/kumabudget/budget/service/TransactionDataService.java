package com.calicoapps.kumabudget.budget.service;

import com.calicoapps.kumabudget.budget.entity.Participation;
import com.calicoapps.kumabudget.budget.entity.Transaction;
import com.calicoapps.kumabudget.budget.repository.ParticipationRepository;
import com.calicoapps.kumabudget.budget.repository.TransactionRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.family.entity.Family;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionDataService extends DataService<Transaction, Long> {

    public static final String ENTITY_NAME = "Transaction";

    private final TransactionRepository repository;
    private final ParticipationRepository participationRepository;

    public TransactionDataService(TransactionRepository repository, ParticipationRepository participationRepository) {
        super(ENTITY_NAME);
        this.repository = repository;
        this.participationRepository = participationRepository;
    }

    @Override
    protected JpaRepository<Transaction, Long> getRepository() {
        return repository;
    }

    public Transaction save(Transaction transaction, List<Participation> participations) {

        int totalPercentage = 0;
        for (Participation p : participations) {
            totalPercentage += p.getPercentage();
        }

        if (totalPercentage < 99 || totalPercentage > 101) {
            throw new KumaException(ErrorCode.BAD_REQUEST, "Sum of all participations % is not 100%: " + totalPercentage);
        }

        participations = participationRepository.saveAll(participations);
        transaction.setParticipations(participations);
        return repository.save(transaction);
    }

    public List<Transaction> getAllOfOnePerson(Person person) {
        List<Participation> payers = participationRepository.findByPerson(person);
        return extractAllTransactionsFromParticipationsList(payers);
    }

    public List<Transaction> getAllOfAFamily(Family family) {
        List<Participation> payers = participationRepository.findByPersonsInList(family.getMembers());
        return extractAllTransactionsFromParticipationsList(payers);
    }

    private List<Transaction> extractAllTransactionsFromParticipationsList(List<Participation> participations) {
        Set<Transaction> transactions = participations.stream().map(p -> p.getTransaction()).collect(Collectors.toSet());
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .collect(Collectors.toList());
    }

}
