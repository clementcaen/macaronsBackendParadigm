package fr.clementjaminion.macaronsbackend.service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.ModifyMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MacaronService {
    private static final String MACARONNOTFOUNDTEXT = "Macaron not Found";
    private static final String MACARONNOTFOUNDCODE = "MACARON_NOT_FOUND";

    private final MacaronRepo macaronRepository;

    public MacaronService(MacaronRepo macaronRepository) {
        this.macaronRepository = macaronRepository;
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, allEntries = true)
    public MacaronDto createMacaron(
            CreateMacaronDto createMacaronDto
    ) throws MacaronsFunctionalException {
        if (macaronRepository.existsByTaste(createMacaronDto.taste())) {
            throw new MacaronsFunctionalException("duplicate taste", "DUPLICATE_TASTE");
        }
        Macaron newMacaron = new Macaron(createMacaronDto.taste(), createMacaronDto.price(), createMacaronDto.stock());
        newMacaron = macaronRepository.save(newMacaron);
        return new MacaronDto(newMacaron.getTaste(), newMacaron.getUnitPrice(), newMacaron.getStock());

    }
    @Cacheable("allMacarons")
    public List<MacaronDto> getAllMacarons() {
        return macaronRepository.findAll().stream()
                .map(MacaronDto::of)
                .toList();
    }

    @Cacheable(value = "macarons", key = "#taste")
    public MacaronDto getOneMacaron(String taste) throws MacaronNotFoundException {
        return macaronRepository.findByTaste(taste)
                .map(MacaronDto::of)
                .orElseThrow( () -> new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE));
    }

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
    public void deleteOneMacaron(String taste) {
        macaronRepository.deleteByTaste(taste);
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    public MacaronDto updateMacaron(String taste, ModifyMacaronDto modifyMacaronDto) throws MacaronNotFoundException {
        Optional<Macaron> macaronOptional = macaronRepository.findByTaste(taste);
        if (macaronOptional.isEmpty()) {
            throw new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE);
        }
        Macaron macaron = macaronOptional.get();
        if (modifyMacaronDto.price() != null) {
            macaron.setUnitPrice(modifyMacaronDto.price());
        }
        macaron.setStock(modifyMacaronDto.stock());
        macaron = macaronRepository.save(macaron);
        return new MacaronDto(macaron.getTaste(), macaron.getUnitPrice(), macaron.getStock());
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
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
