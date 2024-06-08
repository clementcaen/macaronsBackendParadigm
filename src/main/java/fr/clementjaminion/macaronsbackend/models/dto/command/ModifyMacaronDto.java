package fr.clementjaminion.macaronsbackend.models.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ModifyMacaronDto(
        @Min(value = 0, message = "Price must be positive")
        @NotNull(message = "Price cannot be null")
        BigDecimal price,
        @Min(value = 0, message = "Stock must be positive")
        @NotNull(message = "Stock cannot be null")
        int stock
){

}
