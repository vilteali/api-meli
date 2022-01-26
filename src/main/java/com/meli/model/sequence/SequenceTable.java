package com.meli.model.sequence;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SequenceTable {

    public static final int DEFAULT_VALUE = 0;
    private final Map<Character, EnumMap<SequenceType, Integer>> matchesPerLetter;
    private final SequenceType sequenceType;
    private int numberOfDnaMatches;

    public SequenceTable(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
        this.matchesPerLetter = createMatchesByLetter();
    }

    private Map<Character, EnumMap<SequenceType, Integer>> createMatchesByLetter() {
        final Map<Character, EnumMap<SequenceType, Integer>> matchesPerLetter;
        matchesPerLetter = new HashMap<>() {
            {
                put('A', createEnumMap());
                put('T', createEnumMap());
                put('C', createEnumMap());
                put('G', createEnumMap());
            }
        };
        return matchesPerLetter;
    }

    private EnumMap<SequenceType, Integer> createEnumMap() {
        EnumMap<SequenceType, Integer> enumMap = new EnumMap<>(SequenceType.class);
        enumMap.put(sequenceType, DEFAULT_VALUE);
        return enumMap;
    }

    public Map<Character, EnumMap<SequenceType, Integer>> getMatchesPerLetter() {
        return matchesPerLetter;
    }

    public int getNumberOfDnaMatches() {
        return numberOfDnaMatches;
    }

    public void setNumberOfDnaMatches(int numberOfDnaMatches) {
        this.numberOfDnaMatches = numberOfDnaMatches;
    }
}