package com.meli.exceptions;

public class MutantNotValidException extends RuntimeException {

    public MutantNotValidException(String dna) {
        super(String.format("dna: [\"%s\"] is not a from Mutant", dna));
    }

}
