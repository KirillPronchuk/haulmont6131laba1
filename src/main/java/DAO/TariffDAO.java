package DAO;

import model.Tariff;

import javax.ejb.Stateless;

/**
 * Created by Admin on 19.12.2016.
 */
@Stateless
public class TariffDAO extends AbstractJPA_DAO<Tariff> {
    public TariffDAO(){
        super(Tariff.class);
    }
}
