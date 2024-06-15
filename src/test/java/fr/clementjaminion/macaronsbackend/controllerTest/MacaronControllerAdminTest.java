package fr.clementjaminion.macaronsbackend.controllerTest;

import fr.clementjaminion.macaronsbackend.controller.MacaronControllerAdmin;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.service.macaron_service.MacaronService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(MacaronControllerAdmin.class)
class MacaronControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MacaronService macaronService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateMacaron() throws Exception {
        //Given
        CreateMacaronDto createMacaronDto = new CreateMacaronDto("raspberry", BigDecimal.valueOf(1.99), 100);
        MacaronDto macaronDto = new MacaronDto("raspberry", BigDecimal.valueOf(1.99), 100);
        //mocking the service to just test the controller
        Mockito.when(macaronService.createMacaron(createMacaronDto))
                .thenReturn(macaronDto);

        //when
        mockMvc.perform(post("/manage/v1/macarons")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"taste\": \"raspberry\", \"price\": 1.99, \"stock\": 100 }"))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"taste\": \"raspberry\", \"unitPrice\": 1.99, \"stock\": 100 }"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateMacaronWithEmptyBody() throws Exception {
        mockMvc.perform(post("/manage/v1/macarons")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddStockMacaron() throws Exception {
        MacaronDto macaronDto = new MacaronDto("raspberry", BigDecimal.valueOf(1.99), 210);

        Mockito.when(macaronService.addStockMacaron("raspberry", 10))
                .thenReturn(macaronDto);

        mockMvc.perform(put("/manage/v1/macarons/raspberry/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"taste\": \"raspberry\", \"unitPrice\": 1.99, \"stock\": 210 }"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteMacaron() throws Exception {
        mockMvc.perform(delete("/manage/v1/macarons/raspberry")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
