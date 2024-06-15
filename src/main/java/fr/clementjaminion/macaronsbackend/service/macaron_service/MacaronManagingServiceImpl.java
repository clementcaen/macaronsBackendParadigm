package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.ModifyMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MacaronManagingServiceImpl implements MacaronManagingService{
    private final MacaronRepo macaronRepository;
    private static final String MACARONNOTFOUNDTEXT = "Macaron not Found";
    private static final String MACARONNOTFOUNDCODE = "MACARON_NOT_FOUND";

    public MacaronManagingServiceImpl(MacaronRepo macaronRepository) {
        this.macaronRepository = macaronRepository;
    }


    @CacheEvict(value = {"macarons", "allMacarons"}, allEntries = true)
    @Override
    public MacaronDto createMacaron(CreateMacaronDto createMacaronDto) throws MacaronsFunctionalException {
        if (macaronRepository.existsByTaste(createMacaronDto.taste())) {
            throw new MacaronsFunctionalException("duplicate taste", "DUPLICATE_TASTE");
        }
        Macaron newMacaron = new Macaron(createMacaronDto.taste(), createMacaronDto.price(), createMacaronDto.stock());
        newMacaron = macaronRepository.save(newMacaron);
        return new MacaronDto(newMacaron.getTaste(), newMacaron.getUnitPrice(), newMacaron.getStock());

    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
    public void deleteOneMacaron(String taste) {
        macaronRepository.deleteByTaste(taste);
    }

    @CacheEvict(value = {"macarons", "allMacarons"}, key = "#taste")
    @Override
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
}
