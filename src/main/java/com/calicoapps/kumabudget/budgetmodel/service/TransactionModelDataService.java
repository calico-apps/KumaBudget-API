package com.calicoapps.kumabudget.budgetmodel.service;

import com.calicoapps.kumabudget.budgetmodel.entity.ParticipationModel;
import com.calicoapps.kumabudget.budgetmodel.entity.TransactionModel;
import com.calicoapps.kumabudget.budgetmodel.repository.ParticipationsModelRepository;
import com.calicoapps.kumabudget.budgetmodel.repository.TransactionModelRepository;
import com.calicoapps.kumabudget.common.DataService;
import com.calicoapps.kumabudget.exception.ErrorCode;
import com.calicoapps.kumabudget.exception.KumaException;
import com.calicoapps.kumabudget.family.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionModelDataService extends DataService<TransactionModel, Long> {

    public static final String ENTITY_NAME = "TransactionModel";

    private final TransactionModelRepository repository;
    private final ParticipationsModelRepository payerRepository;

    public TransactionModelDataService(TransactionModelRepository repository, ParticipationsModelRepository participationRepository) {
        super(ENTITY_NAME);
        this.repository = repository;
        this.payerRepository = participationRepository;
    }

    @Override
    protected JpaRepository<TransactionModel, Long> getRepository() {
        return repository;
    }

    public TransactionModel save(TransactionModel model, List<ParticipationModel> participations) {
        int totalPercentage = 0;
        for (ParticipationModel p : participations) {
            totalPercentage += p.getPercentage();
        }

        if (totalPercentage < 99 || totalPercentage > 101) {
            throw new KumaException(ErrorCode.BAD_REQUEST, "Sum of all participations % is not 100%: " + totalPercentage);
        }

        participations = payerRepository.saveAll(participations);
        model.setParticipations(participations);
        return repository.save(model);
    }

    public List<TransactionModel> getAllOfOnePerson(Person person) {
        List<ParticipationModel> participations = payerRepository.findByPerson(person);
        return extractAllTransactionsFromParticipationsList(participations);
    }

    private List<TransactionModel> extractAllTransactionsFromParticipationsList(List<ParticipationModel> participations) {
        Set<TransactionModel> transactions = participations.stream().map(p -> p.getTransaction()).collect(Collectors.toSet());
        return transactions.stream()
                .sorted(Comparator.comparing(TransactionModel::getStartDate))
                .collect(Collectors.toList());
    }

}
