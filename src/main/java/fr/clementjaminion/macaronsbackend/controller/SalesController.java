package fr.clementjaminion.macaronsbackend.controller;

import fr.clementjaminion.macaronsbackend.exceptions.MacaronBadRequestException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronNotFoundException;
import fr.clementjaminion.macaronsbackend.exceptions.MacaronsFunctionalException;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.MacaronDto;
import fr.clementjaminion.macaronsbackend.models.dto.returns.SaleDto;
import fr.clementjaminion.macaronsbackend.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @Operation(summary = "Get all sales", description = "Get all sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all sales", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/sales")
    public List<SaleDto> getAllSales() {
        return salesService.getAllSales();
    }

    @Operation(summary = "Get one sale", description = "Get one sale by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get one sale", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("v1/sales/{id}")
    public SaleDto getOneSale(
            @PathVariable Integer id
    ) throws MacaronNotFoundException {
        return salesService.getOneSale(id);
    }

    @Operation(summary = "Create a sale", description = "Create a sale with a list of macarons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the sale", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Macaron not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("v1/sales")
    public SaleDto createSale(
            @Valid @RequestBody CreateSaleDto createSaleDto
            ) throws MacaronsFunctionalException, MacaronBadRequestException, MacaronNotFoundException {
        return salesService.createSale(createSaleDto);
    }

    @Operation(summary = "Calculate the price of a sale", description = "Calculate the price of a sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculate the price of a sale", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("v1/sales/{saleId}/price")
    public BigDecimal calculatePrice(
            @PathVariable Integer saleId
    ) throws MacaronNotFoundException {
        return salesService.calculatePrice(saleId);
    }

    @Operation(summary = "Pay a sale", description = "Complete the status of a sale after the payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pay the sale", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "amount not enough", content = @Content),
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("v1/sales/{id}/pay")
    public SaleDto paySale(
            @PathVariable Integer id,
            @RequestParam double paymentPaid
    ) throws MacaronNotFoundException, MacaronsFunctionalException {
        return salesService.paySale(id, paymentPaid);
    }


    @Operation(summary = "Validate a sale", description = "Complete the status of a sale after the payment and delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validate the sale", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MacaronDto.class))}),
            @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("v1/sales/{id}/validate")
    public SaleDto validateSale(
            @PathVariable Integer id
    ) throws MacaronNotFoundException, MacaronsFunctionalException {
        return salesService.validateSale(id);
    }

}
