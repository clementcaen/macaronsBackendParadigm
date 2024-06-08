package fr.clementjaminion.macaronsbackend.models.dto.command;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateSaleDto(
        List<CreateSaleEntryDto> createSalesEntriesDtos,
        @NotNull
        String firstnameReservation
){
    public String toJson() {
        return "{"
                + "\"createSalesEntriesDtos\":" + createSalesEntriesDtos.stream().map(CreateSaleEntryDto::toJson).toList()
                + ",\"firstnameReservation\":\"" + firstnameReservation + "\""
                + "}";
    }
}
