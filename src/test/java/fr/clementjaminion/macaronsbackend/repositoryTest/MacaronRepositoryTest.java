package fr.clementjaminion.macaronsbackend.repositoryTest;

import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

@DataJpaTest
class MacaronRepositoryTest {

    @Autowired
    private MacaronRepo macaronRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void save() {
        // Given
        Macaron macaron = new Macaron("strawberry", new BigDecimal("1.30"), 10);

        // When
        Macaron savedMacaron = macaronRepository.save(macaron);
        Macaron foundMacaron = testEntityManager.find(Macaron.class, savedMacaron.getTaste());

        // Then
        Assertions.assertNotNull(savedMacaron.getTaste());
        Assertions.assertEquals(macaron.getTaste(), foundMacaron.getTaste());
        Assertions.assertEquals(macaron.getUnitPrice(), foundMacaron.getUnitPrice());
        Assertions.assertEquals(macaron.getStock(), foundMacaron.getStock());
    }
}
