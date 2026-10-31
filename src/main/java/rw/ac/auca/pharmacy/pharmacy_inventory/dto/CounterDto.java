package rw.ac.auca.pharmacy.pharmacy_inventory.dto;

import java.util.UUID;

public class CounterDto {
    private UUID id;
    private int counterNumber;
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(int counterNumber) {
        this.counterNumber = counterNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
