package DAO;

import model.Order;

import javax.ejb.Stateless;

/**
 * Created by Admin on 19.12.2016.
 */
@Stateless
public class OrderDAO extends AbstractJPA_DAO<Order> {
    public OrderDAO(){
        super(Order.class);
    }
}
