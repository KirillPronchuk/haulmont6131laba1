package model;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Created by Admin on 20.02.2016.
 */
@Entity
@Table(name = "orders")
public class Order extends ModelItem {

    @Column(name = "customerid")
    private long customernum;
    @Column(name = "tariffid")
    private long tariffnum;
    @Column(name = "orderdate")
    @Convert(converter = utils.LocalDateConverter.class)
    private LocalDate date;


    public Order() {
        this.customernum = 0;
        this.tariffnum = 0;
        this.date = LocalDate.now();
    }

    public Order(long id, long customernum, long tariffnum) {
        super(id);
        this.customernum = customernum;
        this.tariffnum = tariffnum;
        this.date = LocalDate.now();
    }

    public Order(long customernum, long tariffnum, LocalDate date) {
        this.customernum = customernum;
        this.tariffnum = tariffnum;
        this.date = date;
    }

    public Order(long id, long customernum, long tariffnum, LocalDate date) {
        super(id);
        this.customernum = customernum;
        this.tariffnum = tariffnum;
        this.date = date;
    }

    public void setCustomernum(long customernum) {
        this.customernum = customernum;
    }

    public void setTariffnum(long tariffnum) {
        this.tariffnum = tariffnum;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getCustomernum() {
        return customernum;
    }

    public long getTariffnum() {
        return tariffnum;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;

        if (customernum != order.customernum) {
            return false;
        }
        if (tariffnum != order.tariffnum) {
            return false;
        }
        return date.equals(order.date);

    }

    @Override
    public int hashCode() {
        int result = (int) customernum;
        result = 31 * result + (int) tariffnum;
        result = 31 * result + (int) getNumber();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        java.util.Date parsedate = new java.util.Date();
        try {
            parsedate = df.parse(date.toString());
        } catch (ParseException e) {
            Logger.getLogger(Order.class).error(e.getMessage(), e);
        }
        return "№ " +
                this.getNumber() +
                "\n Абонент № " +
                this.getCustomernum() +
                "\n Тариф № " +
                this.getTariffnum() +
                "\n Дата заключения " +
                df.format(parsedate);
    }

    @Override
    public Order clone(){
        return new Order(this.getCustomernum(),this.getTariffnum(), this.getDate());
    }
}
