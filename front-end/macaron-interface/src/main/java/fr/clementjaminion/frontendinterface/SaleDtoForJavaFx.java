package fr.clementjaminion.frontendinterface;

import fr.clementjaminion.macaronsbackend.models.SalesStatus;

import java.util.List;

public record SaleDtoForJavaFx
    (
            int id,
            List<SaleEntryDtoForJavaFx> saleEntryDto,
            String firstnameReservation,
            double totalPricePaid,
            String date,
            SalesStatus status
    ) {
}
