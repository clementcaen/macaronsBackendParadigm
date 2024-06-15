package fr.clementjaminion.macaronsbackend.controller;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.service.macaron_service.MacaronGettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MacaronController {
    private final MacaronGettingService macaronGettingService;

    public MacaronController(MacaronGettingService macaronGettingService) {
        this.macaronGettingService = macaronGettingService;
    }

    @Operation(summary = "Get all macarons", description = "Get all macarons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all macarons", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/macarons")
    public List<MacaronDto> getAllMacarons() {
        return macaronGettingService.getAllMacarons();
    }

    @Operation(summary = "Get one macaron", description = "Get one macaron by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get one macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/macarons/{taste}")
    public MacaronDto getOneMacaron(@PathVariable String taste) throws MacaronNotFoundException {
        return macaronGettingService.getOneMacaron(taste);
    }


}
