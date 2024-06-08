package fr.clementjaminion.macaronsbackend.repositoryTest;

import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

@DataJpaTest
public class MacaronRepositoryTest {

    private final MacaronRepo macaronRepository;

    private final TestEntityManager testEntityManager;

    public MacaronRepositoryTest(MacaronRepo macaronRepository, TestEntityManager testEntityManager) {
        this.macaronRepository = macaronRepository;
        this.testEntityManager = testEntityManager;
    }

    @Test
    void save() {
        // When
        Macaron framboiseMacaron = macaronRepository.save(new Macaron("Framboise", new BigDecimal("1.30"), 10));
        Macaron framboiseMacaronInDB = testEntityManager.find(Macaron.class, 1);

        // Then
        Assertions.assertEquals(new Macaron( "Framboise", new BigDecimal("1.30"), 10), framboiseMacaron);
        Assertions.assertEquals(new Macaron("Framboise", new BigDecimal("1.30"), 10), framboiseMacaronInDB);
    }
}
