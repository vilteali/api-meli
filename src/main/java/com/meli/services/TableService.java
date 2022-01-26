package com.meli.services;

import com.meli.dto.DnaDto;
import com.meli.servicesImpl.HorizontalSequenceServiceImpl;
import com.meli.servicesImpl.ObliqueSequenceServiceImpl;
import com.meli.servicesImpl.VerticalSequenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TableService {

    private final static Logger LOGGER = Logger.getLogger(TableService.class.getName());

    @Autowired
    private HorizontalSequenceServiceImpl horizontalSequenceService;
    @Autowired
    private VerticalSequenceServiceImpl verticalSequenceService;
    @Autowired
    private ObliqueSequenceServiceImpl obliqueSequenceService;

    public boolean findSequencesInTable(DnaDto dna, int n, String dnaSequence) {
        LOGGER.info(String.format("Validating if it's a mutant with DNA: [\"%s\"]", dnaSequence));
        Character[][] table = createTable(dna.getDna(), n);

        int numberOfDnaMatches = horizontalSequenceService.findSequence(table);
        if (numberOfDnaMatches <= 1) numberOfDnaMatches += verticalSequenceService.findSequence(table);
        if (numberOfDnaMatches <= 1) numberOfDnaMatches += obliqueSequenceService.findSequence(table);

        resetCount(numberOfDnaMatches);

        return numberOfDnaMatches > 1;
    }

    private void resetCount(int nroOfDnaMatches) {
        if (nroOfDnaMatches >= 1) {
            horizontalSequenceService.resetNroMatches();
            verticalSequenceService.resetNroMatches();
            obliqueSequenceService.resetNroMatches();
        }
    }

    private Character[][] createTable(String[] dna, int n) {
        String[] arrayDividedByComma = String.join(",", dna).split(",");
        Character[][] table = new Character[n][n];

        for (int row = 0; row < table.length; row++) {
            for (int column = 0; column < table[row].length; column++) {
                table[row][column] = arrayDividedByComma[row].charAt(column);
            }
        }
        return table;
    }

}