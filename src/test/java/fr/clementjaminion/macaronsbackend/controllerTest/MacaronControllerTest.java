package fr.clementjaminion.macaronsbackend.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.clementjaminion.macaronsbackend.controller.MacaronController;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.service.macaron_service.MacaronService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MacaronController.class)
class MacaronControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MacaronService macaronService;

    @Test
    @WithMockUser(username = "John Doe", roles = {"USER"})
    void testGetAllMacarons() throws Exception {
        Mockito.when(macaronService.getAllMacarons())
                .thenReturn(List.of(new MacaronDto("framboise", new BigDecimal("0.50"), 10)));

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/macarons")
                        .contentType("application/json")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        List<MacaronDto> result = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
        });

    }

}
