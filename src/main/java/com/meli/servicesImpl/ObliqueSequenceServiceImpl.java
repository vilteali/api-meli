package com.meli.servicesImpl;

import com.meli.model.sequence.SequenceOblique;
import com.meli.services.SequenceService;
import org.springframework.stereotype.Service;

@Service
public class ObliqueSequenceServiceImpl extends SequenceService<SequenceOblique> {

    public ObliqueSequenceServiceImpl() {
        super(new SequenceOblique());
    }

    @Override
    public int findSequence(Character[][] table) {
        final int nLength = table.length;
        Character[] obliqueMainDiagonal = new Character[nLength];
        Character[] obliqueSecondaryDiagonal = new Character[nLength];

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (i == j) {
                    countSequences(table[i][j]);
                    fillVerticalArray(obliqueMainDiagonal, table[i][j], j);
                } if (i + j == table.length - 1) {
                    countSequences(table[i][j]);
                    fillVerticalArray(obliqueSecondaryDiagonal, table[i][j], j);
                }
            }
        }
        if (hasASequence(obliqueMainDiagonal)) {
            setNumberOfDnaMatches(ONE);
        }
        if (hasASequence(obliqueSecondaryDiagonal)) {
            setNumberOfDnaMatches(ONE);
        }
        return getNumberOfDnaMatches();
    }

    private void fillVerticalArray(Character[] array, Character letter, int position) {
        array[position] = letter;
    }

}