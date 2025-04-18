package br.com.fiap.aspersor.repository;

import br.com.fiap.aspersor.model.Sprinkler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinklerRepository extends JpaRepository<Sprinkler, Long> {
}
