package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.ModifyMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;

public interface MacaronManagingService {
    public MacaronDto createMacaron(
            CreateMacaronDto createMacaronDto) throws MacaronsFunctionalException;

    public void deleteOneMacaron(String taste);

    public MacaronDto updateMacaron(String taste, ModifyMacaronDto modifyMacaronDto) throws MacaronNotFoundException;
}
