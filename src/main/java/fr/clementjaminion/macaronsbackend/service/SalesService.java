package fr.clementjaminion.macaronsbackend.service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.*;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.SaleDto;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface SalesService {

    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(int saleId) throws MacaronNotFoundException;

    @Transactional(readOnly = true)
    public List<SaleDto> getAllSales();

    @Transactional(readOnly = true)
    public SaleDto getOneSale(Integer id) throws MacaronNotFoundException;


    public SaleDto createSale(CreateSaleDto createSaleDto) throws MacaronNotFoundException, MacaronBadRequestException, MacaronsFunctionalException;

    @Transactional
    public Sales createEmptySaleForSomeone(String firstname) throws MacaronsFunctionalException;


    @Transactional
    public SaleDto validateSale(Integer id) throws MacaronNotFoundException, MacaronsFunctionalException;


    @Transactional
    public SaleDto paySale(Integer id, double paymentPaid) throws MacaronNotFoundException, MacaronsFunctionalException;
}
