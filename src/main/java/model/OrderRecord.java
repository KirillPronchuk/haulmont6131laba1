package model;

import java.time.LocalDate;

/**
 * Created by Vladislav on 27.04.2016.
 */
public class OrderRecord {

    private long number;

    private String customerName;

    private String tariffName;

    private LocalDate date;

    public OrderRecord() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }
}
