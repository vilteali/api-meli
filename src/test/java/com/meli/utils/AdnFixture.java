package com.meli.utils;

import com.meli.dto.DnaDto;
import com.meli.model.Adn;

public class AdnFixture {

    public static final String[] ADN_VALID_WITH_MATCHES = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    public static final String[] ADN_VALID_VER_HOR_TWO_MATCHES = {"ATCGAT", "AGGATG", "ACCTGA", "ACCCCT", "GTACGT", "TGCAGT"};
    public static final String[] ADN_VALID_WITHOUT_MATCHES = {"ATCGAT", "AGGAAG", "ACCTGA", "ACGCCA", "GTACGT", "TGCAGT"};
    public static final String[] ADN_VALID_WITH_ONE_MATCH = {"TGCAGT", "CAAGTA", "GTACGC", "TTAACC", "GAGGTT", "AAGGGG"};
    public static final String[] ADN_HORIZONTAL_WITH_TWO_MATCHES = {"TGCAGT", "CAAGTA", "GTACGC", "TTAACC", "GATTTT", "AAGGGG"};

    public static final String[] ADN_INVALID = {"ATCGA3", "AGGATG", "ACCTGA", "ACCCCT", "GTACGT", "TGCAGT"};
    public static final String[] ADN_INVALID_2 = {"ATCGA3", "AGGATG", "ACCTGA", "ACCCCT", "GTACGT", "TGCAG"};
    public static final String[] ADN_INVALID_WITHOUT_NITROGENOUS = {"PVRGV3", "DSQYLX", "PYRRSA", "XXZCNM", "0XDGGT", "IYULDS"};

    public static final String[] ADN_OBLIQUE_WITH_ONE_MATCH = {"ATGCGA", "CAGTTC", "TTATGT", "ACAACG", "AACGCA", "TCACTG"};
    public static final String[] ADN_OBLIQUE_WITH_TWO_MATCHES = {"ATGCGT", "CAGTTC", "TTATGT", "ACTACG", "AACGCA", "TCACTG"};

    public static final String[] ADN_VERTICAL_WITH_TWO_MATCHES = {"ATGCGA", "CTGTGC", "TTATGT", "ATAAGG", "CCGCTA", "TCACTG"};

    public static Adn adnValid() {
        return new Adn(toDnaSequence(ADN_VALID_WITH_MATCHES));
    }

    public static Adn adnValid(String[] adn) {
        return new Adn(toDnaSequence(adn));
    }

    public static DnaDto toDto(String adn) {
        return new DnaDto(toArraySequence(adn));
    }

    public static String toDnaSequence(String[] dna) {
        return String.join("-" , dna);
    }

    public static String[] toArraySequence(String dna) {
        String[] dnaArr = dna.split("-");
        String[] newArr = new String[dna.split("-").length];
        System.arraycopy(dnaArr, 0, newArr, 0, dnaArr.length);
        return newArr;
    }
}
