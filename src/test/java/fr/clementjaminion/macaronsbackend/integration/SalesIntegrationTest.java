package fr.clementjaminion.macaronsbackend.integration;

import fr.clementjaminion.macaronsbackend.models.*;
import fr.clementjaminion.macaronsbackend.repositories.SalesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SalesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalesRepo salesRepository;

    @BeforeEach
    void setUp() {
        salesRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateSale() throws Exception {
        mockMvc.perform(post("/v1/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"createSalesEntriesDtos\":[],\"firstnameReservation\":\"user\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"firstnameReservation\":\"user\",\"totalPricePaid\":0}"));

        Sales sales = salesRepository.findAll().get(0);
        assertEquals("user", sales.getFirstnameReservation());
        assertEquals(BigDecimal.ZERO, sales.getTotalPricePaid());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllSales() throws Exception {
        Macaron macaronstrawberry = new Macaron("strawberry", new BigDecimal("0.50"), 10);
        Macaron macaronchocolate = new Macaron("chocolate", new BigDecimal("0.30"), 10);
        Sales sales = new Sales("user", new SalesStatus(SalesStatusEnum.WAITING));
        sales.setSalesEntries(List.of(new SaleEntry(1, sales, macaronchocolate), new SaleEntry(2, sales, macaronstrawberry)));
        Sales sales2 = new Sales("user2", new SalesStatus(SalesStatusEnum.WAITING));
        sales2.setSalesEntries(List.of(new SaleEntry(10, sales2, macaronchocolate)));
        salesRepository.save(sales);
        salesRepository.save(sales2);

        mockMvc.perform(get("/v1/sales")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"firstnameReservation\":\"user\"},{\"firstnameReservation\":\"user2\"}]"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOneSale() throws Exception {
        Macaron macaronstrawberry = new Macaron("strawberry", new BigDecimal("0.50"), 10);
        Macaron macaronchocolate = new Macaron("chocolate", new BigDecimal("0.30"), 10);
        Sales sales = new Sales("user", new SalesStatus(SalesStatusEnum.WAITING));
        sales.setSalesEntries(List.of(new SaleEntry(1, sales, macaronchocolate), new SaleEntry(2, sales, macaronstrawberry)));
        sales = salesRepository.save(sales);

        mockMvc.perform(get("/v1/sales/" + sales.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{"
                        + "\"id\":" + sales.getId() + ","
                        + "\"saleEntryDto\":["
                        + "{\"numberOfMacarons\":1,\"macaron\":{\"taste\":\"chocolate\",\"unitPrice\":0.30,\"stock\":10}},"
                        + "{\"numberOfMacarons\":2,\"macaron\":{\"taste\":\"strawberry\",\"unitPrice\":0.50,\"stock\":10}}"
                        + "],"
                        + "\"firstnameReservation\":\"user\","
                        + "\"totalPricePaid\":0,"
                        + "\"date\":\"" + sales.getDate().toString() + "\","
                        + "\"status\":{\"code\":\"WAITING\"}"
                        + "}"));
    }
}