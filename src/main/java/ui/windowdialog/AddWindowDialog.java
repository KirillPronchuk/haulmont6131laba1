package ui.windowdialog;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.*;
import exceptions.UIException;
import model.Customer;
import model.Order;
import model.Tariff;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ui.MainPage;

import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vladislav on 27.03.2016.
 */

public class AddWindowDialog extends AbstractWindowDialog {

    @NotNull
    private static final Logger LOGGER = Logger.getLogger(UpdateWindowDialog.class);

    @NotNull
    private final Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");

    @NotNull
    final Button confirm;

    public AddWindowDialog(@NotNull String tableName, MainPage page) {
        super("Добавить " + tableName, page);
        confirm = new Button("Добавить");
        layout.addComponent(confirm, 1, 3);
        this.setModal(true);
        switch (tableName) {
            case MainPage.CUSTOMER_TABLE_NAME:
                customerAddWindow();
                break;
            case MainPage.ORDER_TABLE_NAME:
                orderAddWindow();
                break;
            case MainPage.TARIFF_TABLE_NAME:
                tariffAddWindow();
                break;
            default:
                break;
        }
        this.setContent(layout);
    }

    private void customerAddWindow() {
        BeanFieldGroup<Customer> beanFieldGroup = constructCustomerWindow();
        beanFieldGroup.setItemDataSource(new Customer());

        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                beanFieldGroup.commit();
                Customer customer = beanFieldGroup.getItemDataSource().getBean();
                Matcher matcher = pattern.matcher(customer.getPhonenum());
                if (matcher.matches()) {
                    page.getCustomerDao();
                    page.getCustomerDao().persist(customer);
                    page.getCustomerContainer().addBean(customer);
                    this.close();
                } else {
                    Notification.show("Номер телефона в данном формате не поддерживается", Notification.Type.WARNING_MESSAGE);
                }
            } catch (FieldGroup.CommitException e) {
                LOGGER.error("Error in customerAddWindow", e);
                throw new UIException("Ошибка при добавлении в таблицу пользователей");
            }
        });
    }

    private void orderAddWindow() {
            @NotNull final NativeSelect customerId = new NativeSelect();
            customerId.setContainerDataSource(page.getCustomerContainer());
            @NotNull final NativeSelect tariffId = new NativeSelect();
            tariffId.setContainerDataSource(page.getTariffContainer());
            @NotNull final DateField dateField = new DateField();
            dateField.setDateFormat("yyyy-dd-MM");

            layout.addComponent(new Label("№ Тарифа: "), 0, 0);
            layout.addComponent(tariffId, 1, 0);
            layout.addComponent(new Label("№ Пользователя: "), 0, 1);
            layout.addComponent(customerId, 1, 1);
            layout.addComponent(new Label("Дата заключения договора: "), 0, 2);
            layout.addComponent(dateField, 1, 2);
            layout.setMargin(true);
            layout.setSpacing(true);

            confirm.addClickListener((Button.ClickListener) clickEvent -> {
                tariffId.commit();
                customerId.commit();
                Order order = new Order();
                Customer customer = (Customer) customerId.getValue();
                Tariff tariff = (Tariff) tariffId.getValue();

                order.setCustomernum(customer.getNumber());
                order.setTariffnum(tariff.getNumber());
                if (dateField.getValue() != null) {
                    order.setDate(dateField.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                page.getOrderDao().persist(order);
                page.getOrderContainer().addBean(order);
                this.close();
            });
    }

    private void tariffAddWindow() {
        BeanFieldGroup<Tariff> beanFieldGroup = initTariffWindow();
        beanFieldGroup.setItemDataSource(new Tariff());

        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                beanFieldGroup.commit();
                Tariff tariff = beanFieldGroup.getItemDataSource().getBean();
                page.getTariffDao().persist(tariff);
                page.getTariffContainer().addBean(tariff);
                this.close();
            } catch (FieldGroup.CommitException e) {
                LOGGER.error("Error in tariffAddWindow", e);
                throw new UIException("Ошибка при добавлении в таблицу тарифов");
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        AddWindowDialog that = (AddWindowDialog) o;

        return confirm.equals(that.confirm);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + confirm.hashCode();
        return result;
    }
}
