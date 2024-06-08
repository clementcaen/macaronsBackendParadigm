package fr.clementjaminion.macaronsbackend.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.clementjaminion.macaronsbackend.controller.MacaronController;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.service.MacaronService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(MacaronController.class)
class MacaronControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MacaronService macaronService;

    @Test
    void creationMacaron() throws Exception {
        // Given
        Mockito.when(macaronService.createMacaron(new CreateMacaronDto("fraise", new BigDecimal("3.00"), 100)))
                .thenReturn(new MacaronDto( "fraise", new BigDecimal("3.00"), 100));

        // When
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/macaron")
                        .content(new ObjectMapper().writeValueAsString(new CreateMacaronDto("fraise", new BigDecimal("3.00"), 100)))
                        .contentType("application/json")
        ).andReturn().getResponse();

        // Then
        Assertions.assertEquals(201, response.getStatus());
        MacaronDto macaronDto = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(new MacaronDto( "fraise", new BigDecimal("3.00"), 100), macaronDto);
    }
//    @Test
//    void creationEmptyMacaron() throws Exception {//test of param mandatory validation
//        // Given
//
//        // When
//        MockHttpServletResponse response = mockMvc.perform(
//                MockMvcRequestBuilders.post("/v1/macaron")
//                        .content("{}")
//                        .contentType("application/json")
//        ).andReturn().getResponse();
//        // Then
//        Assertions.assertEquals(400, response.getStatus());
//        Map<String, String> errors = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
//        });
//        Assertions.assertEquals(Map.of("taste", "Taste is mandatory", "price", "Price cannot be null"), errors);
//    }
//    @Test
//    void creationnegativePriceMacaron() throws Exception {//test of param mandatory validation
//        // Given
//
//        // When
//        MockHttpServletResponse response = mockMvc.perform(
//                MockMvcRequestBuilders.post("/v1/macaron")
//                        .content(new ObjectMapper().writeValueAsString(new CreateMacaronDto("cassis", new BigDecimal("-1.00"))))
//                        .contentType("application/json")
//        ).andReturn().getResponse();
//        // Then
//        Assertions.assertEquals(400, response.getStatus());
//        Map<String, String> errors = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
//        });
//        Assertions.assertEquals(Map.of("price", "Price must be positive"), errors);
//    }

    @Test
    void getAllChocolates_emptyList() throws Exception {
        // Given
        Mockito.when(macaronService.getAllMacarons()).thenReturn(List.of());

        // When
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/macarons")
                        .contentType("application/json")
        ).andReturn().getResponse();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        List<MacaronDto> result = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getAllChocolates_withList() throws Exception {
        // Given
        Mockito.when(macaronService.getAllMacarons()).thenReturn(List.of(new MacaronDto( "Fraise", new BigDecimal("1.74"), 100)));

        // When
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/macarons")
                        .contentType("application/json")
        ).andReturn().getResponse();

        // Then
        Assertions.assertEquals(200, response.getStatus());
        List<MacaronDto> result = new ObjectMapper().readValue(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(List.of(new MacaronDto( "Fraise", new BigDecimal("1.74"), 100)), result);
    }

}
