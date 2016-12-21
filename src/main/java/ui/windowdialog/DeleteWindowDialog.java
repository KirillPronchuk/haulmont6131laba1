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
                header = new Label(head + "абонента:  " + page.getCustomerContainer().getItem(id).getBean().toString() + " ?");
                break;
            case MainPage.ORDER_TABLE_NAME:
                header = new Label(head + tableName + "договор:  " + page.getOrderContainer().getItem(id).getBean().toString() + " ?");
                break;
            case MainPage.TARIFF_TABLE_NAME:
                header = new Label(head + tableName + "тариф:  " + page.getTariffContainer().getItem(id).getBean().toString() + " ?");
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
                    page.getCustomerDao().removeById(id);
                    page.getCustomerContainer().removeItem(id);
                    break;
                case MainPage.ORDER_TABLE_NAME:
                    page.getOrderDao().removeById(id);
                    page.getOrderContainer().removeItem(id);
                    break;
                case MainPage.TARIFF_TABLE_NAME:
                    page.getTariffDao().removeById(id);
                    page.getTariffContainer().removeItem(id);
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
