package fr.clementjaminion.macaronsbackend.controller;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.ModifyMacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.service.MacaronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MacaronController {

    private final MacaronService macaronService;


    public MacaronController(MacaronService macaronService) {
        this.macaronService = macaronService;
    }


    @Operation(summary = "Creation of a Macaron", description = "Create a macaron with a taste and a price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "409", description = "Duplicate taste", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing taste", content = @Content),
            @ApiResponse(responseCode = "400", description = "Price null of negative", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("v1/macarons")
    public MacaronDto creationMacaron(
            @Valid @RequestBody CreateMacaronDto validcreateMacaronDto
    ) throws MacaronsFunctionalException {
        return macaronService.createMacaron(validcreateMacaronDto);
    }

    @Operation(summary = "Update a macaron", description = "Update a macaron with a taste, a price and a stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing taste", content = @Content),
            @ApiResponse(responseCode = "400", description = "Price null of negative", content = @Content),
            @ApiResponse(responseCode = "400", description = "Stock null of negative", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("v1/macarons/{taste}")
    public MacaronDto updateMacaron(@PathVariable String taste, @Valid @RequestBody ModifyMacaronDto validmodifyMacaronDto) throws MacaronNotFoundException {
        return macaronService.updateMacaron(taste, validmodifyMacaronDto);
    }

    @Operation(summary = "Adding some stock to a macaron", description = "Add some stock to a macaron")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added stock to the macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Stock null of negative", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("v1/macarons/{taste}/add")
    public MacaronDto addStockMacaron(@PathVariable String taste, @Valid @RequestBody Integer addingStock) throws MacaronNotFoundException {
        return macaronService.addStockMacaron(taste, addingStock);
    }

    @Operation(summary = "Get all macarons", description = "Get all macarons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all macarons", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/macarons")
    public List<MacaronDto> getAllMacarons() {
        return macaronService.getAllMacarons();
    }

    @Operation(summary = "Get one macaron", description = "Get one macaron by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get one macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/macarons/{taste}")
    public MacaronDto getOneMacaron(@PathVariable String taste) throws MacaronNotFoundException {
        return macaronService.getOneMacaron(taste);
    }
    @Operation(summary = "Delete one type of macaron", description = "Delete one macaron by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete one macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("v1/macarons/{taste}")
    public void deleteOneMacaron(@PathVariable String taste) {
        macaronService.deleteOneMacaron(taste);
    }

    @Operation(summary = "Reduce the stock of a macaron", description = "Reduce the stock of a macaron")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reduced stock of the macaron", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Stock null of negative", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("v1/macarons/{taste}/reduce")
    public MacaronDto reduceStockMacaron(@PathVariable String taste, @Valid @RequestBody Integer reducingStock) throws MacaronNotFoundException {
        return macaronService.reduceStockMacaron(taste, reducingStock);
    }

}
