package fr.clementjaminion.macaronsbackend.integration;

import fr.clementjaminion.macaronsbackend.models.Macaron;
import fr.clementjaminion.macaronsbackend.repositories.MacaronRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MacaronIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MacaronRepo macaronRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateMacaron() throws Exception {
        mockMvc.perform(post("/manage/v1/macarons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"taste\": \"rose\", \"price\": 1.99, \"stock\": 100 }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"taste\": \"rose\", \"unitPrice\": 1.99, \"stock\": 100 }"));

        Macaron macaron = macaronRepository.findByTaste("rose").orElseThrow();
        assertEquals(new BigDecimal("1.99"), macaron.getUnitPrice());
        assertEquals(100, macaron.getStock());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreatemacaronwithoutproperAuth() throws Exception {
        mockMvc.perform(post("/manage/v1/macarons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"taste\": \"rose\", \"price\": 1.99, \"stock\": 100 }"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddStockMacaron() throws Exception {
        Macaron macaron = new Macaron("raspberry", new BigDecimal("1.99"), 100);
        macaronRepository.save(macaron);

        mockMvc.perform(put("/manage/v1/macarons/raspberry/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"taste\": \"raspberry\", \"unitPrice\": 1.99, \"stock\": 110 }"));

        Macaron updatedMacaron = macaronRepository.findByTaste("raspberry").orElseThrow();
        assertEquals(110, updatedMacaron.getStock());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetMacaron() throws Exception {
        Macaron macaron = new Macaron("raspberry", new BigDecimal("1.99"), 100);
        macaronRepository.save(macaron);

        mockMvc.perform(get("/v1/macarons/raspberry")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"taste\": \"raspberry\", \"unitPrice\": 1.99, \"stock\": 100 }"));
    }
}
