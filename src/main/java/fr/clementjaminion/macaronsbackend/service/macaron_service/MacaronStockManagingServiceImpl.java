package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.config.ExceptionUtil;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class MacaronStockManagingServiceImpl implements MacaronStockManagingService{

    private static final String MACARONNOTFOUNDTEXT = "Macaron not Found";
    private static final String MACARONNOTFOUNDCODE = "MACARON_NOT_FOUND";
    private final MacaronRepo macaronRepository;

    public MacaronStockManagingServiceImpl(MacaronRepo macaronRepository) {
        this.macaronRepository = macaronRepository;
    }

    @Override
    public void verifyStock (
            List<CreateSaleEntryDto> saleEntriesdto
    ){
        saleEntriesdto.forEach(ExceptionUtil.wrap(createSaleEntryDto -> {
            Macaron macaron = macaronRepository.findByTaste(createSaleEntryDto.tasteMacaron())
                    .orElseThrow(() -> new MacaronNotFoundException("A Macaron was not found in your order", MACARONNOTFOUNDCODE));
            if (createSaleEntryDto.numberOfMacarons() > macaron.getStock()) {
                throw new MacaronBadRequestException("Not enough macarons in stock", "NOT_ENOUGH_MACARONS");
            }
        }));
    }
    private MacaronDto updateStock(String taste, int stockDelta, Predicate<Macaron> stockCheck) throws MacaronNotFoundException {
        Macaron macaron = macaronRepository.findByTaste(taste)
                .orElseThrow(() -> new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE));
        macaron.setStock(macaron.getStock() + stockDelta);
        if (!stockCheck.test(macaron)) {
            throw new MacaronNotFoundException("Invalid stock after update", MACARONNOTFOUNDCODE);
        }
        macaron = macaronRepository.save(macaron);
        return new MacaronDto(macaron.getTaste(), macaron.getUnitPrice(), macaron.getStock());
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
    public MacaronDto addStockMacaron(String taste, int addingStock) throws MacaronNotFoundException {
        return updateStock(taste, addingStock, macaron -> macaron.getStock() >= 0);
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
    public MacaronDto reduceStockMacaron(String taste, Integer reducingStock) throws MacaronNotFoundException {
        return updateStock(taste, -reducingStock, macaron -> macaron.getStock() >= 0);
    }
}
