package com.meli.services;

import com.meli.core.test.BaseTest;
import com.meli.dto.DnaDto;
import com.meli.exceptions.DnaAlreadyExistsException;
import com.meli.exceptions.MutantNotValidException;
import com.meli.model.Adn;
import com.meli.model.AdnStatistic;
import com.meli.repository.AdnRepository;
import com.meli.utils.AdnFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.meli.utils.AdnFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AdnServiceTest extends BaseTest {

    @Autowired
    private AdnService adnService;
    @Autowired
    private AdnRepository adnRepository;

    private Adn adnValid;

    @BeforeEach
    void setUp() {
        adnValid = AdnFixture.adnValid();
        adnRepository.save(adnValid);
    }

    @Test
    void whenThereAreNoStatistics_thenItIsSavedAsHuman() {
        Optional<AdnStatistic> result = adnService.findStatistics();
        adnService.saveStatistic(false);
        Optional<AdnStatistic> expected = adnService.findStatistics();

        assertThat(result.isPresent()).isFalse();
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get().getCountHumanDna()).isOne();
        assertThat(expected.get().getCountMutantDna()).isZero();
    }

    @Test
    void whenThereAreNoStatisticsButHasSequences_thenItIsSavedAsMutant() {
        Optional<AdnStatistic> result = adnService.findStatistics();
        adnService.saveStatistic(true);
        Optional<AdnStatistic> expected = adnService.findStatistics();

        assertThat(result.isPresent()).isFalse();
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get().getCountHumanDna()).isZero();
        assertThat(expected.get().getCountMutantDna()).isOne();
    }

    @Test
    void whenThereAreSequences_thenItIsSavedAsMutant() {
        adnService.saveStatistic(true);
        Optional<AdnStatistic> result = adnService.findStatistics();

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getCountMutantDna()).isOne();
        assertThat(result.get().getCountHumanDna()).isZero();
    }

    @Test
    void whenThereAreNoNoStatistics_thenItIsSavedAsMutant() {
        Optional<AdnStatistic> result = adnService.findStatistics();
        adnService.saveStatistic(true);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void whenThereIsNotDnaSaved_thenItsSavedSuccessfully() {
        Adn adnValid = AdnFixture.adnValid(ADN_VALID_VER_HOR_TWO_MATCHES);
        adnService.saveAdn(adnValid);

        assertNotNull(adnRepository.getById(adnValid.getId()));
    }

    @Test
    void whenVerifyIfIsAMutantWithSequenceValid_thenDoNotThrowAnException() {
        DnaDto dnaDto = toDto(AdnFixture.adnValid(ADN_VALID_WITH_ONE_MATCH).getDna());
        boolean result = adnService.isMutant(dnaDto);

        assertThat(result).isTrue();
    }

    @Test()
    void whenVerifyIfIsAMutantButAlreadyExists_thenAnExceptionIsThrown() {
        String expectedMessage = String.format("dna: \"[%s]\" already exists", adnValid.getDna());
        DnaDto dnaDto = toDto(adnValid.getDna());
        Exception exception = assertThrows(DnaAlreadyExistsException.class,
                () -> adnService.isMutant(dnaDto));

        assertThat(exception).isInstanceOf(DnaAlreadyExistsException.class).hasMessage(expectedMessage);
    }

    @Test
    void whenTheDnaIfExists_thenAnExceptionIsThrown() {
        String expectedMessage = String.format("dna: \"[%s]\" already exists", adnValid.getDna());
        assertThatThrownBy(() -> adnService.saveAdn(adnValid))
                .isInstanceOf(DnaAlreadyExistsException.class)
                .hasMessage(expectedMessage);
    }

    @Test
    void whenTheSequenceAdnIsInvalid_thenAnExceptionIsThrown() {
        DnaDto dnaDto = new DnaDto(ADN_INVALID);
        String expectedMessage = toDnaSequence(dnaDto.getDna());
        assertThatThrownBy(() -> adnService.isMutant(dnaDto))
                .isInstanceOf(MutantNotValidException.class)
                .hasMessage(String.format("dna: [\"%s\"] is not a from Mutant", expectedMessage));
    }

    @Test
    void whenTheSequenceAdnDoesNotContainNitrogenous_thenAnExceptionIsThrown() {
        DnaDto dnaDto = new DnaDto(ADN_INVALID_WITHOUT_NITROGENOUS);
        String expectedMessage = toDnaSequence(dnaDto.getDna());
        assertThatThrownBy(() -> adnService.isMutant(dnaDto))
                .isInstanceOf(MutantNotValidException.class)
                .hasMessage(String.format("dna: [\"%s\"] is not a from Mutant", expectedMessage));
    }

    @Test
    void whenTheSequenceHasOneMoreLetter_thenAnExceptionIsThrown() {
        DnaDto dnaDto = new DnaDto(ADN_INVALID_2);
        String expectedMessage = toDnaSequence(dnaDto.getDna());
        assertThatThrownBy(() -> adnService.isMutant(dnaDto))
                .isInstanceOf(MutantNotValidException.class)
                .hasMessage(String.format("dna: [\"%s\"] is not a from Mutant", expectedMessage));
    }

}