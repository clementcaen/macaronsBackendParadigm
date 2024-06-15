package fr.clementjaminion.macaronsbackend.service.macaron_service;

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
    ) throws MacaronBadRequestException, MacaronNotFoundException {
        for (CreateSaleEntryDto createSaleEntryDto : saleEntriesdto) {
            Optional<Macaron> macaronOptional = macaronRepository.findByTaste(createSaleEntryDto.tasteMacaron());
            if (macaronOptional.isPresent()){
                if(createSaleEntryDto.numberOfMacarons() > macaronOptional.get().getStock()) {
                    throw new MacaronBadRequestException("Not enough macarons in stock", "NOT_ENOUGH_MACARONS");
                }
            }else {
                throw new MacaronNotFoundException("A Macaron was not found in your order", MACARONNOTFOUNDCODE);
            }
        }
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
    public MacaronDto addStockMacaron(String taste, int addingStock) throws MacaronNotFoundException {
        Optional<Macaron> macaronOptional = macaronRepository.findByTaste(taste);
        if (macaronOptional.isEmpty()) {
            throw new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE);
        }
        Macaron macaron = macaronOptional.get();
        macaron.setStock(macaron.getStock() + addingStock);
        macaron = macaronRepository.save(macaron);
        return new MacaronDto(macaron.getTaste(), macaron.getUnitPrice(), macaron.getStock());
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
    public MacaronDto reduceStockMacaron(String taste, Integer reducingStock) throws MacaronNotFoundException {
        Optional<Macaron> macaronOptional = macaronRepository.findByTaste(taste);
        if (macaronOptional.isEmpty()) {
            throw new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE);
        }
        Macaron macaron = macaronOptional.get();
        macaron.setStock(macaron.getStock() - reducingStock);
        macaron = macaronRepository.save(macaron);
        return new MacaronDto(macaron.getTaste(), macaron.getUnitPrice(), macaron.getStock());
    }
}
