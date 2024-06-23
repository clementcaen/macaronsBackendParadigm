package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;

import java.util.List;

public interface MacaronStockManagingService {
    public void verifyStock (List<CreateSaleEntryDto> saleEntriesdto) throws MacaronBadRequestException, MacaronNotFoundException;
    public MacaronDto addStockMacaron(String taste, int addingStock) throws MacaronNotFoundException;
    public MacaronDto reduceStockMacaron(String taste, Integer reducingStock) throws MacaronNotFoundException;
}
