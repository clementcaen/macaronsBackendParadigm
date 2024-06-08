package fr.clementjaminion.macaronsbackend.repositories;

import fr.clementjaminion.macaronsbackend.models.Macaron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MacaronRepo extends JpaRepository<Macaron, Integer> {

    Optional<Macaron> findByTaste(String taste);
    boolean existsByTaste(String taste);
    void deleteByTaste(String taste);
}
