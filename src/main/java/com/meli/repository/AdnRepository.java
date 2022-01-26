package com.meli.repository;

import com.meli.model.Adn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdnRepository extends JpaRepository<Adn, Long> {

    Optional<Adn> findByDna(String dna);
}
