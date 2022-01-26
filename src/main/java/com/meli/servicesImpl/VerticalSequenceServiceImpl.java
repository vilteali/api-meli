package com.meli.servicesImpl;

import com.meli.model.sequence.SequenceVertical;
import com.meli.services.SequenceService;
import org.springframework.stereotype.Service;

@Service
public class VerticalSequenceServiceImpl extends SequenceService<SequenceVertical> {

    public VerticalSequenceServiceImpl() {
        super(new SequenceVertical());
    }

    @Override
    public int findSequence(Character[][] table) {
        Character[] verticalArray = new Character[table.length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                char letter = table[j][i];
                countSequences(letter);
                fillVerticalArray(verticalArray, letter, j);
            }
            if (hasASequence(verticalArray)) {
                setNumberOfDnaMatches(ONE);
            }
            if (thereIsMoreThanOneSequence()) {
                break;
            }
        }
        return getNumberOfDnaMatches();
    }

    private void fillVerticalArray(Character[] array, Character letter, int position) {
        array[position] = letter;
    }
}
