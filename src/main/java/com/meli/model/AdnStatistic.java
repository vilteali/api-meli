package com.meli.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class AdnStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count_mutant_dna")
    private int countMutantDna;

    @Column(name = "count_human_dna")
    private int countHumanDna;

    @Column
    private BigDecimal ratio;

    public AdnStatistic() {}

    public AdnStatistic(Long id) {
        this.id = id;
    }

    public AdnStatistic(int countMutantDna, int countHumanDna, BigDecimal ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public static AdnStatistic empty() {
        return new AdnStatistic(0, 0, BigDecimal.ZERO);
    }

    public int getCountMutantDna() {
        return countMutantDna;
    }

    public void setCountHumanDna(int countHumanDna) {
        this.countHumanDna = countHumanDna;
    }

    public int getCountHumanDna() {
        return countHumanDna;
    }

    public void setCountMutantDna(int countMutantDna) {
        this.countMutantDna = countMutantDna;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void calculateRatio(int countMutantDna, int countHumanDna) {
        if (countHumanDna == 0) {
            this.ratio = BigDecimal.ZERO;
        } else {
            this.ratio = BigDecimal.valueOf(countMutantDna)
                    .divide(BigDecimal.valueOf(countHumanDna), 2, RoundingMode.HALF_UP);
        }
    }
}
