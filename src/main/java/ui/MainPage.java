package ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import exceptions.UIException;
import model.*;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.windowdialog.AddWindowDialog;
import ui.windowdialog.DeleteWindowDialog;
import ui.windowdialog.UpdateWindowDialog;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Vladislav on 12.03.2016.
 */
@Theme("valo")
@CDIUI("")
public class MainPage extends UI {

    public static final String NAME = "name";
    private static final String NUMBER = "number";
    public static final String PHONENUM = "phonenum";
    public static final String ADDRESS = "adress";
    public static final String SPEED = "speed";
    public static final String COST = "cost";
    private static final String CUSTOMERNUM = "customernum";
    private static final String TARIFFNUM = "tariffnum";
    private static final String DATE = "date";


    private static final String ADD = "Добавить";
    private static final String UPDATE = "Редактировать";
    private static final String REMOVE = "Удалить";

    private static final Logger LOGGER = Logger.getLogger(MainPage.class);

    public static final String TARIFF_TABLE_NAME = "Тарифы";
    public static final String ORDER_TABLE_NAME = "Договора";
    public static final String CUSTOMER_TABLE_NAME = "Абоненты";

    private JPAContainer<Customer> customer = JPAContainerFactory.make(Customer.class, "PortalMain");
    private JPAContainer<Order> order = JPAContainerFactory.make(Order.class, "PortalMain");
    private JPAContainer<Tariff> tariff = JPAContainerFactory.make(Tariff.class, "PortalMain");

    private final TabSheet tabsheet = new TabSheet();
    private Grid ordersTable = new Grid();
    private Grid customerTable = new Grid();
    private Grid tariffTable = new Grid();

    private MenuBar menubar = new MenuBar();

    final MenuBar.MenuItem file = menubar.addItem("Меню", null);

    private final Button newItem = new Button(ADD);
    private final Button deleteItem = new Button(REMOVE);
    private final Button modifyItem = new Button(UPDATE);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        initButtonSet();
        layout.addComponent(menubar);
        initTabSheet();
        layout.addComponent(tabsheet);

        int height = getCurrent().getPage().getBrowserWindowHeight() - 50;
        tabsheet.setHeight(String.valueOf(height));
        setContent(layout);
        this.setSizeFull();

        this.getPage().addBrowserWindowResizeListener((Page.BrowserWindowResizeListener) browserWindowResizeEvent -> {
            int height1 = getCurrent().getPage().getBrowserWindowHeight() - 50;
            tabsheet.setHeight(String.valueOf(height1));
            setContent(layout);
            setSizeFull();
        });

    }

    private void initButtonSet() {
        file.addItem(ADD, menuCommand2);
        file.addItem(REMOVE, menuCommand1);
        file.addItem(UPDATE, menuCommand);
    }


    private void initTabSheet() {
        fillCustomerTable();
        customerTable.setSizeFull();
        VerticalLayout customerTabSheet = new VerticalLayout();
        customerTabSheet.setCaption(CUSTOMER_TABLE_NAME);
        customerTabSheet.addComponent(customerTable);
        customerTabSheet.setSizeFull();
        tabsheet.addTab(customerTabSheet, CUSTOMER_TABLE_NAME);
        customerTabSheet.setMargin(true);

        fillTariffTable();
        tariffTable.setSizeFull();
        VerticalLayout tariffTabSheet = new VerticalLayout();
        tariffTabSheet.setCaption(TARIFF_TABLE_NAME);
        tariffTabSheet.addComponent(tariffTable);
        tariffTabSheet.setSizeFull();
        tabsheet.addTab(tariffTabSheet, TARIFF_TABLE_NAME);
        tariffTabSheet.setMargin(true);

        fillOrderTable();
        ordersTable.setSizeFull();
        VerticalLayout orderTabSheet = new VerticalLayout();
        orderTabSheet.setCaption(ORDER_TABLE_NAME);
        orderTabSheet.addComponent(ordersTable);
        orderTabSheet.setSizeFull();
        tabsheet.addTab(orderTabSheet, ORDER_TABLE_NAME);
        orderTabSheet.setMargin(true);
    }

    private void fillCustomerTable() {
        customer.removeAllItems();
        customerTable.setEditorEnabled(false);
        customerTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        customerTable.setContainerDataSource(customer);
        customerTable.setColumnOrder(NUMBER, NAME, PHONENUM, ADDRESS);
        customerTable.getColumn(NUMBER).setHeaderCaption("№");
        customerTable.getColumn(NAME).setHeaderCaption("Имя");
        customerTable.getColumn(PHONENUM).setHeaderCaption("Номер телефона");
        customerTable.getColumn(ADDRESS).setHeaderCaption("Адрес");
        initFilter(CUSTOMER_TABLE_NAME);
    }

    private void initFilter(String tableName) {
        @Nullable Grid table;
        @Nullable JPAContainer<? extends ModelItem> container;
        switch (tableName) {
            case ORDER_TABLE_NAME:
                table = ordersTable;
                container = order;
                break;
            case CUSTOMER_TABLE_NAME:
                table = customerTable;
                container = customer;
                break;
            case TARIFF_TABLE_NAME:
                table = tariffTable;
                container = tariff;
                break;
            default:
                container = null;
                table = null;
        }
        // Create a header row to hold column filters
        Grid.HeaderRow filterRow = null;
        if (table != null) {
            filterRow = table.appendHeaderRow();
        }

        // Set up a filter for all columns
        if (table != null) {
            for (Object pid : table.getContainerDataSource()
                    .getContainerPropertyIds()) {
                Grid.HeaderCell cell = filterRow.getCell(pid);

                // Have an input field to use for filter
                TextField filterField = new TextField();
                filterField.setColumns(0);

                // Update filter When the filter input is changed
                filterField.addTextChangeListener(change -> {
                    // Can't modify filters so need to replace
                    container.removeContainerFilters(pid);
                    // (Re)create the filter if necessary
                    if (!change.getText().isEmpty())
                        container.addContainerFilter(
                                new SimpleStringFilter(pid,
                                        change.getText(), true, false));
                });
                cell.setComponent(filterField);
            }
        }
    }

    private void fillTariffTable() {
        tariff.removeAllItems();
        tariffTable.setEditorEnabled(false);
        tariffTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        tariffTable.setContainerDataSource(tariff);
        tariffTable.setColumnOrder(NUMBER, NAME, COST, SPEED);
        tariffTable.getColumn(NUMBER).setHeaderCaption("№");
        tariffTable.getColumn(NAME).setHeaderCaption("Название");
        tariffTable.getColumn(COST).setHeaderCaption("Цена");
        tariffTable.getColumn(SPEED).setHeaderCaption("Скорость");
        initFilter(TARIFF_TABLE_NAME);
    }

    private void fillOrderTable() {
            order.removeAllItems();
            ordersTable.setEditorEnabled(false);
            ordersTable.setSelectionMode(Grid.SelectionMode.SINGLE);
            ordersTable.setContainerDataSource(order);
            ordersTable.setColumnOrder(NUMBER, CUSTOMERNUM, TARIFFNUM, DATE);
            ordersTable.getColumn(NUMBER).setHeaderCaption("№");
            ordersTable.getColumn(CUSTOMERNUM).setHeaderCaption("Абонент №");
            ordersTable.getColumn(TARIFFNUM).setHeaderCaption("Тариф №");
            ordersTable.getColumn(DATE).setHeaderCaption("Дата заключения договора");
            initFilter(ORDER_TABLE_NAME);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainPage.class, productionMode = false)
    public static class MyUIServlet extends VaadinCDIServlet {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        MainPage that = (MainPage) o;

        if (!tabsheet.equals(that.tabsheet)) {
            return false;
        }
        if (ordersTable != null ? !ordersTable.equals(that.ordersTable) : that.ordersTable != null) {
            return false;
        }
        if (customerTable != null ? !customerTable.equals(that.customerTable) : that.customerTable != null) {
            return false;
        }
        if (tariffTable != null ? !tariffTable.equals(that.tariffTable) : that.tariffTable != null) {
            return false;
        }
        if (!menubar.equals(that.menubar)) {
            return false;
        }
        if (!newItem.equals(that.newItem)) {
            return false;
        }
        if (!deleteItem.equals(that.deleteItem)) {
            return false;
        }
        return modifyItem.equals(that.modifyItem);

    }

    private MenuBar.Command menuCommand = (MenuBar.Command) selectedItem -> {
        Long id = null;
        if (tabsheet.getSelectedTab().getCaption().equals(CUSTOMER_TABLE_NAME) && customerTable.getSelectedRow() != null) {
            id = (Long) customerTable.getSelectedRow();
            getCurrent().addWindow(new UpdateWindowDialog(tabsheet.getSelectedTab().getCaption(), id, this));
            customerTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(TARIFF_TABLE_NAME) && tariffTable.getSelectedRow() != null) {
            id = (Long) tariffTable.getSelectedRow();
            getCurrent().addWindow(new UpdateWindowDialog(tabsheet.getSelectedTab().getCaption(), id, this));
            tariffTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(ORDER_TABLE_NAME) && ordersTable.getSelectedRow() != null) {
            id = (Long) ordersTable.getSelectedRow();
            getCurrent().addWindow(new UpdateWindowDialog(tabsheet.getSelectedTab().getCaption(), id, this));
            ordersTable.deselect(id);
        } else {
            Notification.show("Не выбрана строка в таблице.", Notification.Type.HUMANIZED_MESSAGE);
        }
    };

    private MenuBar.Command menuCommand1 = (MenuBar.Command) selectedItem -> {
        Long id;
        if (tabsheet.getSelectedTab().getCaption().equals(CUSTOMER_TABLE_NAME) && customerTable.getSelectedRow() != null) {
            id = (Long) customerTable.getSelectedRow();
            getCurrent().addWindow(new DeleteWindowDialog(id, tabsheet.getSelectedTab().getCaption(),this));
            customerTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(TARIFF_TABLE_NAME) && tariffTable.getSelectedRow() != null) {
            id = (Long) tariffTable.getSelectedRow();
            getCurrent().addWindow(new DeleteWindowDialog(id, tabsheet.getSelectedTab().getCaption(), this));
            tariffTable.deselect(id);
        } else if (tabsheet.getSelectedTab().getCaption().equals(ORDER_TABLE_NAME) && ordersTable.getSelectedRow() != null) {
            id = (Long) ordersTable.getSelectedRow();
            getCurrent().addWindow(new DeleteWindowDialog(id, tabsheet.getSelectedTab().getCaption(), this));
            ordersTable.deselect(id);
        } else {
            Notification.show("Не выбрана строка в таблице.", Notification.Type.HUMANIZED_MESSAGE);
        }
    };

    private MenuBar.Command menuCommand2 = (MenuBar.Command) selectedItem -> getCurrent().addWindow(new AddWindowDialog(tabsheet.getSelectedTab().getCaption(), this));

    public JPAContainer<Order> getOrders(){
        return order;
    }

    public JPAContainer<Customer> getCustomers(){
        return customer;
    }

    public JPAContainer<Tariff> getTariffs(){
        return tariff;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2;
    }
}