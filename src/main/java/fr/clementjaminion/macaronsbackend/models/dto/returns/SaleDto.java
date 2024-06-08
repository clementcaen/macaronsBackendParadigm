package fr.clementjaminion.macaronsbackend.models.dto.returns;

import fr.clementjaminion.macaronsbackend.models.Sales;
import fr.clementjaminion.macaronsbackend.models.SalesStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record SaleDto(
        int id,
        List<SaleEntryDto> saleEntryDto,
        String firstnameReservation,
        BigDecimal totalPricePaid,
        LocalDate date,
        SalesStatus status
) {

    public SaleDto(Sales sales, List<SaleEntryDto> saleEntryDtos) {
        this(sales.getId(), saleEntryDtos, sales.getFirstnameReservation(), sales.getTotalPricePaid(), sales.getDate(), sales.getStatus());
    }

    public SaleDto(Sales sales) {
        this(sales.getId(),
                Objects.requireNonNull(sales.getSalesEntries()).stream().map(SaleEntryDto::of).toList(),
             sales.getFirstnameReservation(),
             sales.getTotalPricePaid(),
             sales.getDate(),
             sales.getStatus());
    }
}
