package fr.clementjaminion.macaronsbackend.models.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateSaleEntryDto(
        @NotNull
        String tasteMacaron,
        @NotNull @Min(1)
        int numberOfMacarons
){
        public String toJson() {
                return "{"
                        + "\"tasteMacaron\":\"" + tasteMacaron + "\""
                        + ",\"numberOfMacarons\":" + numberOfMacarons
                        + "}";
        }
}
