package fr.clementjaminion.macaronsbackend.service.macaron_service;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MacaronGettingServiceImpl implements MacaronGettingService{

    private static final String MACARONNOTFOUNDTEXT = "Macaron not Found";
    private static final String MACARONNOTFOUNDCODE = "MACARON_NOT_FOUND";

    private final MacaronRepo macaronRepository;

    public MacaronGettingServiceImpl(MacaronRepo macaronRepository) {
        this.macaronRepository = macaronRepository;
    }

    @Cacheable("allMacarons")
    @Override
    public List<MacaronDto> getAllMacarons() {
        return macaronRepository.findAll().stream()
                .map(MacaronDto::of)
                .toList();
    }

    @Cacheable(value = "macarons", key = "#taste")
    @Override
    public MacaronDto getOneMacaron(String taste) throws MacaronNotFoundException {
        return macaronRepository.findByTaste(taste)
                .map(MacaronDto::of)
                .orElseThrow( () -> new MacaronNotFoundException(MACARONNOTFOUNDTEXT, MACARONNOTFOUNDCODE));
    }
}
