package com.meli.services;

import com.meli.model.AdnStatistic;
import com.meli.dto.DnaDto;
import com.meli.exceptions.DnaAlreadyExistsException;
import com.meli.exceptions.MutantNotValidException;
import com.meli.model.Adn;
import com.meli.repository.AdnRepository;
import com.meli.repository.AdnStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AdnService {

    private final static Logger LOGGER = Logger.getLogger(AdnService.class.getName());

    private final List<Character> NITROGENOUS_BASE_OF_DNA = List.of('A', 'T', 'C', 'G');
    private final Long DEFAULT_ID = 1L;
    private int rowLength = 0;
    private String dnaSequence;

    @Autowired
    private AdnRepository adnRepository;
    @Autowired
    private AdnStatisticRepository adnStatisticRepository;

    public boolean isMutant(DnaDto dna) {
        LOGGER.info("Validating if the sequence is correct...");
        dnaSequence = toDnaSequence(dna.getDna());
        if (!verifyDna(dna)) {
            throw new MutantNotValidException(dnaSequence);
        } else if (dnaAlreadyExists(dnaSequence)) {
            throw new DnaAlreadyExistsException(dnaSequence);
        }
        LOGGER.info(String.format("Dna [\"%s\"] is valid.", dnaSequence));
        return true;
    }

    public void saveAdn(Adn adn) {
        adnRepository.findByDna(adn.getDna())
                .ifPresentOrElse(value -> {
                    throw new DnaAlreadyExistsException(value.getDna());
                }, () -> adnRepository.save(adn));
        LOGGER.info(String.format("Dna [\"%s\"] was saved successfully.", dnaSequence));
    }

    public void saveStatistic(boolean isMutant) {
        LOGGER.info("Saving statistic...");
        Optional<AdnStatistic> maybeAdnStatistic = findStatistics();
        if (maybeAdnStatistic.isPresent()) {
            AdnStatistic adnStatsUpdate = updateAdnStatistic(maybeAdnStatistic.get(), isMutant);
            adnStatisticRepository.save(adnStatsUpdate);
        } else {
            AdnStatistic adnStatistic = updateAdnStatistic(new AdnStatistic(DEFAULT_ID), isMutant);
            adnStatisticRepository.save(adnStatistic);
        }
    }

    private AdnStatistic updateAdnStatistic(AdnStatistic statisticUpdate, boolean isMutant) {
        if (isMutant) statisticUpdate.setCountMutantDna(statisticUpdate.getCountMutantDna()+1);
        else statisticUpdate.setCountHumanDna(statisticUpdate.getCountHumanDna()+1);

        statisticUpdate.calculateRatio(statisticUpdate.getCountMutantDna(), statisticUpdate.getCountHumanDna());
        return statisticUpdate;
    }

    public Optional<AdnStatistic> findStatistics() {
        return adnStatisticRepository.findById(DEFAULT_ID);
    }

    private boolean dnaAlreadyExists(String adn) {
        return adnRepository.findByDna(adn).isPresent();
    }

    private boolean verifyDna(DnaDto dna) {
        return Arrays.stream(dna.getDna())
                .allMatch(row -> areRowsOfTheSameLength(row, rowLength) && containsNitrogenousBase(row));
    }

    private boolean areRowsOfTheSameLength(String rowOfDna, int rowLength) {
        if (rowLength == 0) {
            setRowLength(rowOfDna.length());
        }
        return rowOfDna.length() >= 4 && getRowLength() == rowOfDna.length();
    }

    private boolean containsNitrogenousBase(String rowOfDna) {
        int count = 0;
        for (Character letter : rowOfDna.toCharArray()) {
            boolean containsNitrogenous = NITROGENOUS_BASE_OF_DNA.contains(letter);
            if (containsNitrogenous) {
                count++;
            }
        }
        return count == rowOfDna.length();
    }

    public int getRowLength() {
        return rowLength;
    }

    private void setRowLength(int rowLength) {
        this.rowLength = rowLength;
    }

    public String getDnaSequence() {
        return dnaSequence;
    }

    public String toDnaSequence(String[] dna) {
       return String.join("-" , dna);
    }

}