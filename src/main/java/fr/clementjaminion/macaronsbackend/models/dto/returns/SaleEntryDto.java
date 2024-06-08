package fr.clementjaminion.macaronsbackend.models.dto.returns;

import fr.clementjaminion.macaronsbackend.models.SaleEntry;

public record SaleEntryDto(
        int numberOfMacarons,
        MacaronDto macaron
) {
    public static SaleEntryDto of(SaleEntry saleEntry) {
        return new SaleEntryDto(
            saleEntry.getNumberMacaron(),
                MacaronDto.of(saleEntry.getMacaron())
        );
    }
}
