package com.meli.controller;

import com.google.gson.Gson;
import com.meli.core.test.BaseTest;
import com.meli.dto.DnaDto;
import com.meli.exceptions.ExceptionApiHandler;
import com.meli.exceptions.MutantNotValidException;
import com.meli.model.AdnStatistic;
import com.meli.services.AdnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.meli.utils.AdnFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MutantControllerTest extends BaseTest {

    private static final String PATH = "/api";

    @Autowired
    private MutantController mutantController;
    @Autowired
    private AdnService adnService;

    private MockMvc mockMvc;
    private Gson mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mutantController)
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
        mapper = new Gson();
    }

    @Test
    void validatePostWithDnaValid() {
        try {
            DnaDto dnaDto = new DnaDto(ADN_VALID_WITH_MATCHES);
            String jsonExpected = toJson(dnaDto);
            mockMvc.perform(post(PATH+"/mutant")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonExpected))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void validatePostWithDnaInValid() {
        try {
            DnaDto dnaDto = new DnaDto(ADN_INVALID);
            String expectedMessage = String.format("dna: [\"%s\"] is not a from Mutant", toDnaSequence(dnaDto.getDna()));
            String jsonExpected = toJson(dnaDto);
            mockMvc.perform(post(PATH+"/mutant")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonExpected))
                    .andExpect(status().isForbidden())
                    .andExpect(result -> {
                        assertTrue(result.getResolvedException() instanceof MutantNotValidException);
                        assertEquals(expectedMessage, result.getResolvedException().getMessage());
                    })
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void validatePostWithDnaValidButWithoutMatches() {
        try {
            DnaDto dnaDto = new DnaDto(ADN_VALID_WITHOUT_MATCHES);
            String expectedMessage = String.format("dna: [%s] has no mutant matches", toDnaSequence(dnaDto.getDna()));
            String jsonExpected = toJson(dnaDto);
            String response = mockMvc.perform(post(PATH+"/mutant")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonExpected))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertEquals(expectedMessage, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void validateGetWithEmptyData() {
        try {
            String response = mockMvc.perform(get(PATH+"/stats"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            AdnStatistic adnStatisticEmpty = mapper.fromJson(response, AdnStatistic.class);

            assertThat(adnStatisticEmpty.getCountMutantDna()).isZero();
            assertThat(adnStatisticEmpty.getCountHumanDna()).isZero();
            assertThat(adnStatisticEmpty.getRatio()).isZero();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void validateGetWithDataMutant() {
        adnService.saveStatistic(true);
        try {
            String response = mockMvc.perform(get(PATH+"/stats"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            AdnStatistic adnStatistic = mapper.fromJson(response, AdnStatistic.class);

            assertThat(adnStatistic.getCountMutantDna()).isOne();
            assertThat(adnStatistic.getCountHumanDna()).isZero();
            assertThat(adnStatistic.getRatio()).isZero();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void validateGetWithDataHuman() {
        adnService.saveStatistic(false);
        try {
            String response = mockMvc.perform(get(PATH+"/stats"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            AdnStatistic adnStatistic = mapper.fromJson(response, AdnStatistic.class);

            assertThat(adnStatistic.getCountMutantDna()).isZero();
            assertThat(adnStatistic.getCountHumanDna()).isOne();
            assertThat(adnStatistic.getRatio()).isZero();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toJson(Object o) {
        return mapper.toJson(o);
    }

}