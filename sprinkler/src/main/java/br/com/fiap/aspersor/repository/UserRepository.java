package br.com.fiap.aspersor.repository;

import br.com.fiap.aspersor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
