package fr.clementjaminion.macaronsbackend.models;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nullable
    @OneToMany(mappedBy = "sales", orphanRemoval = true)
    private List<SaleEntry> salesEntries;
    private String firstnameReservation;
    private BigDecimal totalPricePaid;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "status_code")
    @Enumerated(EnumType.STRING)
    private SalesStatus status;

    public Sales() {
    }

    public Sales(String firstnameReservation, SalesStatus status) {
        this.firstnameReservation = firstnameReservation;
        this.totalPricePaid = BigDecimal.ZERO;
        this.date = LocalDate.now();
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public List<SaleEntry> getSalesEntries() {
        return salesEntries;
    }

    public String getFirstnameReservation() {
        return firstnameReservation;
    }

    public BigDecimal getTotalPricePaid() {
        return totalPricePaid;
    }

    public LocalDate getDate() {
        return date;
    }

    public SalesStatus getStatus() {
        return status;
    }

    public void setTotalPricePaid(BigDecimal totalPricePaid) {
        this.totalPricePaid = totalPricePaid;
    }

    public void setSalesEntries(@Nullable List<SaleEntry> saleEntries) {
        this.salesEntries = saleEntries;
    }

    public void setStatus(SalesStatus status) {
        this.status = status;
    }

}
