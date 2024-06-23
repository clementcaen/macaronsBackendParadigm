package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;

import java.util.List;

public interface MacaronGettingService {
    public List<MacaronDto> getAllMacarons();

    public MacaronDto getOneMacaron(String taste) throws MacaronNotFoundException;
}
