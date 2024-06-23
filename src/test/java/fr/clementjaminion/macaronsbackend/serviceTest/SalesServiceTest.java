package fr.clementjaminion.macaronsbackend.serviceTest;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.*;
import fr.clementjaminion.macaronsbackend.models.dto.returns.SaleDto;
import fr.clementjaminion.macaronsbackend.repositories.SalesRepo;
import fr.clementjaminion.macaronsbackend.service.SalesService;
import fr.clementjaminion.macaronsbackend.service.SalesServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SalesServiceTest {

    @Mock
    private SalesRepo salesRepository;

    @InjectMocks
    private SalesServiceImpl salesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculatePrice() throws MacaronNotFoundException {
        //Given
        Macaron macaronvanilla = new Macaron("vanilla", new BigDecimal("0.50"), 10);
        Macaron macaronchocolate = new Macaron("chocolate", new BigDecimal("0.30"), 10);
        Sales sales = new Sales("John Doe", new SalesStatus(SalesStatusEnum.WAITING));
        sales.setSalesEntries(List.of(new SaleEntry(1, sales, macaronchocolate), new SaleEntry(2, sales, macaronvanilla)));
        //total price = 0.30 + 2*0.50 = 1.30
        when(salesRepository.findById(1)).thenReturn(Optional.of(sales));

        BigDecimal price = salesService.calculatePrice(1);

        assertEquals(BigDecimal.valueOf(1.30), price);
    }

    @Test
    void testGetOneSale() throws MacaronNotFoundException {
        SalesStatus statusWaiting = new SalesStatus(SalesStatusEnum.WAITING);
        Sales sales = new Sales("John Doe", statusWaiting);
        sales.setSalesEntries(List.of(new SaleEntry(1, sales, new Macaron("vanilla", new BigDecimal("0.50"), 10))));
        when(salesRepository.findById(1)).thenReturn(Optional.of(sales));

        SaleDto saleDto = salesService.getOneSale(1);

        assertEquals("John Doe", saleDto.firstnameReservation());
        assertEquals(statusWaiting, saleDto.status());
        assertEquals(1, saleDto.saleEntryDto().size());
        assertEquals("vanilla", saleDto.saleEntryDto().get(0).macaron().taste());
    }

    @AfterEach
    void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }
}