package fr.clementjaminion.macaronsbackend.serviceTest;


import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import fr.clementjaminion.macaronsbackend.service.MacaronService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MacaronAndCacheTest {
    @InjectMocks
    private MacaronService macaronService;

    @Mock
    MacaronRepo macaronRepository;

    @Test
    void getAllMacaronsInCache(){//le cache ne fonctionne pas et je n'ai pas trouvé pourquoi
        // Given
        Mockito.when(macaronRepository.findAll()).thenReturn(List.of());

        // When
        for (int i = 0; i < 3; i++) {
            macaronService.getAllMacarons();
        }

        // Then
        //verify that the method findAll() is called only once
        Mockito.verify(macaronRepository, Mockito.times(1)).findAll();
    }
}

