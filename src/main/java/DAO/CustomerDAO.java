package DAO;

import model.Customer;

import javax.ejb.Stateless;

/**
 * Created by Admin on 19.12.2016.
 */
@Stateless
public class CustomerDAO extends AbstractJPA_DAO<Customer> {
    public CustomerDAO(){
        super(Customer.class);
    }
}
