package fr.clementjaminion.macaronsbackend.service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.metrics.CustomMetrics;
import fr.clementjaminion.macaronsbackend.models.*;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.SaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.SaleEntryDto;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import fr.clementjaminion.macaronsbackend.repositories.SaleEntryRepo;
import fr.clementjaminion.macaronsbackend.repositories.SalesRepo;
import fr.clementjaminion.macaronsbackend.repositories.SalesStatusRepository;
import fr.clementjaminion.macaronsbackend.service.macaron_service.MacaronStockManagingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    private static final String SALENOTFOUNDTEXT = "Sale not Found";
    private static final String SALENOTFOUNDCODE = "SALE_NOT_FOUND";

    private static final String STATUSNOTFOUNDTEXT = "Status not Found";
    private static final String STATUSNOTFOUNDCODE = "STATUS_NOT_FOUND";
    private final SalesRepo salesRepository;
    private final SalesStatusRepository salesStatusRepository;

    private final MacaronStockManagingService macaronStockManagingService;
    private final MacaronRepo macaronRepo;
    private final SaleEntryRepo saleEntryRepo;
    private final CustomMetrics customMetrics;

    public SalesServiceImpl(SalesRepo salesRepository, SalesStatusRepository salesStatusRepository, MacaronStockManagingService macaronStockManagingService, MacaronRepo macaronRepo, SaleEntryRepo saleEntryRepo, CustomMetrics customMetrics) {
        this.salesRepository = salesRepository;
        this.salesStatusRepository = salesStatusRepository;
        this.macaronStockManagingService = macaronStockManagingService;
        this.macaronRepo = macaronRepo;
        this.saleEntryRepo = saleEntryRepo;
        this.customMetrics = customMetrics;
    }

    @Cacheable(value = "salesPrice", key = "#saleId")
    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(int saleId) throws MacaronNotFoundException {
        return salesRepository.findById(saleId)
                .map(sales -> {
                    List<SaleEntry> saleEntries = sales.getSalesEntries();
                    if (saleEntries == null)
                        return BigDecimal.ZERO;
                    double sum = saleEntries.stream()
                            .mapToDouble(saleEntry -> saleEntry.getMacaron().getUnitPrice().doubleValue() * saleEntry.getNumberMacaron())
                            .sum();
                    return BigDecimal.valueOf(sum);
                }).orElseThrow(() -> new MacaronNotFoundException(SALENOTFOUNDTEXT, SALENOTFOUNDCODE));
    }

    @Cacheable("allSales")
    @Transactional(readOnly = true)
    public List<SaleDto> getAllSales() {
        return salesRepository.findAll().stream()
                .map(sales -> {
                    List<SaleEntryDto> salesEntries = null;
                    if (sales.getSalesEntries() != null) {
                        salesEntries = sales.getSalesEntries().stream().map(SaleEntryDto::of).toList();
                    }
                    return new SaleDto(sales, salesEntries);
                })
                .toList();
    }

    @Cacheable(value = "sales", key = "#id")
    @Transactional(readOnly = true)
    public SaleDto getOneSale(Integer id) throws MacaronNotFoundException {
        return salesRepository.findById(id)
                .map(sales -> {
                    List<SaleEntryDto> salesEntries = sales.getSalesEntries().stream().map(SaleEntryDto::of).toList();
                    return new SaleDto(sales, salesEntries);
                })
                .orElseThrow(() -> new MacaronNotFoundException(SALENOTFOUNDTEXT, SALENOTFOUNDCODE));
    }

    @CacheEvict(value = {"allSales", "sales"}, allEntries = true)
    @Transactional
    public SaleDto createSale(CreateSaleDto createSaleDto) throws MacaronNotFoundException, MacaronBadRequestException, MacaronsFunctionalException {
        //check is the number of macaron is in stock and the tastes exists
        macaronStockManagingService.verifyStock(createSaleDto.createSalesEntriesDtos());

        Sales saleCreated = this.createEmptySaleForSomeone(createSaleDto.firstnameReservation());

        List<SaleEntry> saleEntries = new ArrayList<>();
        for (CreateSaleEntryDto saleEntryDto : createSaleDto.createSalesEntriesDtos()) {
            Macaron mymacaron = macaronRepo.findByTaste(saleEntryDto.tasteMacaron())
                    .orElseThrow(() -> new MacaronsFunctionalException("Macaron not found", "MACARON_NOT_FOUND"));
            mymacaron.setStock(mymacaron.getStock() - saleEntryDto.numberOfMacarons());//reduce the stock of the macaron
            mymacaron = macaronRepo.save(mymacaron);//saving macaron with his stock updated
            SaleEntry apply = new SaleEntry(saleEntryDto.numberOfMacarons(), saleCreated, mymacaron);
            apply = saleEntryRepo.save(apply);//saving the new sale entry
            saleEntries.add(apply);
        }
        saleCreated.setSalesEntries(saleEntries);
        saleCreated.setStatus(salesStatusRepository.findById(SalesStatusEnum.WAITING)//update the status
                .orElseThrow(() -> new MacaronsFunctionalException(STATUSNOTFOUNDTEXT, STATUSNOTFOUNDCODE)));

        // Increment the custom sales creation counter for prometheus
        customMetrics.incrementSalesCreationCounter();

        return new SaleDto(salesRepository.save(saleCreated));
    }

    @CacheEvict(value = {"allSales", "sales"}, allEntries = true)
    @Transactional
    public Sales createEmptySaleForSomeone(String firstname) throws MacaronsFunctionalException {
        SalesStatus status = salesStatusRepository.findById(SalesStatusEnum.NOENTRY)
                .orElseThrow(() -> new MacaronsFunctionalException(STATUSNOTFOUNDTEXT, STATUSNOTFOUNDCODE));
        return salesRepository.save(new Sales(firstname, status));
    }

    @CacheEvict(value = {"allSales", "sales"}, allEntries = true)
    @Transactional
    public SaleDto validateSale(Integer id) throws MacaronNotFoundException, MacaronsFunctionalException {
        Sales sale = salesRepository.findById(id)
                .orElseThrow(() -> new MacaronNotFoundException(SALENOTFOUNDTEXT, SALENOTFOUNDCODE));
        sale.setStatus(salesStatusRepository.findById(SalesStatusEnum.COMPLETED)
                .orElseThrow(() -> new MacaronsFunctionalException(STATUSNOTFOUNDTEXT, STATUSNOTFOUNDCODE)));
        return new SaleDto(salesRepository.save(sale));
    }

    @CacheEvict(value = {"allSales", "sales", "salesPrice"}, allEntries = true)
    @Transactional
    public SaleDto paySale(Integer id, double paymentPaid) throws MacaronNotFoundException, MacaronsFunctionalException {
        Sales sale = salesRepository.findById(id)
                .orElseThrow(() -> new MacaronNotFoundException(SALENOTFOUNDTEXT, SALENOTFOUNDCODE));
        sale.setTotalPricePaid(BigDecimal.valueOf((sale.getTotalPricePaid().doubleValue() + paymentPaid)));
        sale = salesRepository.save(sale);
        //verification amount total paid else throw exception with keeping possibility to continue the payment
        double priceAsked = calculatePrice(id).doubleValue();
        if (sale.getTotalPricePaid().doubleValue() < priceAsked)
            throw new MacaronsFunctionalException("Amount paid is not finish please:"+sale.getTotalPricePaid().doubleValue()+" on "+priceAsked+" total", "AMOUNT_NOT_ENOUGH");
        sale.setStatus(salesStatusRepository.findById(SalesStatusEnum.PAID)
                .orElseThrow(() -> new MacaronsFunctionalException(STATUSNOTFOUNDTEXT, STATUSNOTFOUNDCODE)));
        return new SaleDto(salesRepository.save(sale));
    }
}
