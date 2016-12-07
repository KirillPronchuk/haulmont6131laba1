package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Admin on 20.02.2016.
 */
@Entity
@Table(name = "customer")
public class Customer extends ModelItem {

    @Column(name = "name")
    private String name;
    @Column(name = "phonenum")
    private String phonenum;
    @Column(name = "adress")
    private String adress;

    public Customer() {
        this.name = "";
        this.phonenum = "";
        this.adress = "";
    }

    public Customer(String name, String phonenum, String adress) {
        this.name = name;
        this.phonenum = phonenum;
        this.adress = adress;
    }

    public Customer(long id, String name, String phonenum, String adress) {
        super(id);
        this.name = name;
        this.phonenum = phonenum;
        this.adress = adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getAdress() {
        return adress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Customer customer = (Customer) o;

        return name.equals(customer.name) && phonenum.equals(customer.phonenum) && adress.equals(customer.adress);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) getNumber();
        result = 31 * result + phonenum.hashCode();
        result = 31 * result + adress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "№ " +
                this.getNumber() +
                ";\n Имя " +
                this.getName() +
                ";\n # телефона клиента" +
                this.getPhonenum() +
                ";\n Адрес " +
                this.getAdress();
    }
}