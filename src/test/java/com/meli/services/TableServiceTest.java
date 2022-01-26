package com.meli.services;

import com.meli.core.test.BaseTest;
import com.meli.dto.DnaDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.meli.utils.AdnFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class TableServiceTest extends BaseTest {

    @Autowired
    private TableService tableService;

    @Test
    void whenTheDnaHasMatches_thenTheFindSequencesIsTrue() {
        DnaDto dnaWithMatches = bySequence(ADN_VALID_WITH_MATCHES);
        String dnaSequence = toDnaSequence(dnaWithMatches.getDna());
        boolean result = tableService.findSequencesInTable(dnaWithMatches, dnaWithMatches.getDna().length, dnaSequence);

        assertThat(result).isTrue();
    }

    @Test
    void whenTheDnaHasOneMatch_thenTheFindSequencesIsFalse() {
        DnaDto dnaOneMatch = bySequence(ADN_VALID_WITH_ONE_MATCH);
        String dnaSequence = toDnaSequence(dnaOneMatch.getDna());
        boolean result = tableService.findSequencesInTable(dnaOneMatch, dnaOneMatch.getDna().length, dnaSequence);

        assertThat(result).isFalse();
    }

    @Test
    void whenTheDnaHasTwoHorizontalMatches_thenFindSequencesAreTrue() {
        DnaDto dnaTwoMatches = bySequence(ADN_HORIZONTAL_WITH_TWO_MATCHES);
        String dnaSequence = toDnaSequence(dnaTwoMatches.getDna());
        boolean result = tableService.findSequencesInTable(dnaTwoMatches, dnaTwoMatches.getDna().length, dnaSequence);

        assertThat(result).isTrue();
    }

    @Test
    void whenTheDnaHasNoMatches_thenTheFindSequencesIsFalse() {
        DnaDto dnaWithoutMatches = bySequence(ADN_VALID_WITHOUT_MATCHES);
        String dnaSequence = toDnaSequence(dnaWithoutMatches.getDna());
        boolean result = tableService.findSequencesInTable(dnaWithoutMatches, dnaWithoutMatches.getDna().length, dnaSequence);

        assertThat(result).isFalse();
    }

    @Test
    void whenTheDnaHasOneObliqueMatch_thenFindSequencesIsFalse() {
        DnaDto dnaObliqueOneMatch = bySequence(ADN_OBLIQUE_WITH_ONE_MATCH);
        String dnaSequence = toDnaSequence(dnaObliqueOneMatch.getDna());
        boolean result = tableService.findSequencesInTable(dnaObliqueOneMatch, dnaObliqueOneMatch.getDna().length, dnaSequence);

        assertThat(result).isFalse();
    }

    @Test
    void whenTheDnaHasTwoObliqueMatches_thenFindSequencesAreTrue() {
        DnaDto dnaObliqueWithTwoMatches = bySequence(ADN_OBLIQUE_WITH_TWO_MATCHES);
        String dnaSequence = toDnaSequence(dnaObliqueWithTwoMatches.getDna());
        boolean result = tableService.findSequencesInTable(dnaObliqueWithTwoMatches,
                dnaObliqueWithTwoMatches.getDna().length, dnaSequence);

        assertThat(result).isTrue();
    }

    @Test
    void whenTheDnaHasTwoVerticalMatches_thenFindSequencesAreTrue() {
        DnaDto dnaVerticalTwoMatches = bySequence(ADN_VERTICAL_WITH_TWO_MATCHES);
        String dnaSequence = toDnaSequence(dnaVerticalTwoMatches.getDna());
        boolean result = tableService.findSequencesInTable(dnaVerticalTwoMatches, dnaVerticalTwoMatches.getDna().length, dnaSequence);

        assertThat(result).isTrue();
    }

    private DnaDto bySequence(String[] sequence) {
        return new DnaDto(sequence);
    }

}