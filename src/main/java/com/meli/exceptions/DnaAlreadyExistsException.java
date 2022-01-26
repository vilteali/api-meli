package com.meli.exceptions;

public class DnaAlreadyExistsException extends RuntimeException {

    public DnaAlreadyExistsException(String dna) {
        super(String.format("dna: \"[%s]\" already exists", dna));
    }

}
