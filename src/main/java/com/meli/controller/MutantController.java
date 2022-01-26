package com.meli.controller;

import com.meli.model.AdnStatistic;
import com.meli.dto.DnaDto;
import com.meli.model.Adn;
import com.meli.services.AdnService;
import com.meli.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api")
public class MutantController {

    @Autowired
    private AdnService adnService;
    @Autowired
    private TableService tableService;

    @PostMapping("/mutant")
    public ResponseEntity<String> verifyHuman(@RequestBody DnaDto dna) {
        if (adnService.isMutant(dna)) {
            String dnaSequence = adnService.getDnaSequence();
            Adn adn = new Adn(dnaSequence);

            if (tableService.findSequencesInTable(dna, adnService.getRowLength(), dnaSequence)) {
                adn.setMutant(true);
                adnService.saveAdn(adn);
                adnService.saveStatistic(true);
                return new ResponseEntity<>(String.format("dna: [%s] is mutant", dnaSequence), HttpStatus.OK);
            } else {
                adn.setMutant(false);
                adnService.saveAdn(adn);
                adnService.saveStatistic(false);
                return new ResponseEntity<>(String.format("dna: [%s] has no mutant matches", dnaSequence), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/stats")
    public ResponseEntity<AdnStatistic> getStatistics() {
        Optional<AdnStatistic> adnStatistic = adnService.findStatistics();

        if (adnStatistic.isPresent()) {
            AdnStatistic stats = adnStatistic.get();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        }
        return new ResponseEntity<>(AdnStatistic.empty(), HttpStatus.OK);
    }

}
