package fr.clementjaminion.macaronsbackend.models;

public enum SalesStatusEnum {
    NOENTRY,
    WAITING,//not paid or cancelled
    CANCELLED,
    PAID,
    COMPLETED//paid and delivered
}
