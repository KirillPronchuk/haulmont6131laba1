package ui.windowdialog;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import model.Customer;
import model.Tariff;
import org.jetbrains.annotations.NotNull;
import ui.MainPage;

/**
 * Created by Vladislav on 27.03.2016.
 */
public abstract class AbstractWindowDialog extends Window {

    final GridLayout layout;
    protected MainPage page;

    AbstractWindowDialog(String caption, MainPage page) {
        super(caption);
        this.page = page;
        layout = new GridLayout(2, 4);
    }

    BeanFieldGroup<Tariff> initTariffWindow() {
        TextField name = new TextField();
        TextField speed = new TextField();
        TextField cost = new TextField();
        layout.addComponent(new Label("Название: "), 0, 0);
        layout.addComponent(name, 1, 0);
        layout.addComponent(new Label("Скорость: "), 0, 1);
        layout.addComponent(speed, 1, 1);
        layout.addComponent(new Label("Цена: "), 0, 2);
        layout.addComponent(cost, 1, 2);
        layout.setMargin(true);
        layout.setSpacing(true);
        BeanFieldGroup<Tariff> beanFieldGroup = new BeanFieldGroup<>(Tariff.class);
        beanFieldGroup.bind(name, MainPage.NAME);
        beanFieldGroup.bind(speed, MainPage.SPEED);
        beanFieldGroup.bind(cost, MainPage.COST);
        setContent(layout);
        return beanFieldGroup;
    }

    BeanFieldGroup<Customer> constructCustomerWindow() {
        TextField name = new TextField();
        TextField phonenum = new TextField();
        TextField adress = new TextField();
        layout.addComponent(new Label("Имя: "), 0, 0);
        layout.addComponent(name, 1, 0);
        layout.addComponent(new Label("Номер телефона: "), 0, 1);
        layout.addComponent(phonenum, 1, 1);
        layout.addComponent(new Label("Адрес: "), 0, 2);
        layout.addComponent(adress, 1, 2);
        layout.setMargin(true);
        layout.setSpacing(true);
        BeanFieldGroup<Customer> beanFieldGroup = new BeanFieldGroup<>(Customer.class);
        beanFieldGroup.bind(name, MainPage.NAME);
        beanFieldGroup.bind(phonenum, MainPage.PHONENUM);
        beanFieldGroup.bind(adress, MainPage.ADDRESS);
        setContent(layout);
        return beanFieldGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);

    }

    @Override
    public int hashCode() {
        return super.hashCode() + 31;
    }
}
