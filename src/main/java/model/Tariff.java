package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Admin on 19.02.2016.
 */
@Entity
@Table(name = "tariff")
public class Tariff extends ModelItem {

    @Column(name = "name")
    private String name;
    @Column(name = "speed")
    private double speed;
    @Column(name = "cost")
    private double cost;

    public Tariff() {
        this.name = "";
        this.speed = 0;
        this.cost = 0;
    }

    public Tariff(long id, String name, double speed, double cost) {
        super(id);
        this.name = name;
        this.speed = speed;
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tariff tariff = (Tariff) o;

        if (Double.compare(tariff.speed, speed) != 0) {
            return false;
        }
        if (Double.compare(tariff.cost, cost) != 0) {
            return false;
        }
        return name.equals(tariff.name);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + (int) getNumber();
        temp = Double.doubleToLongBits(speed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "№ " +
                this.getNumber() +
                ";\n Название " +
                this.getName() +
                ";\n Скорость " +
                this.getSpeed() +
                ";\n Цена " +
                this.getCost();
    }

}
