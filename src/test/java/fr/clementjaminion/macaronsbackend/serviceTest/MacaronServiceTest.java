package fr.clementjaminion.macaronsbackend.serviceTest;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import fr.clementjaminion.macaronsbackend.service.macaron_service.*;
import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class
MacaronServiceTest {
    @InjectMocks
    private MacaronManagingServiceImpl macaronManagingService;
    @InjectMocks
    private MacaronGettingServiceImpl macaronGettingService;
    @InjectMocks
    private MacaronStockManagingServiceImpl macaronStockManagingService;

    @Mock
    MacaronRepo macaronRepository;


    @Test
    void createChocolate() throws MacaronsFunctionalException {
        // Given
        Mockito.when(macaronRepository.save(new Macaron("Rose", new BigDecimal("2.01"), 10)))
                .thenReturn(new Macaron("Rose", new BigDecimal("2.01"), 10));

        // When
        MacaronDto result = macaronManagingService.createMacaron(new CreateMacaronDto("Rose", new BigDecimal("2.01"), 10));

        // Then
        Assertions.assertEquals(new MacaronDto( "Rose", new BigDecimal("2.01"), 10), result);
    }


    @Test
    void getAllChocolates_withoutResult() {
        // Given

        // When
        List<MacaronDto> result = macaronGettingService.getAllMacarons();

        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getAllChocolates_withResult() {
        // Given
        Mockito.when(macaronRepository.findAll()).thenReturn(List.of(new Macaron( "vanilla", new BigDecimal("0.50"), 10)));

        // When
        List<MacaronDto> result = macaronGettingService.getAllMacarons();

        // Then
        Assertions.assertEquals(List.of(new MacaronDto( "vanilla", new BigDecimal("0.50"), 10)), result);
    }
}
