package fr.clementjaminion.macaronsbackend.repositories;

import fr.clementjaminion.macaronsbackend.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepo extends JpaRepository<Sales, Integer> {

}
