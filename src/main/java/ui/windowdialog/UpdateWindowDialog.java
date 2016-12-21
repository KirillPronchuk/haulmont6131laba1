package ui.windowdialog;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import exceptions.UIException;
import model.Customer;
import model.Order;
import model.Tariff;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ui.MainPage;

import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vladislav on 27.03.2016.
 */
public class UpdateWindowDialog extends AbstractWindowDialog {

    private static final Logger LOGGER = Logger.getLogger(UpdateWindowDialog.class);

    final Button confirm;

    final Long id;

    private final Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");


    public UpdateWindowDialog(String tableName, Long id, MainPage page) {
        super("Редактирование " + tableName, page);
        this.id = id;

        confirm = new Button("Изменить");
        setModal(true);
        switch (tableName) {
            case MainPage.CUSTOMER_TABLE_NAME:
                customerUpdateWindow();
                break;
            case MainPage.ORDER_TABLE_NAME:
                orderUpdateWindow();
                break;
            case MainPage.TARIFF_TABLE_NAME:
                tariffUpdateWindow();
                break;
            default:
                break;
        }
        setSizeUndefined();
        setResizable(false);
    }

    private void customerUpdateWindow() {

        BeanFieldGroup<Customer> beanFieldGroup = constructCustomerWindow();
        layout.addComponent(confirm, 1, 3);
        beanFieldGroup.setItemDataSource(page.getCustomerContainer().getItem(id).getBean().clone());

        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            Customer customer = null;
            try {
                beanFieldGroup.commit();
                customer = beanFieldGroup.getItemDataSource().getBean();
                Matcher matcher = pattern.matcher(customer.getPhonenum());
                if (matcher.matches()) {
                    customer.setNumber(id);
                    page.getCustomerDao().update(customer);
                    page.getCustomerContainer().removeItem(id);
                    page.getCustomerContainer().addItem(id, customer);
//                    page.getCustomerContainer().addBean(customer);
                } else {
                    Notification.show("Номер телефона в данном формате не поддерживается", Notification.Type.WARNING_MESSAGE);
                }
            } catch (FieldGroup.CommitException e) {
                LOGGER.error("Error in customerUpdateWindow", e);
                if (customer != null) {
                    throw new UIException("Ошибка при изменении пользователья с №" + customer.getNumber());
                }
            }
            this.close();
        });
    }

    private void orderUpdateWindow() {

        NativeSelect customerID = new NativeSelect();
        customerID.setContainerDataSource(new BeanItemContainer<>(page.getCustomerDao().getType(), page.getCustomerDao().findAll()));
        NativeSelect tariffID = new NativeSelect();
        tariffID.setContainerDataSource(new BeanItemContainer<>(page.getTariffDao().getType(), page.getTariffDao().findAll()));
        DateField dateField = new DateField();
        dateField.setDateFormat("yyyy-dd-MM");

        layout.addComponent(new Label("№ Тарифа: "), 0, 0);
        layout.addComponent(tariffID, 1, 0);
        layout.addComponent(new Label("№ Пользователя: "), 0, 1);
        layout.addComponent(customerID, 1, 1);
        layout.addComponent(new Label("Дата заключения договора: "), 0, 2);
        layout.addComponent(dateField, 1, 2);
        layout.addComponent(confirm, 1, 3);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Order order = page.getOrderContainer().getItem(id).getBean().clone();
        tariffID.setValue(page.getTariffContainer().getItem(order.getTariffnum()).getBean());
        customerID.setValue(page.getCustomerContainer().getItem(order.getCustomernum()).getBean());
        dateField.setValue(Date.from(order.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        confirm.addClickListener((Button.ClickListener) clickEvent -> {

            tariffID.commit();
            customerID.commit();
            order.setNumber(id);

            Customer customer = (Customer) customerID.getValue();
            Tariff tariff = (Tariff) tariffID.getValue();

            order.setCustomernum(customer.getNumber());
            order.setTariffnum(tariff.getNumber());
            if (dateField.getValue() != null) {
                order.setDate(dateField.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            page.getOrderDao().update(order);
            page.getOrderContainer().removeItem(id);
            page.getOrderContainer().addItem(id, order);

            this.close();
        });
    }

    private void tariffUpdateWindow() {

        @NotNull final BeanFieldGroup<Tariff> beanFieldGroup = initTariffWindow();
        layout.addComponent(confirm, 1, 3);
        beanFieldGroup.setItemDataSource(page.getTariffContainer().getItem(id).getBean().clone());

        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            Tariff tariff = null;
            try {
                beanFieldGroup.commit();
                tariff = beanFieldGroup.getItemDataSource().getBean();
                tariff.setNumber(id);
                page.getTariffDao().update(tariff);
                page.getTariffContainer().removeItem(id);
                page.getTariffContainer().addItem(id, tariff);
//                page.getTariffContainer().addBean(tariff);
            } catch (FieldGroup.CommitException e) {
                LOGGER.error("Error in tariffUpdateWindow", e);
                if (tariff != null) {
                    throw new UIException("Ошибка при изменении тарифа с №:" + tariff.getNumber());
                }
            }
            this.close();
        });
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        UpdateWindowDialog that = (UpdateWindowDialog) o;

        if (!confirm.equals(that.confirm)) {
            return false;
        }
        if (!layout.equals(that.layout)) {
            return false;
        }
        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + confirm.hashCode();
        result = 31 * result + layout.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
