package com.meli.model;

import javax.persistence.*;

@Entity
public class Adn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_mutant")
    private boolean isMutant;

    @Column(unique = true)
    private String dna;

    public Adn() {}

    public Adn(String dna) {
        this.dna = dna;
    }

    public Long getId() {
        return id;
    }

    public String getDna() {
        return dna;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

    public boolean isMutant() {
        return isMutant;
    }
}
