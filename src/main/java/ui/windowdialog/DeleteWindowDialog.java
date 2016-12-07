package ui.windowdialog;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.MainPage;

/**
 * Created by Vladislav on 27.03.2016.
 */
public class DeleteWindowDialog extends AbstractWindowDialog {

    private final Long id;
    private final String tableName;

    public DeleteWindowDialog(Long id, String tableName, MainPage page) {
        super("Удаление " + tableName, page);
        this.id = id;
        this.tableName = tableName;
        setModal(true);
        abstractDeleteWindow();
        setContent(layout);
        setResizable(false);
        setSizeUndefined();
    }

    private void abstractDeleteWindow() {
        @Nullable Label header = null;
        @NotNull String head = "Вы точно хотите удалить ";
        switch (tableName) {
            case MainPage.CUSTOMER_TABLE_NAME:
                header = new Label(head + "абонента:  " + page.getCustomers().getItem(id).getEntity().toString() + " ?");
                break;
            case MainPage.ORDER_TABLE_NAME:
                header = new Label(head + tableName + "договор:  " + page.getOrders().getItem(id).getEntity().toString() + " ?");
                break;
            case MainPage.TARIFF_TABLE_NAME:
                header = new Label(head + tableName + "тариф:  " + page.getTariffs().getItem(id).getEntity().toString() + " ?");
                break;
            default:
                break;
        }
        if (header != null) {
            layout.addComponent(header);
        }
        @NotNull final Button confirm = new Button("Да");
        layout.addComponent(confirm);
        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            switch (tableName) {
                case MainPage.CUSTOMER_TABLE_NAME:
                    page.getCustomers().removeItem(id);
                    break;
                case MainPage.ORDER_TABLE_NAME:
                    page.getOrders().removeItem(id);
                    break;
                case MainPage.TARIFF_TABLE_NAME:
                    page.getTariffs().removeItem(id);
                    break;
                default:
                    break;
            }
            this.close();
        });
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        DeleteWindowDialog that = (DeleteWindowDialog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return tableName != null ? tableName.equals(that.tableName) : that.tableName == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        return result;
    }
}
