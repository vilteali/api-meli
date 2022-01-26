package com.meli.services;

import com.meli.model.sequence.ISequenceType;
import com.meli.model.sequence.SequenceTable;
import com.meli.model.sequence.SequenceType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import static com.meli.model.sequence.SequenceTable.DEFAULT_VALUE;
import static com.meli.model.sequence.SequenceType.OBLIQUE;

@Service
public abstract class SequenceService<TYPE extends ISequenceType> {

    private final static Logger LOGGER = Logger.getLogger(SequenceService.class.getName());

    private final TYPE sequence;
    private final SequenceTable sequenceTable;
    protected final int SEQUENCE_MINIMUM = 3;
    protected final int ONE = 1;

    protected SequenceService(TYPE sequenceType) {
        this.sequence = sequenceType;
        this.sequenceTable = new SequenceTable(sequenceType.type());
    }

    protected boolean hasASequence(Character[] letters) {
        if (hasMatchesByLetter()) {
            String array = Arrays.toString(letters);
            LOGGER.info("Has sequence with array: "+array);
            int count = 0;
            for (int i = 0; i < letters.length; i++) {
                char letter = letters[i];
                int index = i+1 < letters.length ? i+1 : i;
                if (i + 1 < letters.length && letter == letters[index]) {
                    count++;
                } else if (count > 0 && count != SEQUENCE_MINIMUM) {
                    count = 0;
                } else if (letters.length - i+2 < SEQUENCE_MINIMUM || count == SEQUENCE_MINIMUM) {
                    break;
                }
            }
            LOGGER.info(String.format("%s dna matches found from %s", count>1 ? count+1 : 0, array));
            return count >= SEQUENCE_MINIMUM;
        }
        return false;
    }

    protected boolean hasMatchesByLetter() {
        var valuePerCharacter = this.sequenceTable.getMatchesPerLetter()
                .entrySet().stream().flatMap(map -> map.getValue().entrySet()
                        .stream()
                        .filter(this::byType))
                .mapToInt(Map.Entry::getValue)
                .max();

        if (getSequenceType() != OBLIQUE) {
            this.cleanValues();
        }
        return valuePerCharacter.isPresent() && valuePerCharacter.getAsInt() > SEQUENCE_MINIMUM;
    }

    public void cleanValues() {
        this.sequenceTable.getMatchesPerLetter().values()
                .forEach(key -> key.entrySet()
                .forEach(e -> e.setValue(DEFAULT_VALUE)));
    }

    private boolean byType(Map.Entry<SequenceType, Integer> sequenceType) {
        return sequenceType.getKey() == getSequenceType();
    }

    protected void countSequences(Character character) {
        sequenceTable.getMatchesPerLetter().computeIfPresent(character, (key, value) -> {
            if (character == key) {
                SequenceType sequenceType = getSequenceType();
                value.put(sequenceType, value.get(sequenceType) + ONE);
            }
            return value;
        });
    }

    protected boolean thereIsMoreThanOneSequence() {
        if (sequenceTable.getNumberOfDnaMatches() > ONE) {
            cleanValues();
            return true;
        }
        return sequenceTable.getNumberOfDnaMatches() > ONE;
    }

    public int getNumberOfDnaMatches() {
        return sequenceTable.getNumberOfDnaMatches();
    }

    public void setNumberOfDnaMatches(int numberOfDnaMatches) {
        int value = this.sequenceTable.getNumberOfDnaMatches() + numberOfDnaMatches;
        this.sequenceTable.setNumberOfDnaMatches(value);
    }

    public void resetNroMatches() {
        this.sequenceTable.setNumberOfDnaMatches(0);
    }

    public SequenceType getSequenceType() {
        return sequence.type();
    }

    protected abstract int findSequence(Character[][] table);

}