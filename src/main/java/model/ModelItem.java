package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Admin on 20.02.2016.
 */

@MappedSuperclass
public abstract class ModelItem implements Serializable, Cloneable {
    @Id
    @GenericGenerator(name = "kaugen", strategy = "increment")
    @GeneratedValue(generator = "kaugen" , strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long number;

    public ModelItem() {

    }

    ModelItem(long id) {
        number = id;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public abstract ModelItem clone();
}


