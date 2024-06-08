package fr.clementjaminion.macaronsbackend.repositories;

import fr.clementjaminion.macaronsbackend.models.SalesStatus;
import fr.clementjaminion.macaronsbackend.models.SalesStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesStatusRepository extends JpaRepository<SalesStatus, SalesStatusEnum> {
}
