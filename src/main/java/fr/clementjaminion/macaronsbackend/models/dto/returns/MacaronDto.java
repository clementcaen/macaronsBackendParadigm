package fr.clementjaminion.macaronsbackend.models.dto.returns;

import fr.clementjaminion.macaronsbackend.models.Macaron;

import java.math.BigDecimal;

public record MacaronDto(
        String taste,
        BigDecimal unitPrice,
        int stock
) {
    public static MacaronDto of(Macaron macaron) {
        return new MacaronDto(
                macaron.getTaste(),
                macaron.getUnitPrice(),
                macaron.getStock()
        );
    }

}
