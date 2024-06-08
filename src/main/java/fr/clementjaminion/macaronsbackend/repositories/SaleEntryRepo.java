package fr.clementjaminion.macaronsbackend.repositories;

import fr.clementjaminion.macaronsbackend.models.SaleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleEntryRepo extends JpaRepository<SaleEntry, Integer> {

}
