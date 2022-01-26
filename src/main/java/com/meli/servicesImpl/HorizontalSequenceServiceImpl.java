package com.meli.servicesImpl;

import com.meli.model.sequence.SequenceHorizontal;
import com.meli.services.SequenceService;
import org.springframework.stereotype.Service;

@Service
public class HorizontalSequenceServiceImpl extends SequenceService<SequenceHorizontal> {

    public HorizontalSequenceServiceImpl() {
        super(new SequenceHorizontal());
    }

    @Override
    public int findSequence(Character[][] table) {
        for (Character[] characters : table) {
            for (int j = 0; j < table.length; j++) {
                char letter = characters[j];
                countSequences(letter);
            }
            if (hasASequence(characters)) {
                setNumberOfDnaMatches(ONE);
            }
            if (thereIsMoreThanOneSequence()) {
                break;
            }
        }
        return getNumberOfDnaMatches();
    }

}