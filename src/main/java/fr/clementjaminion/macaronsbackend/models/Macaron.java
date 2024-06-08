package fr.clementjaminion.macaronsbackend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "macaron")
public class Macaron {
    @Id
    private String taste;

    private BigDecimal unitPrice;

    private int stock;

    public Macaron() {
    }

    public Macaron(String taste, BigDecimal unitPrice, int stock) {
        this.taste = taste;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }


    public String getTaste() {
        return taste;
    }

    public void setTaste(String gout) {
        this.taste = gout;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal tarif) {
        this.unitPrice = tarif;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macaron macaron = (Macaron) o;
        return stock == macaron.stock && Objects.equals(taste, macaron.taste) && Objects.equals(unitPrice, macaron.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taste, unitPrice, stock);
    }

    @Override
    public String toString() {
        return "Macaron{" +
                "taste='" + taste + '\'' +
                ", unitPrice=" + unitPrice +
                ", stock=" + stock +
                '}';
    }
}
