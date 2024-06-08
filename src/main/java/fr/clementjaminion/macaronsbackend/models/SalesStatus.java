package fr.clementjaminion.macaronsbackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sales_status")
public class SalesStatus {
    @Id
    @Enumerated(EnumType.STRING)
    private SalesStatusEnum code;

    public SalesStatus() {
    }

    public SalesStatus(SalesStatusEnum code) {
        this.code = code;
    }

    public SalesStatusEnum getCode() {
        return code;
    }

    public void setCode(SalesStatusEnum code) {
        this.code = code;
    }
}
