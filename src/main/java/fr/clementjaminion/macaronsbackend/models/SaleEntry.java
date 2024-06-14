package fr.clementjaminion.macaronsbackend.models;

import jakarta.persistence.*;


@Entity
@IdClass(SaleEntryId.class)
@Table(name = "sale_entry")
public class SaleEntry {
    @Id
    @ManyToOne
    @JoinColumn(name = "sales_id")
    private Sales sales;
    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "macaron_taste")
    private Macaron macaron;
    private int numberMacaron;


    public SaleEntry(int numberMacaron, Sales sales, Macaron macaron) {
        this.numberMacaron = numberMacaron;
        this.sales = sales;
        this.macaron = macaron;
    }

    public SaleEntry() {

    }


    public Macaron getMacaron() {
        return macaron;
    }

    public int getNumberMacaron() {
        return numberMacaron;
    }
}
